package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.Key;
import com.avantir.blowfish.entity.KeyMap;
import com.avantir.blowfish.entity.Transaction;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.messaging.Message;
import com.avantir.blowfish.repository.TransactionRepository;
import com.avantir.blowfish.utils.BlowfishUtil;
import com.solab.iso8583.IsoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    IsoMessageService isoMessageService;
    @Autowired
    KeyManagementService keyManagementService;
    @Autowired
    SecurityService securityService;
    @Autowired
    KeyService keyService;
    @Autowired
    KeyMapService keyMapService;


    @Transactional(readOnly=false)
    public Optional<Transaction> create(Transaction transaction) {
        return Optional.ofNullable(transactionRepository.save(transaction));
    }

    @Transactional(readOnly=true)
    public Optional<Transaction> findByTransactionId(Long transactionId) {
        return transactionRepository.findByTransactionId(transactionId);
    }

    @Transactional(readOnly=true)
    public Optional<Transaction> findByOriginalDataElements(String msgType, String stan, String transmissionDateTime,
                                          String acquirerInstId, String forwardingInstId,
                                          String terminalId, String merchantId) {

        return transactionRepository.findByMsgTypeStanTransmissionDateTimeAcquirerInstIdForwardingInstIdTerminalIdMerchantIdAllIgnoringCase(msgType, stan, transmissionDateTime, acquirerInstId, forwardingInstId, terminalId, merchantId);
    }


    //@Cacheable(value = "acquirer", key = "#root.target.ALL_ACQUIRER")
    @Transactional(readOnly=true)
    public Optional<List<Transaction>> findAll() {
        return Optional.ofNullable(transactionRepository.findAll());
    }


    public Transaction copyMessage(Message message){

        KeyMap hashKeyMap = keyMapService.findByCode("PAN_HASH").orElseThrow(() -> new BlowfishEntityNotFoundException("PAN_HASH KeyMap"));
        Key hashKey = keyService.findByKeyId(hashKeyMap.getKeyId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Key " + hashKeyMap.getKeyId()));
        KeyMap panEncryptKeyMap = keyMapService.findByCode("PAN_ENCRYPT").orElseThrow(() -> new BlowfishEntityNotFoundException("PAN_ENCRYPT KeyMap"));
        Key encryptionKey = keyService.findByKeyId(panEncryptKeyMap.getKeyId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Key " + panEncryptKeyMap.getKeyId()));

        Transaction transaction = new Transaction();
        transaction.setAccId1(message.getF100());
        transaction.setAccId2(message.getF103());
        transaction.setAcquirerInstId(message.getF32());
        transaction.setAdditionalData(message.getF8());
        transaction.setAlgoVersion(hashKey.getKeyId());
        transaction.setAmount(message.getF4());
        transaction.setAuthAgentIdCode(message.getF58());
        transaction.setCardSeqNr(message.getF23());
        transaction.setConversionDate(message.getF16());
        String proc = message.getF3();
        transaction.setDestAccType(isoMessageService.getToAccountType(proc));
        transaction.setForwardingInstId(message.getF33());
        transaction.setIccData(message.getF55());
        transaction.setLocation(message.getF43());
        transaction.setLocalDate(message.getF13());
        transaction.setLocalTime(message.getF12());
        transaction.setMerchantId(message.getF42());
        transaction.setMerchantType(message.getF18());
        transaction.setMessageReasonCode(message.getF56());
        transaction.setMsgType(message.getF0());
        transaction.setOriginalDataElement(message.getF90());

        String pan = message.getF2();
        transaction.setPanHash(securityService.getHash(pan, hashKey.getData(), "SHA-512").orElse(null));
        transaction.setPanMask(securityService.maskPan(pan, 6, 4, '*').orElse("****************"));
        transaction.setPanEncrypted(keyManagementService.encrypt3DESWithHSM(encryptionKey.getData(), pan).orElse(null));

        transaction.setPayee(message.getF98());
        transaction.setPosEntryMode(message.getF22());
        transaction.setPosCondCode(message.getF25());
        transaction.setPosDataCode(message.getF123());
        transaction.setPosPinCaptureCode(message.getF26());
        transaction.setReceivingInstIdCode(message.getF100());
        transaction.setReplacementAmt(message.getF95());
        transaction.setRetRefNr(message.getF37());
        transaction.setServiceRestrictionCode(message.getF40());
        transaction.setSettlementConversionRate(message.getF9());
        transaction.setSettlementCurrency(message.getF50());
        transaction.setSettlementDate(message.getF15());
        transaction.setSettlementFeeAmt(message.getF29());
        transaction.setSettlementProcFeeAmt(message.getF31());
        transaction.setSrcAccType(isoMessageService.getFromAccountType(proc));
        transaction.setStan(message.getF11());
        transaction.setTransmissionDateTime(message.getF7());
        transaction.setTerminalId(message.getF41());
        transaction.setTranCurrency(message.getF49());
        transaction.setTranFeeAmt(message.getF28());
        transaction.setTranProcFeeAmt(message.getF30());
        transaction.setTranType(isoMessageService.getTranType(proc));

        transaction.setPaymentInfo(message.getF60());
        transaction.setExtendedPaymentCode(message.getF67());
        transaction.setNfcData(message.getF124());

        transaction.setTmsKey(message.getF127_2());

        return transaction;
    }


    public Transaction copyIsoMessageRequest(IsoMessage isoMessage){

        KeyMap hashKeyMap = keyMapService.findByCode("PAN_HASH").orElseThrow(() -> new BlowfishEntityNotFoundException("PAN_HASH KeyMap"));
        Key hashKey = keyService.findByKeyId(hashKeyMap.getKeyId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Key " + hashKeyMap.getKeyId()));
        KeyMap panEncryptKeyMap = keyMapService.findByCode("PAN_ENCRYPT").orElseThrow(() -> new BlowfishEntityNotFoundException("PAN_ENCRYPT KeyMap"));
        Key encryptionKey = keyService.findByKeyId(panEncryptKeyMap.getKeyId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Key " + panEncryptKeyMap.getKeyId()));

        Transaction transaction = new Transaction();
        transaction.setAccId1(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f102));
        transaction.setAccId2(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f103));
        transaction.setAcquirerInstId(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f32));
        transaction.setAdditionalData(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f48));
        transaction.setAlgoVersion(hashKey.getKeyId());
        transaction.setAmount(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f4));
        transaction.setAuthAgentIdCode(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f58));
        transaction.setCardSeqNr(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f23));
        transaction.setConversionDate(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f16));
        transaction.setDestAccType(isoMessageService.getToAccountType(isoMessage));
        transaction.setForwardingInstId(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f33));
        transaction.setIccData(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f55));
        transaction.setLocation(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f43));
        transaction.setLocalDate(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f13));
        transaction.setLocalTime(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f12));
        transaction.setMerchantId(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f42));
        transaction.setMerchantType(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f18));
        transaction.setMessageReasonCode(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f56));
        transaction.setMsgType(String.format("%04X", isoMessage.getType()));
        transaction.setOriginalDataElement(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f90));

        String pan = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f2);
        transaction.setPanHash(securityService.getHash(pan, hashKey.getData(), "SHA-512").orElse(null));
        transaction.setPanMask(securityService.maskPan(pan, 6, 4, '*').orElse("****************"));
        transaction.setPanEncrypted(keyManagementService.encrypt3DESWithHSM(encryptionKey.getData(), pan).orElse(null));

        transaction.setPayee(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f98));
        transaction.setPosEntryMode(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f22));
        transaction.setPosCondCode(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f25));
        transaction.setPosDataCode(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f123));
        transaction.setPosPinCaptureCode(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f26));
        transaction.setReceivingInstIdCode(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f100));
        transaction.setReplacementAmt(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f95));
        transaction.setRetRefNr(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f37));
        transaction.setServiceRestrictionCode(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f40));
        transaction.setSettlementConversionRate(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f9));
        transaction.setSettlementCurrency(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f50));
        transaction.setSettlementDate(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f15));
        transaction.setSettlementFeeAmt(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f29));
        transaction.setSettlementProcFeeAmt(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f31));
        transaction.setSrcAccType(isoMessageService.getFromAccountType(isoMessage));
        transaction.setStan(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f11));
        transaction.setTransmissionDateTime(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f7));
        transaction.setTerminalId(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f41));
        transaction.setTranCurrency(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f49));
        transaction.setTranFeeAmt(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f28));
        transaction.setTranProcFeeAmt(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f30));
        transaction.setTranType(isoMessageService.getTranType(isoMessage));

        transaction.setPaymentInfo(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f60));
        transaction.setExtendedPaymentCode(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f67));
        transaction.setNfcData(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f124));

        transaction.setTmsKey(isoMessageService.getCompositeFieldValue(isoMessage, IsoMessageService.f127, IsoMessageService.f2));

        return transaction;
    }

    public Transaction copyIsoMessageResponse(IsoMessage isoMessage, Transaction transaction){

        transaction.setAuthIdResp(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f38));
        transaction.setResp_code(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f39));
        transaction.setAdditionalResponseData(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f44));
        transaction.setAdditionalAmt(isoMessageService.getFieldValue(isoMessage, IsoMessageService.f54));

        return transaction;
    }



}

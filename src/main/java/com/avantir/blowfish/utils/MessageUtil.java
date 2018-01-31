package com.avantir.blowfish.utils;

import com.avantir.blowfish.messaging.Message;
import com.avantir.blowfish.model.Transaction;
import com.solab.iso8583.IsoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lekanomotayo on 12/01/2018.
 */
public class MessageUtil {

    private static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);


    public static Transaction copyMessage(Message message, long keyVersionId, String salt){

        Transaction transaction = new Transaction();
        transaction.setAccId1(message.getF100());
        transaction.setAccId2(message.getF103());
        transaction.setAcquirerInstId(message.getF32());
        transaction.setAdditionalData(message.getF8());
        transaction.setAlgoVersion(keyVersionId);
        transaction.setAmount(message.getF4());
        transaction.setAuthAgentIdCode(message.getF58());
        transaction.setCardSeqNr(message.getF23());
        transaction.setConversionDate(message.getF16());
        String proc = message.getF3();
        transaction.setDestAccType(getToAccountType(proc));
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
        transaction.setPanHash(BlowfishUtil.getSHA512(pan, salt));
        transaction.setPanMask(BlowfishUtil.maskPan(pan, 6, 4, '*'));

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
        transaction.setSrcAccType(getFromAccountType(proc));
        transaction.setStan(message.getF11());
        transaction.setTransmissionDateTime(message.getF7());
        transaction.setTerminalId(message.getF41());
        transaction.setTranCurrency(message.getF49());
        transaction.setTranFeeAmt(message.getF28());
        transaction.setTranProcFeeAmt(message.getF30());
        transaction.setTranType(getTranType(proc));

        transaction.setPaymentInfo(message.getF60());
        transaction.setExtendedPaymentCode(message.getF67());
        transaction.setNfcData(message.getF124());

        transaction.setTmsKey(message.getF127_2());

        return transaction;
    }


    public static Transaction copyIsoMessageRequest(IsoMessage isoMessage, long keyVersionId, String salt){

        Transaction transaction = new Transaction();
        transaction.setAccId1(IsoUtil.getFieldValue(isoMessage, IsoUtil.f102));
        transaction.setAccId2(IsoUtil.getFieldValue(isoMessage, IsoUtil.f103));
        transaction.setAcquirerInstId(IsoUtil.getFieldValue(isoMessage, IsoUtil.f32));
        transaction.setAdditionalData(IsoUtil.getFieldValue(isoMessage, IsoUtil.f48));
        transaction.setAlgoVersion(keyVersionId);
        transaction.setAmount(IsoUtil.getFieldValue(isoMessage, IsoUtil.f4));
        transaction.setAuthAgentIdCode(IsoUtil.getFieldValue(isoMessage, IsoUtil.f58));
        transaction.setCardSeqNr(IsoUtil.getFieldValue(isoMessage, IsoUtil.f23));
        transaction.setConversionDate(IsoUtil.getFieldValue(isoMessage, IsoUtil.f16));
        transaction.setDestAccType(IsoUtil.getToAccountType(isoMessage));
        transaction.setForwardingInstId(IsoUtil.getFieldValue(isoMessage, IsoUtil.f33));
        transaction.setIccData(IsoUtil.getFieldValue(isoMessage, IsoUtil.f55));
        transaction.setLocation(IsoUtil.getFieldValue(isoMessage, IsoUtil.f43));
        transaction.setLocalDate(IsoUtil.getFieldValue(isoMessage, IsoUtil.f13));
        transaction.setLocalTime(IsoUtil.getFieldValue(isoMessage, IsoUtil.f12));
        transaction.setMerchantId(IsoUtil.getFieldValue(isoMessage, IsoUtil.f42));
        transaction.setMerchantType(IsoUtil.getFieldValue(isoMessage, IsoUtil.f18));
        transaction.setMessageReasonCode(IsoUtil.getFieldValue(isoMessage, IsoUtil.f56));
        transaction.setMsgType(String.format("%04X", isoMessage.getType()));
        transaction.setOriginalDataElement(IsoUtil.getFieldValue(isoMessage, IsoUtil.f90));

        String pan = IsoUtil.getFieldValue(isoMessage, IsoUtil.f2);
        transaction.setPanHash(BlowfishUtil.getSHA512(pan, salt));
        transaction.setPanMask(BlowfishUtil.maskPan(pan, 6, 4, '*'));

        transaction.setPayee(IsoUtil.getFieldValue(isoMessage, IsoUtil.f98));
        transaction.setPosEntryMode(IsoUtil.getFieldValue(isoMessage, IsoUtil.f22));
        transaction.setPosCondCode(IsoUtil.getFieldValue(isoMessage, IsoUtil.f25));
        transaction.setPosDataCode(IsoUtil.getFieldValue(isoMessage, IsoUtil.f123));
        transaction.setPosPinCaptureCode(IsoUtil.getFieldValue(isoMessage, IsoUtil.f26));
        transaction.setReceivingInstIdCode(IsoUtil.getFieldValue(isoMessage, IsoUtil.f100));
        transaction.setReplacementAmt(IsoUtil.getFieldValue(isoMessage, IsoUtil.f95));
        transaction.setRetRefNr(IsoUtil.getFieldValue(isoMessage, IsoUtil.f37));
        transaction.setServiceRestrictionCode(IsoUtil.getFieldValue(isoMessage, IsoUtil.f40));
        transaction.setSettlementConversionRate(IsoUtil.getFieldValue(isoMessage, IsoUtil.f9));
        transaction.setSettlementCurrency(IsoUtil.getFieldValue(isoMessage, IsoUtil.f50));
        transaction.setSettlementDate(IsoUtil.getFieldValue(isoMessage, IsoUtil.f15));
        transaction.setSettlementFeeAmt(IsoUtil.getFieldValue(isoMessage, IsoUtil.f29));
        transaction.setSettlementProcFeeAmt(IsoUtil.getFieldValue(isoMessage, IsoUtil.f31));
        transaction.setSrcAccType(IsoUtil.getFromAccountType(isoMessage));
        transaction.setStan(IsoUtil.getFieldValue(isoMessage, IsoUtil.f11));
        transaction.setTransmissionDateTime(IsoUtil.getFieldValue(isoMessage, IsoUtil.f7));
        transaction.setTerminalId(IsoUtil.getFieldValue(isoMessage, IsoUtil.f41));
        transaction.setTranCurrency(IsoUtil.getFieldValue(isoMessage, IsoUtil.f49));
        transaction.setTranFeeAmt(IsoUtil.getFieldValue(isoMessage, IsoUtil.f28));
        transaction.setTranProcFeeAmt(IsoUtil.getFieldValue(isoMessage, IsoUtil.f30));
        transaction.setTranType(IsoUtil.getTranType(isoMessage));

        transaction.setPaymentInfo(IsoUtil.getFieldValue(isoMessage, IsoUtil.f60));
        transaction.setExtendedPaymentCode(IsoUtil.getFieldValue(isoMessage, IsoUtil.f67));
        transaction.setNfcData(IsoUtil.getFieldValue(isoMessage, IsoUtil.f124));

        transaction.setTmsKey(IsoUtil.getCompositeFieldValue(isoMessage, IsoUtil.f127, IsoUtil.f2));

        return transaction;
    }

    public static Transaction copyIsoMessageResponse(IsoMessage isoMessage, Transaction transaction){

        transaction.setAuthIdResp(IsoUtil.getFieldValue(isoMessage, IsoUtil.f38));
        transaction.setResp_code(IsoUtil.getFieldValue(isoMessage, IsoUtil.f39));
        transaction.setAdditionalResponseData(IsoUtil.getFieldValue(isoMessage, IsoUtil.f44));
        transaction.setAdditionalAmt(IsoUtil.getFieldValue(isoMessage, IsoUtil.f54));

        return transaction;
    }


    public static String getTranType(String proc){
        try{
            return proc == null ? null : proc.substring(0,2);
        }
        catch(Exception ex){
            logger.error("getTranType: Error getting from transaction type", ex);
            ex.printStackTrace();
        }
        return null;
    }

    public static String getFromAccountType(String proc){
        try{
            return proc == null ? null : proc.substring(2,4);
        }
        catch(Exception ex){
            logger.error("getFromAccountType: Error getting from account type", ex);
            ex.printStackTrace();
        }
        return null;
    }

    public static String getToAccountType(String proc){
        try{
            return proc == null ? null : proc.substring(4,6);
        }
        catch(Exception ex){
            logger.error("getToAccountType: Error getting to account type", ex);
            ex.printStackTrace();
        }
        return null;
    }

}

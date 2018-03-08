package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * Created by lekanomotayo on 15/01/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_transactions", indexes = {@Index(name = "transaction_idx", columnList = "msg_type,stan,transmission_date_time,acquirer_inst_id,forwarding_inst_id,terminal_id,merchant_id", unique = true)})
@SuppressWarnings("serial")
public class Transaction extends BaseModel{

    //for operations - log for support: query trxn state, settlement, reconciliation, dispute resolution
    // who, what, when, why, where
    // other data, send to NoSQL

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long transactionId;
    @Column(name = "msg_type", nullable = false)
    String msgType;
    @Column(name = "stan", nullable = false)
    String stan;
    @Column(name = "transmission_date_time", nullable = false)
    String transmissionDateTime;
    @Column(name = "acquirer_inst_id", nullable = false)
    String acquirerInstId;
    @Column(name = "forwarding_inst_id", nullable = true)
    String forwardingInstId;
    @Column(name = "terminal_id", nullable = false)
    String terminalId;
    @Column(name = "merchant_id", nullable = false)
    String merchantId;


    @Column(name = "pan_hash", nullable = false)
    String panHash;
    @Column(name = "pan_mask", nullable = false)
    String panMask;

    @Column(name = "tran_type", nullable = false)
    String tranType;
    @Column(name = "src_acc_type", nullable = false)
    String srcAccType;
    @Column(name = "dest_acc_type", nullable = false)
    String destAccType;
    @Column(name = "amount", nullable = false)
    String amount;
    @Column(name = "settlement_conversion_rate", nullable = true)
    String settlementConversionRate;


    @Column(name = "local_time", nullable = false)
    String localTime;
    @Column(name = "local_date", nullable = false)
    String localDate;
    @Column(name = "settlement_date", nullable = true)
    String settlementDate;
    @Column(name = "conversion_date", nullable = true)
    String conversionDate;

    @Column(name = "merchant_type", nullable = false)
    String merchantType;
    @Column(name = "pos_entry_mode", nullable = false)
    String posEntryMode;
    @Column(name = "cardSeqNr", nullable = false)
    String cardSeqNr;
    @Column(name = "pos_cond_code", nullable = false)
    String posCondCode;
    @Column(name = "pos_pin_capture_code", nullable = false)
    String posPinCaptureCode;
    @Column(name = "tran_fee_amt", nullable = true)
    String tranFeeAmt;
    @Column(name = "settlement_fee_amt", nullable = true)
    String settlementFeeAmt;
    @Column(name = "tran_proc_fee_amt", nullable = true)
    String tranProcFeeAmt;
    @Column(name = "settlement_proc_fee_amt", nullable = true)
    String settlementProcFeeAmt;

    @Column(name = "ret_ref_nr", nullable = true)
    String retRefNr;

    @Column(name = "auth_id_resp", nullable = true)
    String authIdResp;
    @Column(name = "resp_code", nullable = true)
    String resp_code;

    @Column(name = "service_restriction_code", nullable = true)
    String serviceRestrictionCode;

    @Column(name = "location", nullable = false)
    String location;

    @Column(name = "additional_response_data", nullable = true)
    String additionalResponseData;
    @Column(name = "additionalData", nullable = true)
    String additionalData;

    @Column(name = "tran_currency", nullable = true)
    String tranCurrency;
    @Column(name = "settlement_currency", nullable = true)
    String settlementCurrency;

    @Column(name = "additional_amt", nullable = true)
    String additionalAmt;
    @Column(name = "icc_data", nullable = true)
    String iccData;

    @Column(name = "message_reason_code", nullable = true)
    String messageReasonCode;
    @Column(name = "auth_agent_id_code", nullable = true)
    String authAgentIdCode;
    @Column(name = "payment_info", nullable = true)
    String paymentInfo;
    @Column(name = "extended_payment_code", nullable = true)
    String extendedPaymentCode;
    @Column(name = "original_data_element", nullable = true)
    String originalDataElement;
    @Column(name = "replacement_amt", nullable = true)
    String replacementAmt;
    @Column(name = "payee", nullable = true)
    String payee;
    @Column(name = "receiving_inst_id_code", nullable = true)
    String receivingInstIdCode;

    @Column(name = "acc_id_1", nullable = true)
    String accId1;
    @Column(name = "acc_id_2", nullable = true)
    String accId2;

    @Column(name = "pos_data_code", nullable = false)
    String posDataCode;

    @Column(name = "nfc_data", nullable = true)
    String nfcData;

    @Column(name = "tms_key", nullable = true)
    String tmsKey;

    @Column(name = "algo_version", nullable = false)
    Long algoVersion; // PAN hash algorithm version


    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getPanHash() {
        return panHash;
    }

    public void setPanHash(String panHash) {
        this.panHash = panHash;
    }

    public String getPanMask() {
        return panMask;
    }

    public void setPanMask(String panMask) {
        this.panMask = panMask;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getSrcAccType() {
        return srcAccType;
    }

    public void setSrcAccType(String srcAccType) {
        this.srcAccType = srcAccType;
    }

    public String getDestAccType() {
        return destAccType;
    }

    public void setDestAccType(String destAccType) {
        this.destAccType = destAccType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSettlementConversionRate() {
        return settlementConversionRate;
    }

    public void setSettlementConversionRate(String settlementConversionRate) {
        this.settlementConversionRate = settlementConversionRate;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getTransmissionDateTime() {
        return transmissionDateTime;
    }

    public void setTransmissionDateTime(String transmissionDateTime) {
        this.transmissionDateTime = transmissionDateTime;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getConversionDate() {
        return conversionDate;
    }

    public void setConversionDate(String conversionDate) {
        this.conversionDate = conversionDate;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getPosEntryMode() {
        return posEntryMode;
    }

    public void setPosEntryMode(String posEntryMode) {
        this.posEntryMode = posEntryMode;
    }

    public String getCardSeqNr() {
        return cardSeqNr;
    }

    public void setCardSeqNr(String cardSeqNr) {
        this.cardSeqNr = cardSeqNr;
    }

    public String getPosCondCode() {
        return posCondCode;
    }

    public void setPosCondCode(String posCondCode) {
        this.posCondCode = posCondCode;
    }

    public String getPosPinCaptureCode() {
        return posPinCaptureCode;
    }

    public void setPosPinCaptureCode(String posPinCaptureCode) {
        this.posPinCaptureCode = posPinCaptureCode;
    }

    public String getTranFeeAmt() {
        return tranFeeAmt;
    }

    public void setTranFeeAmt(String tranFeeAmt) {
        this.tranFeeAmt = tranFeeAmt;
    }

    public String getSettlementFeeAmt() {
        return settlementFeeAmt;
    }

    public void setSettlementFeeAmt(String settlementFeeAmt) {
        this.settlementFeeAmt = settlementFeeAmt;
    }

    public String getTranProcFeeAmt() {
        return tranProcFeeAmt;
    }

    public void setTranProcFeeAmt(String tranProcFeeAmt) {
        this.tranProcFeeAmt = tranProcFeeAmt;
    }

    public String getSettlementProcFeeAmt() {
        return settlementProcFeeAmt;
    }

    public void setSettlementProcFeeAmt(String settlementProcFeeAmt) {
        this.settlementProcFeeAmt = settlementProcFeeAmt;
    }

    public String getAcquirerInstId() {
        return acquirerInstId;
    }

    public void setAcquirerInstId(String acquirerInstId) {
        this.acquirerInstId = acquirerInstId;
    }

    public String getForwardingInstId() {
        return forwardingInstId;
    }

    public void setForwardingInstId(String forwardingInstId) {
        this.forwardingInstId = forwardingInstId;
    }

    public String getRetRefNr() {
        return retRefNr;
    }

    public void setRetRefNr(String retRefNr) {
        this.retRefNr = retRefNr;
    }

    public String getAuthIdResp() {
        return authIdResp;
    }

    public void setAuthIdResp(String authIdResp) {
        this.authIdResp = authIdResp;
    }

    public String getResp_code() {
        return resp_code;
    }

    public void setResp_code(String resp_code) {
        this.resp_code = resp_code;
    }

    public String getServiceRestrictionCode() {
        return serviceRestrictionCode;
    }

    public void setServiceRestrictionCode(String serviceRestrictionCode) {
        this.serviceRestrictionCode = serviceRestrictionCode;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAdditionalResponseData() {
        return additionalResponseData;
    }

    public void setAdditionalResponseData(String additionalResponseData) {
        this.additionalResponseData = additionalResponseData;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public String getTranCurrency() {
        return tranCurrency;
    }

    public void setTranCurrency(String tranCurrency) {
        this.tranCurrency = tranCurrency;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public String getAdditionalAmt() {
        return additionalAmt;
    }

    public void setAdditionalAmt(String additionalAmt) {
        this.additionalAmt = additionalAmt;
    }

    public String getIccData() {
        return iccData;
    }

    public void setIccData(String iccData) {
        this.iccData = iccData;
    }

    public String getMessageReasonCode() {
        return messageReasonCode;
    }

    public void setMessageReasonCode(String messageReasonCode) {
        this.messageReasonCode = messageReasonCode;
    }

    public String getAuthAgentIdCode() {
        return authAgentIdCode;
    }

    public void setAuthAgentIdCode(String authAgentIdCode) {
        this.authAgentIdCode = authAgentIdCode;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getExtendedPaymentCode() {
        return extendedPaymentCode;
    }

    public void setExtendedPaymentCode(String extendedPaymentCode) {
        this.extendedPaymentCode = extendedPaymentCode;
    }

    public String getOriginalDataElement() {
        return originalDataElement;
    }

    public void setOriginalDataElement(String originalDataElement) {
        this.originalDataElement = originalDataElement;
    }

    public String getReplacementAmt() {
        return replacementAmt;
    }

    public void setReplacementAmt(String replacementAmt) {
        this.replacementAmt = replacementAmt;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getReceivingInstIdCode() {
        return receivingInstIdCode;
    }

    public void setReceivingInstIdCode(String receivingInstIdCode) {
        this.receivingInstIdCode = receivingInstIdCode;
    }

    public String getAccId1() {
        return accId1;
    }

    public void setAccId1(String accId1) {
        this.accId1 = accId1;
    }

    public String getAccId2() {
        return accId2;
    }

    public void setAccId2(String accId2) {
        this.accId2 = accId2;
    }

    public String getPosDataCode() {
        return posDataCode;
    }

    public void setPosDataCode(String posDataCode) {
        this.posDataCode = posDataCode;
    }

    public String getNfcData() {
        return nfcData;
    }

    public void setNfcData(String nfcData) {
        this.nfcData = nfcData;
    }

    public Long getAlgoVersion() {
        return algoVersion;
    }

    public void setAlgoVersion(Long algoVersion) {
        this.algoVersion = algoVersion;
    }

    public String getTmsKey() {
        return tmsKey;
    }

    public void setTmsKey(String tmsKey) {
        this.tmsKey = tmsKey;
    }
}

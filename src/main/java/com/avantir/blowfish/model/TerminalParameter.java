package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 18/02/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_terminal_parameters")
@SuppressWarnings("serial")
public class TerminalParameter extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    String name;
    @Column(name = "tms_endpoint_id", nullable = false)
    long tmsEndpointId;
    @Column(name = "ctmk_key_id", nullable = false)
    long ctmkKeyId;
    @Column(name = "bdk_key_id", nullable = false)
    long bdkKeyId;
    @Column(name = "acquirer_id", nullable = false)
    long acquirerId;
    @Column(name = "key_download_time_in_min", nullable = false)
    int keyDownloadTimeInMin;
    @Column(name = "key_download_interval_in_min", nullable = false)
    int keyDownloadIntervalInMin;
    @Column(name = "terminal_type", nullable = false)
    int terminalType;
    @Column(name = "terminal_capabilities", nullable = false)
    String terminalCapabilities;
    @Column(name = "terminal_extra_capabilities", nullable = false)
    String terminalExtraCapabilities;
    @Column(name = "transaction_currency", nullable = false)
    String transactionCurrency;
    @Column(name = "transaction_reference_currency", nullable = false)
    String transactionReferenceCurrency;
    @Column(name = "force_online", nullable = false)
    boolean forceOnline;
    @Column(name = "pos_data_code", nullable = false)
    String posDataCode;
    @Column(name = "icc_data", nullable = false)
    String iccData;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTmsEndpointId() {
        return tmsEndpointId;
    }

    public void setTmsEndpointId(long tmsEndpointId) {
        this.tmsEndpointId = tmsEndpointId;
    }

    public long getCtmkKeyId() {
        return ctmkKeyId;
    }

    public void setCtmkKeyId(long ctmkKeyId) {
        this.ctmkKeyId = ctmkKeyId;
    }

    public long getBdkKeyId() {
        return bdkKeyId;
    }

    public void setBdkKeyId(long bdkKeyId) {
        this.bdkKeyId = bdkKeyId;
    }

    public long getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(long acquirerId) {
        this.acquirerId = acquirerId;
    }

    public int getKeyDownloadTimeInMin() {
        return keyDownloadTimeInMin;
    }

    public void setKeyDownloadTimeInMin(int keyDownloadTimeInMin) {
        this.keyDownloadTimeInMin = keyDownloadTimeInMin;
    }

    public int getKeyDownloadIntervalInMin() {
        return keyDownloadIntervalInMin;
    }

    public void setKeyDownloadIntervalInMin(int keyDownloadIntervalInMin) {
        this.keyDownloadIntervalInMin = keyDownloadIntervalInMin;
    }

    public int getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(int terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalCapabilities() {
        return terminalCapabilities;
    }

    public void setTerminalCapabilities(String terminalCapabilities) {
        this.terminalCapabilities = terminalCapabilities;
    }

    public String getTerminalExtraCapabilities() {
        return terminalExtraCapabilities;
    }

    public void setTerminalExtraCapabilities(String terminalExtraCapabilities) {
        this.terminalExtraCapabilities = terminalExtraCapabilities;
    }

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public String getTransactionReferenceCurrency() {
        return transactionReferenceCurrency;
    }

    public void setTransactionReferenceCurrency(String transactionReferenceCurrency) {
        this.transactionReferenceCurrency = transactionReferenceCurrency;
    }

    public boolean isForceOnline() {
        return forceOnline;
    }

    public void setForceOnline(boolean forceOnline) {
        this.forceOnline = forceOnline;
    }

    public String getPosDataCode() {
        return posDataCode;
    }

    public void setPosDataCode(String posDataCode) {
        this.posDataCode = posDataCode;
    }

    public String getIccData() {
        return iccData;
    }

    public void setIccData(String iccData) {
        this.iccData = iccData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

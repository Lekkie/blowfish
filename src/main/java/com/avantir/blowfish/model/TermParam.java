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
public class TermParam extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long termParamId;
    @Column(name = "name", nullable = false, unique = true)
    String name;
    @Column(name = "tms_endpoint_id", nullable = false)
    long tmsEndpointId;
    @Column(name = "ctmk_key_id", nullable = false)
    long ctmkKeyId;
    @Column(name = "bdk_key_id", nullable = false)
    long bdkKeyId;
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
    @Column(name = "transaction_currency_exponent", nullable = false)
    int transactionCurrencyExponent;
    @Column(name = "reference_currency_exponent", nullable = false)
    int referenceCurrencyExponent;
    @Column(name = "reference_currency_conversion", nullable = false)
    int referenceCurrencyConversion;
    @Column(name = "force_online", nullable = false)
    boolean forceOnline;
    @Column(name = "pos_data_code", nullable = false)
    String posDataCode;
    @Column(name = "icc_data", nullable = false)
    String iccData;
    @Column(name = "support_default_tdol", nullable = false)
    boolean supportDefaultTDOL;
    @Column(name = "support_default_ddol", nullable = false)
    boolean supportDefaultDDOL;
    @Column(name = "support_pse_selection", nullable = false)
    boolean supportPSESelection;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;
    @Column(name = "use_local_network_config", nullable = false)
    boolean useLocalNetworkConfig;

    public Long getTermParamId() {
        return termParamId;
    }

    public void setTermParamId(Long termParamId) {
        this.termParamId = termParamId;
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

    public int getTransactionCurrencyExponent() {
        return transactionCurrencyExponent;
    }

    public void setTransactionCurrencyExponent(int transactionCurrencyExponent) {
        this.transactionCurrencyExponent = transactionCurrencyExponent;
    }

    public int getReferenceCurrencyExponent() {
        return referenceCurrencyExponent;
    }

    public void setReferenceCurrencyExponent(int referenceCurrencyExponent) {
        this.referenceCurrencyExponent = referenceCurrencyExponent;
    }

    public int getReferenceCurrencyConversion() {
        return referenceCurrencyConversion;
    }

    public void setReferenceCurrencyConversion(int referenceCurrencyConversion) {
        this.referenceCurrencyConversion = referenceCurrencyConversion;
    }

    public boolean isSupportDefaultTDOL() {
        return supportDefaultTDOL;
    }

    public void setSupportDefaultTDOL(boolean supportDefaultTDOL) {
        this.supportDefaultTDOL = supportDefaultTDOL;
    }

    public boolean isSupportDefaultDDOL() {
        return supportDefaultDDOL;
    }

    public void setSupportDefaultDDOL(boolean supportDefaultDDOL) {
        this.supportDefaultDDOL = supportDefaultDDOL;
    }

    public boolean isSupportPSESelection() {
        return supportPSESelection;
    }

    public void setSupportPSESelection(boolean supportPSESelection) {
        this.supportPSESelection = supportPSESelection;
    }

    public boolean isUseLocalNetworkConfig() {
        return useLocalNetworkConfig;
    }

    public void setUseLocalNetworkConfig(boolean useLocalNetworkConfig) {
        this.useLocalNetworkConfig = useLocalNetworkConfig;
    }
}

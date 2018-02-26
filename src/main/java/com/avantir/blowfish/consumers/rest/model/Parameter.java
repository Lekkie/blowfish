package com.avantir.blowfish.consumers.rest.model;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
public class Parameter {

    private String name;
    private String tmsIp;
    private int tmsPort;
    private int tmsTimeout;
    private boolean tmsSsl;
    private String ctmk;
    private String bdk;
    private String ctmkChkDigit;
    private String bdkChkDigit;
    private String acquirer;
    private String merchantName;
    private String terminalId;
    private int keyDownlTimeInMin;
    private int keyDownlIntervalInMin;
    private int termType;
    private String termCap;
    private String termExCap;
    private String transCurr;
    private String transRefCurr;
    private boolean forceOnline;
    private String posDataCode;
    private String iccData;
    private String desc;
    private int status;
    int transCurrExp; // https://en.wikipedia.org/wiki/ISO_4217#Treatment_of_minor_currency_units_(the_"exponent")
    int refCurrExp;
    int refCurrConv;
    boolean supportDefaultTDOL;
    boolean supportDefaultDDOL;
    boolean supportPSESelection;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTmsIp() {
        return tmsIp;
    }

    public void setTmsIp(String tmsIp) {
        this.tmsIp = tmsIp;
    }

    public int getTmsPort() {
        return tmsPort;
    }

    public void setTmsPort(int tmsPort) {
        this.tmsPort = tmsPort;
    }

    public int getTmsTimeout() {
        return tmsTimeout;
    }

    public void setTmsTimeout(int tmsTimeout) {
        this.tmsTimeout = tmsTimeout;
    }

    public boolean isTmsSsl() {
        return tmsSsl;
    }

    public void setTmsSsl(boolean tmsSsl) {
        this.tmsSsl = tmsSsl;
    }

    public String getCtmk() {
        return ctmk;
    }

    public void setCtmk(String ctmk) {
        this.ctmk = ctmk;
    }

    public String getBdk() {
        return bdk;
    }

    public void setBdk(String bdk) {
        this.bdk = bdk;
    }

    public String getAcquirer() {
        return acquirer;
    }

    public void setAcquirer(String acquirer) {
        this.acquirer = acquirer;
    }

    public int getKeyDownlTimeInMin() {
        return keyDownlTimeInMin;
    }

    public void setKeyDownlTimeInMin(int keyDownlTimeInMin) {
        this.keyDownlTimeInMin = keyDownlTimeInMin;
    }

    public int getKeyDownlIntervalInMin() {
        return keyDownlIntervalInMin;
    }

    public void setKeyDownlIntervalInMin(int keyDownlIntervalInMin) {
        this.keyDownlIntervalInMin = keyDownlIntervalInMin;
    }

    public int getTermType() {
        return termType;
    }

    public void setTermType(int termType) {
        this.termType = termType;
    }

    public String getTermCap() {
        return termCap;
    }

    public void setTermCap(String termCap) {
        this.termCap = termCap;
    }

    public String getTermExCap() {
        return termExCap;
    }

    public void setTermExCap(String termExCap) {
        this.termExCap = termExCap;
    }

    public String getTransCurr() {
        return transCurr;
    }

    public void setTransCurr(String transCurr) {
        this.transCurr = transCurr;
    }

    public String getTransRefCurr() {
        return transRefCurr;
    }

    public void setTransRefCurr(String transRefCurr) {
        this.transRefCurr = transRefCurr;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCtmkChkDigit() {
        return ctmkChkDigit;
    }

    public void setCtmkChkDigit(String ctmkChkDigit) {
        this.ctmkChkDigit = ctmkChkDigit;
    }

    public String getBdkChkDigit() {
        return bdkChkDigit;
    }

    public void setBdkChkDigit(String bdkChkDigit) {
        this.bdkChkDigit = bdkChkDigit;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public int getTransCurrExp() {
        return transCurrExp;
    }

    public void setTransCurrExp(int transCurrExp) {
        this.transCurrExp = transCurrExp;
    }

    public int getRefCurrExp() {
        return refCurrExp;
    }

    public void setRefCurrExp(int refCurrExp) {
        this.refCurrExp = refCurrExp;
    }

    public int getRefCurrConv() {
        return refCurrConv;
    }

    public void setRefCurrConv(int refCurrConv) {
        this.refCurrConv = refCurrConv;
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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}

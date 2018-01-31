package com.avantir.blowfish.consumers.rest.model;

/**
 * Created by lekanomotayo on 15/01/2018.
 */
public class BaseRequest {

    String merchatId;
    String terminalId;
    String tranType;
    String pan;
    String expiryDate;

    public String getMerchatId() {
        return merchatId;
    }

    public void setMerchatId(String merchatId) {
        this.merchatId = merchatId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

}

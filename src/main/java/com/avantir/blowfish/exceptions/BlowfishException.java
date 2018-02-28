package com.avantir.blowfish.exceptions;

/**
 * Created by lekanomotayo on 28/02/2018.
 */
public class BlowfishException extends Exception {

    String code;

    public BlowfishException(String code, String msg, Exception ex){
        super(msg, ex);
        this.code = code;
    }

    public BlowfishException(String code, String msg){
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

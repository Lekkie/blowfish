package com.avantir.blowfish.exceptions;

/**
 * Created by lekanomotayo on 28/02/2018.
 */

public class BlowfishRuntimeException extends RuntimeException {

    int code;

    public BlowfishRuntimeException(int code, String msg, Exception ex){
        super(msg, ex);
        this.code = code;
    }

    public BlowfishRuntimeException(int code, String msg){
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

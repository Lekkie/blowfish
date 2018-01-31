package com.avantir.blowfish.consumers.rest.model;

/**
 * Created by lekanomotayo on 15/01/2018.
 */
public class Error {

    String code;
    String message;

    public Error(){

    }

    public Error(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.avantir.blowfish.model;

/**
 * Created by lekanomotayo on 09/04/2018.
 */
public abstract class RestResponse implements BlowfishResponse{

    protected final String code;
    protected final String message;

    public RestResponse(String code, String message){
        this.code = code;
        this.message = message;
    }


    // get error object

    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

}

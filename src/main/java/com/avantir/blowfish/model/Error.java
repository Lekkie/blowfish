package com.avantir.blowfish.model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by lekanomotayo on 15/01/2018.
 */
public class Error {

    int code;
    String message;

    public Error(){

    }

    public Error(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        return new JSONObject(this).toString();
    }

}

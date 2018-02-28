package com.avantir.blowfish.exceptions;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class InvalidExpiryDateException extends BlowfishException {

    public InvalidExpiryDateException(String msg){
        super("4102", msg);
    }
}

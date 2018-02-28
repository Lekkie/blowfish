package com.avantir.blowfish.exceptions;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class InvalidPanException extends BlowfishException {

    public InvalidPanException(String msg){
        super("4101", msg);
    }
}

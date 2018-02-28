package com.avantir.blowfish.exceptions;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class AcquirerNotSupportedException extends BlowfishException {

    public AcquirerNotSupportedException(String msg){
        super("1101", msg);
    }
}

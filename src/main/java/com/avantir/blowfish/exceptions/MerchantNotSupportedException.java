package com.avantir.blowfish.exceptions;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class MerchantNotSupportedException extends BlowfishException {

    public MerchantNotSupportedException(String msg){
        super("1201", msg);
    }
}

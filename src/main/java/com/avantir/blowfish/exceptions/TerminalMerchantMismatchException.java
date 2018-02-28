package com.avantir.blowfish.exceptions;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class TerminalMerchantMismatchException extends BlowfishException {

    public TerminalMerchantMismatchException(String msg){
        super("4103", msg);
    }
}

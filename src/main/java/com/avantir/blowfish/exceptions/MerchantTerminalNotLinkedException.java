package com.avantir.blowfish.exceptions;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class MerchantTerminalNotLinkedException extends BlowfishException {

    public MerchantTerminalNotLinkedException(String msg){
        super("3501", msg);
    }
}

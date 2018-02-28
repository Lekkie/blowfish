package com.avantir.blowfish.exceptions;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class MerchantTerminalTranTypeBinNotSupportedException extends BlowfishException {

    public MerchantTerminalTranTypeBinNotSupportedException(String msg){
        super("3601", msg);
    }
}

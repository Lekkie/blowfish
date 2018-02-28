package com.avantir.blowfish.exceptions;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class AcquirerMerchantTranTypeBinNotSupportedException extends BlowfishException {

    public AcquirerMerchantTranTypeBinNotSupportedException(String msg){
        super("3201", msg);
    }
}

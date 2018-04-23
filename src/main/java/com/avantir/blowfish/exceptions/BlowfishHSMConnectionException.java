package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 28/02/2018.
 */
public class BlowfishHSMConnectionException extends BlowfishRuntimeException {

    public BlowfishHSMConnectionException(String msg){
        super(ErrorType.HSM_CONNECTION.getCode(), "HSM Connection error: " + msg);
    }

}

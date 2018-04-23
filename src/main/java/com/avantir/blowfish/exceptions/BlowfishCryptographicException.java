package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 28/02/2018.
 */
public class BlowfishCryptographicException extends BlowfishRuntimeException {

    public BlowfishCryptographicException(String entity){
        super(ErrorType.CRYPTOGRAPHIC_ERROR.getCode(), "A cryptographic error occurred: " + entity);
    }

}

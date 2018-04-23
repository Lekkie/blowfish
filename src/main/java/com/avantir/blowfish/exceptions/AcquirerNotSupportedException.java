package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class AcquirerNotSupportedException extends BlowfishRuntimeException {

    public AcquirerNotSupportedException(String msg){
        super(ErrorType.ENTITY_NOT_FOUND.getCode(), msg);
    }
}

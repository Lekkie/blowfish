package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 28/02/2018.
 */
public class BlowfishOperationNotSupportedException extends BlowfishRuntimeException {

    public BlowfishOperationNotSupportedException(String entity){
        super(ErrorType.OPERATION_NOT_SUPPORTED.getCode(), entity + " Operation not supported");
    }

}

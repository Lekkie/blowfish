package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 28/02/2018.
 */
public class BlowfishIllegalStateException extends BlowfishRuntimeException {

    public BlowfishIllegalStateException(String entity){
        super(ErrorType.ILLEGAL_STATE.getCode(), "Invalid/Illegal state (" + entity + ")");
    }

}

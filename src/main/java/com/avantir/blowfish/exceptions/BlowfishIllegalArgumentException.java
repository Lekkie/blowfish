package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 28/02/2018.
 */
public class BlowfishIllegalArgumentException extends BlowfishRuntimeException {

    public BlowfishIllegalArgumentException(String entity){
        super(ErrorType.ILLEGAL_ARGUMENT.getCode(), "Invalid/Illegal value supplied (" + entity + "). Check that " + entity + " is not empty or null");
    }

}

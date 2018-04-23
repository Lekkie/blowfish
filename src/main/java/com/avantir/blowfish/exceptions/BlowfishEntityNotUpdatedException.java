package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 28/02/2018.
 */
public class BlowfishEntityNotUpdatedException extends BlowfishRuntimeException {

    public BlowfishEntityNotUpdatedException(String entity){
        super(ErrorType.ENTITY_NOT_UPDATED.getCode(), entity + " Not Updated");
    }

}

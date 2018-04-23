package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 28/02/2018.
 */
public class BlowfishEntityNotDeletedException extends BlowfishRuntimeException {

    public BlowfishEntityNotDeletedException(String entity){
        super(ErrorType.ENTITY_NOT_DELETED.getCode(), entity + " Not Deleted");
    }

}

package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 28/02/2018.
 */
public class BlowfishEntityNotCreatedException extends BlowfishRuntimeException {

    public BlowfishEntityNotCreatedException(String entity){
        super(ErrorType.ENTITY_NOT_CREATED.getCode(), entity + " Not Created");
    }

}

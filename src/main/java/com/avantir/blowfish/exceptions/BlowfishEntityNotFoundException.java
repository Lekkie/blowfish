package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 28/02/2018.
 */
public class BlowfishEntityNotFoundException extends BlowfishRuntimeException {

    public BlowfishEntityNotFoundException(String entity){
        super(ErrorType.ENTITY_NOT_FOUND.getCode(), entity + " Not Found");
    }

}

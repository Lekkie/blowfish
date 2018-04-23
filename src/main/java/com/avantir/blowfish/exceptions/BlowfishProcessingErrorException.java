package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 28/02/2018.
 */
public class BlowfishProcessingErrorException extends BlowfishRuntimeException {

    public BlowfishProcessingErrorException(String entity){
        super(ErrorType.PROCESSING_ERROR.getCode(), "An unknown error occurred while processing " + entity);
    }

}

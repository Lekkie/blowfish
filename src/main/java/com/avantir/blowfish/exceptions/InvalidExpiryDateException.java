package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class InvalidExpiryDateException extends BlowfishRuntimeException {

    public InvalidExpiryDateException(String msg){
        super(ErrorType.ILLEGAL_ARGUMENT.getCode(), msg);
    }

    public InvalidExpiryDateException(int code, String msg){
        super(code, msg);
    }
}

package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class InvalidPanException extends BlowfishRuntimeException {

    public InvalidPanException(String msg){
        super(ErrorType.ILLEGAL_ARGUMENT.getCode(), msg);
    }

    public InvalidPanException(int code, String msg){
        super(code, msg);
    }
}

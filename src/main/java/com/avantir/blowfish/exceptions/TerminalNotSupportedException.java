package com.avantir.blowfish.exceptions;

import com.avantir.blowfish.utils.ErrorType;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class TerminalNotSupportedException extends BlowfishRuntimeException {

    public TerminalNotSupportedException(String msg){
        super(ErrorType.ENTITY_NOT_FOUND.getCode(), msg);
    }

    public TerminalNotSupportedException(int code, String msg){
        super(code, msg);
    }
}

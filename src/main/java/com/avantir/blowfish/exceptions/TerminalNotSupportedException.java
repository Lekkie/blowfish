package com.avantir.blowfish.exceptions;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
public class TerminalNotSupportedException extends BlowfishException {

    public TerminalNotSupportedException(String msg){
        super("1301", msg);
    }
}

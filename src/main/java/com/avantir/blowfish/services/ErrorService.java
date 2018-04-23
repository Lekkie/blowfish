package com.avantir.blowfish.services;

import com.avantir.blowfish.exceptions.BlowfishException;
import com.avantir.blowfish.exceptions.BlowfishRuntimeException;
import com.avantir.blowfish.model.Error;
import com.avantir.blowfish.model.Errors;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lekanomotayo on 27/03/2018.
 */
@Service
public class ErrorService {


    @Deprecated
    public Errors getError(String code, String msg){
        Errors errors = new Errors();
        errors.getErrors().add(new Error(Integer.parseInt(code), msg));
        return errors;
    }

    public Errors getError(int code, String msg){
        Errors errors = new Errors();
        errors.getErrors().add(new Error(code, msg));
        return errors;
    }

    public Errors getError(BlowfishRuntimeException bex){
        Errors errors = new Errors();
        errors.getErrors().add(new Error(bex.getCode(), bex.getMessage()));
        return errors;
    }

    public Errors addError(Errors errors, BlowfishRuntimeException bex){
        errors.getErrors().add(new Error(bex.getCode(), bex.getMessage()));
        return errors;
    }
}

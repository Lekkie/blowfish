package com.avantir.blowfish.model;

import com.avantir.blowfish.exceptions.BlowfishException;
import com.avantir.blowfish.exceptions.BlowfishRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lekanomotayo on 15/01/2018.
 */
public class Errors {

    List<Error> errors = new ArrayList<>();

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Override
    public String toString(){
        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();
        for (Error error : errors)
            jArray.put(new JSONObject(error));
        jObject.put("errors", jArray);
        return jObject.toString();
    }
}

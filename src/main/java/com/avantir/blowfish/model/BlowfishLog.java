package com.avantir.blowfish.model;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by lekanomotayo on 19/02/2018.
 */
public class BlowfishLog {

    private static final Logger logger = LoggerFactory.getLogger(BlowfishLog.class);

    private Exception exception;
    private String functionParameters = "";
    private String data = "";

    public BlowfishLog(String functionParameters, String data){
        this.functionParameters = functionParameters;
        this.data = data;
    }

    public BlowfishLog(String functionParameters, Exception exception){
        this.functionParameters = functionParameters;
        this.exception = exception;
    }


    public String toString(){
        // Mask any sensitive data

        StringWriter ex = new StringWriter();
        //StringWriter th = new StringWriter();
        if(exception != null){
            exception.printStackTrace(new PrintWriter(ex));
            //exception.getCause().printStackTrace(new PrintWriter(th));
        }

        JSONObject json = new JSONObject();
        json.put("functionParameters", functionParameters);
        json.put("data", data);
        json.put("exception", ex);

        return json.toString();
    }


    /*
    public static void main(String[] args){
        try{
            String fxnParams = "id=test\"testing\" tesfkaks" + ",HttpServletResponse=response.tostring()";
            BlowfishLog log = new BlowfishLog(fxnParams, "Merchant created");
            logger.debug(log.toString());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    */



}

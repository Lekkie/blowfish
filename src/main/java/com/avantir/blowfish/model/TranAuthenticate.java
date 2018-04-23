package com.avantir.blowfish.model;

import com.avantir.blowfish.messaging.Message;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.RequestValidationService;
import com.avantir.blowfish.services.SpringContextService;

/**
 * Created by lekanomotayo on 15/01/2018.
 */
public class TranAuthenticate extends Message{

    private final static RequestValidationService requestValidationService = (RequestValidationService) SpringContextService.getApplicationContext().getBean("requestValidationService");
    private final static IsoMessageService isoMessageService = (IsoMessageService) SpringContextService.getApplicationContext().getBean("isoMessageService");


    public void validate(){

    }

}

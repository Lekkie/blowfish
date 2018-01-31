package com.avantir.blowfish.processor.trans;

import com.avantir.blowfish.consumers.rest.model.TranAuthenticate;
import com.avantir.blowfish.consumers.rest.model.TranResponse;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.messaging.Exchange;
import com.avantir.blowfish.messaging.Message;
import com.avantir.blowfish.utils.IsoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by lekanomotayo on 15/01/2018.
 */
@Component
public class TranAuthenticateProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TranAuthenticateProcessor.class);

    public TranResponse process(Exchange requestExchange){

        Message message = requestExchange.getMessage();
        TranAuthenticate tranAuthenticate = (TranAuthenticate) message;
        TranResponse tranResponse = new TranResponse();
        return tranResponse;
    }

}

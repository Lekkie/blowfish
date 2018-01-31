package com.avantir.blowfish.processor.trans;

import com.avantir.blowfish.consumers.rest.model.TranAuthenticate;
import com.avantir.blowfish.consumers.rest.model.TranResponse;
import com.avantir.blowfish.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by lekanomotayo on 15/01/2018.
 */
@Component
public class TranNotificationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TranNotificationProcessor.class);

    public TranResponse process(Message message){

        TranAuthenticate tranAuthenticate = (TranAuthenticate) message;
        TranResponse tranResponse = new TranResponse();
        return tranResponse;
    }

}

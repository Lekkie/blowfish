package com.avantir.blowfish.processor.factory;

import com.avantir.blowfish.model.BlowfishRequest;
import com.avantir.blowfish.model.IsoRequest;
import com.avantir.blowfish.model.TranAuthenticate;
import com.avantir.blowfish.model.TranNotification;
import com.avantir.blowfish.processor.iso8583.IsoRequestProcessor;
import com.avantir.blowfish.processor.request.RequestProcessor;
import com.avantir.blowfish.processor.protocol.ProtocolProcessor;
import com.avantir.blowfish.processor.rest.TranAuthenticateProcessor;
import com.avantir.blowfish.processor.rest.TranNotificationProcessor;
import com.avantir.blowfish.services.SpringContextService;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

/**
 * Created by lekanomotayo on 09/04/2018.
 */

public class RequestProcessorFactory extends AbstractFactory {

    ApplicationContext context = SpringContextService.getApplicationContext();

    @Override
    public Optional<RequestProcessor> getTransactionProcessor(BlowfishRequest blowfishRequest){

        if(blowfishRequest instanceof TranNotification) {
            TranNotificationProcessor tranNotificationProcessor = (TranNotificationProcessor) context.getBean("tranNotificationProcessor");
            return Optional.ofNullable(tranNotificationProcessor);
        }
        else if(blowfishRequest instanceof TranAuthenticate) {
            TranAuthenticateProcessor tranAuthenticateProcessor = (TranAuthenticateProcessor) context.getBean("tranAuthenticateProcessor");
            return Optional.ofNullable(tranAuthenticateProcessor);
        }
        else if(blowfishRequest instanceof IsoRequest) {
            IsoRequestProcessor isoRequestProcessor = (IsoRequestProcessor) context.getBean("isoRequestProcessor");
            return Optional.ofNullable(isoRequestProcessor);
        }

        return Optional.empty();
    }


    @Override
    public Optional<ProtocolProcessor> getProtocolProcessor(String protocol){
        return Optional.empty();
    }
}

package com.avantir.blowfish.processor.factory;

import com.avantir.blowfish.model.BlowfishRequest;
import com.avantir.blowfish.processor.protocol.ProtocolProcessor;
import com.avantir.blowfish.processor.request.RequestProcessor;

import java.util.Optional;

/**
 * Created by lekanomotayo on 09/04/2018.
 */
public class IsoNodeProcessorFactory extends AbstractFactory {

    @Override
    public Optional<RequestProcessor> getTransactionProcessor(BlowfishRequest blowfishRequest){

        return Optional.empty();
    }

    public Optional<ProtocolProcessor> getProtocolProcessor(String protocol){

        if("REST".equalsIgnoreCase(protocol))
            return Optional.empty();

        if("ISO".equalsIgnoreCase(protocol))
            return Optional.empty();

        return Optional.empty();
    }
}

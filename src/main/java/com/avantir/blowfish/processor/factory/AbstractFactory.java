package com.avantir.blowfish.processor.factory;

import com.avantir.blowfish.model.BlowfishRequest;
import com.avantir.blowfish.processor.request.RequestProcessor;
import com.avantir.blowfish.processor.protocol.ProtocolProcessor;

import java.util.Optional;

/**
 * Created by lekanomotayo on 09/04/2018.
 */
public abstract class AbstractFactory {

    public abstract Optional<RequestProcessor> getTransactionProcessor(BlowfishRequest blowfishRequest);
    public abstract Optional<ProtocolProcessor> getProtocolProcessor(String protocol);


}

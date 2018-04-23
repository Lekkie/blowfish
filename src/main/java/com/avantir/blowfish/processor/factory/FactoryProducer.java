package com.avantir.blowfish.processor.factory;

import java.util.Optional;

/**
 * Created by lekanomotayo on 09/04/2018.
 */
public class FactoryProducer {

    public static Optional<AbstractFactory> getFactory(String choice){

        if("TRANSACTION_PROCESSOR".equalsIgnoreCase(choice)){
            return Optional.of(new RequestProcessorFactory());
        } else if("PROTOCOL".equalsIgnoreCase(choice)){
            return Optional.of(new ProtocolProcessorFactory());
        }

        return Optional.empty();
    }
}

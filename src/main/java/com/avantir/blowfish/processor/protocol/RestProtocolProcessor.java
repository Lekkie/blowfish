package com.avantir.blowfish.processor.protocol;

import com.avantir.blowfish.exceptions.BlowfishProcessingErrorException;
import com.avantir.blowfish.exceptions.TranTypeNotSupportedException;
import com.avantir.blowfish.model.*;
import com.avantir.blowfish.processor.factory.AbstractFactory;
import com.avantir.blowfish.processor.factory.FactoryProducer;
import com.avantir.blowfish.processor.request.RequestProcessor;
import com.avantir.blowfish.processor.rest.TranAuthenticateProcessor;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by lekanomotayo on 09/04/2018.
 */
@Component
public class RestProtocolProcessor implements ProtocolProcessor {

    @Autowired
    TranAuthenticateProcessor tranAuthenticateProcessor;
    @Autowired
    TransactionService transactionService;
    @Autowired
    IsoMessageService isoMessageService;

    public Optional<BlowfishResponse> processRequest(BlowfishRequest blowfishRequest){
        return processRest(blowfishRequest);
    }

    public Optional<BlowfishResponse> processRest(BlowfishRequest blowfishRequest){
        Optional<AbstractFactory> optionalAbstractFactory = FactoryProducer.getFactory("TRANSACTION_PROCESSOR");
        AbstractFactory abstractFactory = optionalAbstractFactory.orElseThrow(() -> new BlowfishProcessingErrorException("TransactionProcessorFactory"));
        Optional<RequestProcessor> optionalTransactionProcessor = abstractFactory.getTransactionProcessor(blowfishRequest);
        RequestProcessor transactionProcessor = optionalTransactionProcessor.orElseThrow(() -> new TranTypeNotSupportedException("Transaction not supported"));
        return transactionProcessor.process(blowfishRequest);
    }

    public void processResponse(BlowfishResponse blowfishResponse){

    }

}

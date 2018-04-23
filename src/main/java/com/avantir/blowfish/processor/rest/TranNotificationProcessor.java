package com.avantir.blowfish.processor.rest;

import com.avantir.blowfish.entity.Key;
import com.avantir.blowfish.entity.Transaction;
import com.avantir.blowfish.exceptions.BlowfishEntityNotCreatedException;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.model.*;
import com.avantir.blowfish.processor.request.RequestProcessor;
import com.avantir.blowfish.services.KeyService;
import com.avantir.blowfish.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by lekanomotayo on 15/01/2018.
 */
@Component
public class TranNotificationProcessor implements RequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TranNotificationProcessor.class);

    @Autowired
    TransactionService transactionService;
    @Autowired
    KeyService keyService;

    public Optional<BlowfishResponse> process(BlowfishRequest blowfishRequest){
        TranNotification tranNotification = (TranNotification) blowfishRequest;
        Transaction transaction = transactionService.copyMessage(tranNotification);
        Optional<Transaction> optionalTrans = transactionService.create(transaction);
        transaction = optionalTrans.orElseThrow(() -> new BlowfishEntityNotCreatedException("Transaction"));
        return Optional.ofNullable(new TranResponse("00", "Successful"));
    }

}

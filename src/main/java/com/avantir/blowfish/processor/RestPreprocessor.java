package com.avantir.blowfish.processor;

import com.avantir.blowfish.consumers.rest.model.TranAuthenticate;
import com.avantir.blowfish.consumers.rest.model.TranNotification;
import com.avantir.blowfish.exceptions.*;
import com.avantir.blowfish.messaging.Exchange;
import com.avantir.blowfish.messaging.Message;
import com.avantir.blowfish.model.Key;
import com.avantir.blowfish.model.Transaction;
import com.avantir.blowfish.processor.trans.TranAuthenticateProcessor;
import com.avantir.blowfish.processor.trans.TranNotificationProcessor;
import com.avantir.blowfish.services.TransactionService;
import com.avantir.blowfish.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lekanomotayo on 13/01/2018.
 */

@Component
public class RestPreprocessor extends APreprocessor {


    @Autowired
    TranAuthenticateProcessor tranAuthenticateProcessor;
    @Autowired
    TranNotificationProcessor tranNotificationProcessor;

    @Autowired
    TransactionService transactionService;

    // forward to transaction processor request handler
    // if exception is thrown, u r responsbile for sending response
    public Object processRest(Exchange requestExchange) throws Exception{

        Message message = requestExchange.getMessage();
        // confirm that transaction is allowed
        String pan = message.getF2();
        String tranTypeCode = MessageUtil.getTranType(message.getF3());
        String expDate = message.getF14();
        String tid = message.getF41();
        String mid = message.getF42();

        validateBase(mid, tid, tranTypeCode, pan, expDate);

        Key keyVersion = null;
        Transaction transaction = MessageUtil.copyMessage(message, keyVersion.getKeyId(), keyVersion.getSalt());
        requestExchange.setTransaction(transaction);

        // get processor
        if(message instanceof TranAuthenticate)
            return tranAuthenticateProcessor.process(requestExchange);

        if(message instanceof TranNotification)
            return tranNotificationProcessor.process(message);

        throw new TranTypeNotSupportedException("Transaction not supported");
    }



    public void processIso8583(Exchange requestExchange){

    }





}

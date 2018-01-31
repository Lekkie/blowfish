package com.avantir.blowfish.processor;

import com.avantir.blowfish.messaging.Exchange;

/**
 * Created by lekanomotayo on 11/01/2018.
 */
public interface IProcessor {

    public void processIso8583(Exchange requestExchange);
    public Object processRest(Exchange requestExchange)throws Exception;
}

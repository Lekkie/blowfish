package com.avantir.blowfish.messaging.endpoint;

import com.avantir.blowfish.messaging.Exchange;
import com.avantir.blowfish.messaging.isobridge.Interchange;
import com.avantir.blowfish.messaging.model.ISOBridge;
import com.avantir.blowfish.messaging.model.Node;
import com.avantir.blowfish.messaging.model.SAPEndpoint;
import com.avantir.blowfish.processor.IProcessor;
import com.avantir.blowfish.services.TMSKeyOrigDataElemService;
import com.avantir.blowfish.utils.SpringContext;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Created by lekanomotayo on 01/01/2018.
 */
public abstract class ISO8583Node {

    protected static final Logger logger = LoggerFactory.getLogger(ISO8583Node.class);

    protected IProcessor isoProcessor;
    protected Interchange interchange;
    protected SAPEndpoint sapEndpoint;
    protected Node node;
    protected ISOBridge isoBridge;
    protected IConnection connection;
    protected TMSKeyOrigDataElemService tmsKeyOrigDataElemService;

    protected static ApplicationContext context = SpringContext.getApplicationContext();

    protected void receiveMessage(ChannelHandlerContext ctx, IsoMessage isoMessage){
        Exchange exchange = new Exchange(isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap(), isoMessage, true, node, this, ctx);
        isoProcessor.processIso8583(exchange);
    }

    protected void sendMessage(IConnection connection, IsoMessage isoMessage) throws Exception{
        connection.send(isoMessage);
    }

    protected void sendReply(ChannelHandlerContext ctx, IsoMessage isoMessage){
        ctx.writeAndFlush(isoMessage);
    }


    public IConnection getConnection() {
        return connection;
    }

    public ISOBridge getIsoBridge() {
        return isoBridge;
    }


}

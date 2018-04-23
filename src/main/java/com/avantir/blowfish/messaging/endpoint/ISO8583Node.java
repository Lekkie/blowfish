package com.avantir.blowfish.messaging.endpoint;

import com.avantir.blowfish.messaging.isobridge.Interchange;
import com.avantir.blowfish.messaging.entity.ISOBridge;
import com.avantir.blowfish.messaging.entity.Node;
import com.avantir.blowfish.messaging.entity.SAPEndpoint;
import com.avantir.blowfish.processor.protocol.IsoProtocolProcessor;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.TMSKeyOrigDataElemService;
import com.avantir.blowfish.services.SpringContextService;
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

    protected final IsoProtocolProcessor isoProcessor;
    protected final Interchange interchange;
    protected final SAPEndpoint sapEndpoint;
    protected final Node node;
    protected final ISOBridge isoBridge;
    protected final IConnection connection;
    protected final TMSKeyOrigDataElemService tmsKeyOrigDataElemService;
    protected final IsoMessageService isoMessageService;

    protected ISO8583Node(IsoProtocolProcessor isoProcessor, Interchange interchange, SAPEndpoint sapEndpoint,
                          Node node, ISOBridge isoBridge, IConnection connection,
                          TMSKeyOrigDataElemService tmsKeyOrigDataElemService, IsoMessageService isoMessageService){
        this.isoProcessor = isoProcessor;
        this.interchange = interchange;
        this.sapEndpoint = sapEndpoint;
        this.node = node;
        this.isoBridge = isoBridge;
        this.connection= connection;
        this.tmsKeyOrigDataElemService = tmsKeyOrigDataElemService;
        this.isoMessageService = isoMessageService;
    }

    protected static ApplicationContext context = SpringContextService.getApplicationContext();

    /*
    protected void receiveMessage(ChannelHandlerContext ctx, IsoMessage isoMessage){
        IsoMsg isoMsg = new IsoMsg(isoMessage, this, ctx, isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap());
        try{
            isoMsg.validate(); // try and handle valdiate error
        }
        catch(BlowfishRuntimeException brex){

        }
        isoProcessor.process(isoMsg);
    }
    */

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

package com.avantir.blowfish.clients.iso8583;

import com.avantir.blowfish.exceptions.BlowfishRuntimeException;
import com.avantir.blowfish.messaging.endpoint.IConnection;
import com.avantir.blowfish.messaging.endpoint.ISO8583Node;
import com.avantir.blowfish.messaging.isobridge.AInterchange;
import com.avantir.blowfish.messaging.entity.ISOBridge;
import com.avantir.blowfish.messaging.entity.Node;
import com.avantir.blowfish.messaging.entity.SAPEndpoint;
import com.avantir.blowfish.model.IsoRequest;
import com.avantir.blowfish.model.IsoResponse;
import com.avantir.blowfish.processor.protocol.IsoProtocolProcessor;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.TMSKeyOrigDataElemService;
import com.avantir.blowfish.services.StringService;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by lekanomotayo on 01/01/2018.
 */
public class ISO8583SinkNode extends ISO8583Node {

    public ISO8583SinkNode(SAPEndpoint sapEndpoint, Node node, ISOBridge isoBridge, IConnection connection) throws Exception{

        super((IsoProtocolProcessor) context.getBean("iso8583ProtocolProcessor"),
                (AInterchange)context.getBean(((StringService) context.getBean("stringService")).lowerCaseFirst(isoBridge.getClassName())),
                sapEndpoint, node, isoBridge, connection,
                (TMSKeyOrigDataElemService)context.getBean("tmsKeyOrigDataElemService"),
                (IsoMessageService) context.getBean("isoMessageService"));
        if(connection.isServer())
            throw new Exception("Sink node does not support Server connection at the moment");

        /*
        this.isoProcessor = (IsoPostprocessor)context.getBean("isoPostprocessor");
        this.sapEndpoint = sapEndpoint;
        this.node = node;
        this.isoBridge = isoBridge;
        this.connection = connection;
        StringService stringService = (StringService) context.getBean("stringService");
        String className = stringService.lowerCaseFirst(isoBridge.getClassName());
        interchange = (AInterchange)context.getBean(className);
        tmsKeyOrigDataElemService = (TMSKeyOrigDataElemService)context.getBean("tmsKeyOrigDataElemService");
        isoMessageService = (IsoMessageService) context.getBean("isoMessageService");
        */
    }

    //Ideally, only request shld go thru here...
    // send request to remote = receive request from TM
    public void sendRequestToRemote(IConnection connection, IsoMessage isoMessage)throws Exception{
        IsoMessage messageToRemote = interchange.requestFromTranMgr(isoMessage); //call an interface bridge
        super.sendMessage(connection, messageToRemote);
    }

    //Ideally, only response shld go thru here...
    // receive response from remote = send response to TM
    public void receiveResponseFromRemote(ChannelHandlerContext ctx, IsoMessage isoMessage){
        IsoMessage messageToTranMgr = interchange.responseFromRemote(isoMessage); // call an interface bridge

        Long tmsKey = isoMessageService.getTMSKey(isoMessage, tmsKeyOrigDataElemService);
        if(tmsKey < 1) {
            logger.error("receiveResponseFromRemote: TMS key not found for this message");
            return;
        }
        isoMessageService.setCompositeChildValue(isoMessage, isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap(), IsoMessageService.f127, IsoMessageService.f2, String.valueOf(tmsKey));
        //super.receiveMessage(ctx, messageToTranMgr); // receive response and forward to TM

        IsoResponse isoResponse = new IsoResponse(isoMessage, this);
        try{
            isoResponse.validate(); // try and handle valdiate error
            isoProcessor.processResponse(isoResponse);
        }
        catch(BlowfishRuntimeException brex){

        }
    }
}

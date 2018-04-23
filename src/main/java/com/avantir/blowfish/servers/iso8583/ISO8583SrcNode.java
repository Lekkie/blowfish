package com.avantir.blowfish.servers.iso8583;

import com.avantir.blowfish.exceptions.BlowfishIllegalStateException;
import com.avantir.blowfish.exceptions.BlowfishRuntimeException;
import com.avantir.blowfish.messaging.endpoint.IConnection;
import com.avantir.blowfish.messaging.endpoint.ISO8583Node;
import com.avantir.blowfish.messaging.isobridge.AInterchange;
import com.avantir.blowfish.messaging.entity.ISOBridge;
import com.avantir.blowfish.messaging.entity.Node;
import com.avantir.blowfish.messaging.entity.SAPEndpoint;
import com.avantir.blowfish.model.IsoRequest;
import com.avantir.blowfish.processor.protocol.IsoProtocolProcessor;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.TMSKeyOrigDataElemService;
import com.avantir.blowfish.services.StringService;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

public final class ISO8583SrcNode extends ISO8583Node {

    public ISO8583SrcNode(SAPEndpoint sapEndpoint, Node node, ISOBridge isoBridge, IConnection connection){

        super((IsoProtocolProcessor) context.getBean("iso8583ProtocolProcessor"),
                (AInterchange)context.getBean(((StringService) context.getBean("stringService")).lowerCaseFirst(isoBridge.getClassName())),
                sapEndpoint, node, isoBridge, connection,
                (TMSKeyOrigDataElemService)context.getBean("tmsKeyOrigDataElemService"),
                (IsoMessageService) context.getBean("isoMessageService"));

        assert isoProcessor != null;
        assert this.sapEndpoint != null;
        assert this.node != null;
        assert this.isoBridge != null;
        assert this.connection != null;
        assert this.interchange != null;
        assert this.tmsKeyOrigDataElemService != null;
        assert this.isoMessageService != null;
        if(!connection.isServer())
            throw new BlowfishIllegalStateException("Src node does not support Client connection at the moment");


        //isoProcessor = (IsoPreprocessor)context.getBean("isoPreprocessor");
        //this.sapEndpoint = sapEndpoint;
        //this.node=node;
        //this.isoBridge = isoBridge;
        //this.connection = connection;
        //StringService stringService = (StringService) context.getBean("stringService");
        //String className = stringService.lowerCaseFirst(isoBridge.getClassName());
        //interchange = (AInterchange)context.getBean(((StringService) context.getBean("stringService")).lowerCaseFirst(isoBridge.getClassName()));
        //tmsKeyOrigDataElemService = (TMSKeyOrigDataElemService)context.getBean("tmsKeyOrigDataElemService");
        //isoMessageService = (IsoMessageService) context.getBean("isoMessageService");
    }


    //  Ideally, only request shld go thru here...
    // send receive request to remote = send request to TM
    public void receiveRequestFromRemote(ChannelHandlerContext ctx, IsoMessage isoMessage){

        IsoMessage messageToTranMgr = interchange.requestFromRemote(isoMessage); //call an interface bridge

        Long tmsKey = isoMessageService.generateTMSKey(isoMessage, tmsKeyOrigDataElemService);
        if(tmsKey == -1){
            isoMessageService.sendResponse(this, ctx, isoMessage, isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap(), IsoMessageService.RESP_94);
            return;
        }
        if(tmsKey < 1){
            logger.error("receiveRequestFromRemote: TMS key cannot be less than 1");
            isoMessageService.sendResponse(this, ctx, isoMessage, isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap(), IsoMessageService.RESP_96);
            return;
        }

        // Set TMS Key
        isoMessageService.setCompositeChildValue(isoMessage, isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap(), IsoMessageService.f127, IsoMessageService.f2, String.valueOf(tmsKey));


        //super.receiveMessage(ctx, messageToTranMgr);
        IsoRequest isoRequest = new IsoRequest(isoMessage, this, ctx, isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap());
        try{
            isoRequest.validate(); // try and handle valdiate error
            isoProcessor.processRequest(isoRequest);
        }
        catch(BlowfishRuntimeException brex){

        }
    }

    //Ideally, only response shld go thru here...
    // send response to remote = receive response from TM
    public void sendResponseToRemote(ChannelHandlerContext ctx, IsoMessage isoMessage){
        IsoMessage messageToRemote = interchange.responseFromTranMgr(isoMessage); //call an interface bridge
        super.sendReply(ctx, messageToRemote);
    }

}

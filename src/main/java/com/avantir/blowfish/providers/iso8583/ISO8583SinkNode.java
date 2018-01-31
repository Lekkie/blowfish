package com.avantir.blowfish.providers.iso8583;

import com.avantir.blowfish.messaging.endpoint.IConnection;
import com.avantir.blowfish.messaging.endpoint.ISO8583Node;
import com.avantir.blowfish.messaging.isobridge.AInterchange;
import com.avantir.blowfish.messaging.model.ISOBridge;
import com.avantir.blowfish.messaging.model.Node;
import com.avantir.blowfish.messaging.model.SAPEndpoint;
import com.avantir.blowfish.processor.IsoPostprocessor;
import com.avantir.blowfish.services.TMSKeyOrigDataElemService;
import com.avantir.blowfish.utils.IsoUtil;
import com.avantir.blowfish.utils.StringUtil;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by lekanomotayo on 01/01/2018.
 */
public class ISO8583SinkNode extends ISO8583Node {

    public ISO8583SinkNode(IsoPostprocessor isoPostprocessor, SAPEndpoint sapEndpoint, Node node, ISOBridge isoBridge, IConnection connection) throws Exception{

        if(connection.isServer())
            throw new Exception("Sink node does not support Server connection at the moment");

        this.isoProcessor = isoPostprocessor;
        this.sapEndpoint = sapEndpoint;
        this.node = node;
        this.isoBridge = isoBridge;
        this.connection = connection;
        String className = StringUtil.lowerCaseFirst(isoBridge.getClassName());
        interchange = (AInterchange)context.getBean(className);
        tmsKeyOrigDataElemService = (TMSKeyOrigDataElemService)context.getBean("tmsKeyOrigDataElemService");
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

        Long tmsKey = IsoUtil.getTMSKey(isoMessage, tmsKeyOrigDataElemService);
        if(tmsKey < 1) {
            logger.error("receiveResponseFromRemote: TMS key not found for this message");
            return;
        }
        IsoUtil.setCompositeChildValue(isoMessage, isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap(), IsoUtil.f127, IsoUtil.f2, String.valueOf(tmsKey));
        super.receiveMessage(ctx, messageToTranMgr); // receive response and forward to TM
    }
}

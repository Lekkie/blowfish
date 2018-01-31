package com.avantir.blowfish.consumers.iso8583;

import com.avantir.blowfish.messaging.endpoint.IConnection;
import com.avantir.blowfish.messaging.endpoint.ISO8583Node;
import com.avantir.blowfish.messaging.isobridge.AInterchange;
import com.avantir.blowfish.messaging.model.ISOBridge;
import com.avantir.blowfish.messaging.model.Node;
import com.avantir.blowfish.messaging.model.SAPEndpoint;
import com.avantir.blowfish.processor.IsoPreprocessor;
import com.avantir.blowfish.services.TMSKeyOrigDataElemService;
import com.avantir.blowfish.utils.IsoUtil;
import com.avantir.blowfish.utils.StringUtil;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

public class ISO8583SrcNode extends ISO8583Node {

    public ISO8583SrcNode(IsoPreprocessor isoPreprocessor, SAPEndpoint sapEndpoint, Node node, ISOBridge isoBridge, IConnection connection)throws Exception{

        if(!connection.isServer())
            throw new Exception("Src node does not support Client connection at the moment");

        this.isoProcessor = isoPreprocessor;
        this.sapEndpoint = sapEndpoint;
        this.node=node;
        this.isoBridge = isoBridge;
        this.connection = connection;
        String className = StringUtil.lowerCaseFirst(isoBridge.getClassName());
        interchange = (AInterchange)context.getBean(className);
        tmsKeyOrigDataElemService = (TMSKeyOrigDataElemService)context.getBean("tmsKeyOrigDataElemService");
    }


    //  Ideally, only request shld go thru here...
    // send receive request to remote = send request to TM
    public void receiveRequestFromRemote(ChannelHandlerContext ctx, IsoMessage isoMessage){

        IsoMessage messageToTranMgr = interchange.requestFromRemote(isoMessage); //call an interface bridge

        Long tmsKey = IsoUtil.generateTMSKey(isoMessage, tmsKeyOrigDataElemService);
        if(tmsKey == -1){
            IsoUtil.sendResponse(this, ctx, isoMessage, isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap(), IsoUtil.RESP_94);
            return;
        }
        if(tmsKey < 1){
            logger.error("receiveRequestFromRemote: TMS key cannot be less than 1");
            IsoUtil.sendResponse(this, ctx, isoMessage, isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap(), IsoUtil.RESP_96);
            return;
        }

        // Set TMS Key
        IsoUtil.setCompositeChildValue(isoMessage, isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap(), IsoUtil.f127, IsoUtil.f2, String.valueOf(tmsKey));
        super.receiveMessage(ctx, messageToTranMgr);
    }

    //Ideally, only response shld go thru here...
    // send response to remote = receive response from TM
    public void sendResponseToRemote(ChannelHandlerContext ctx, IsoMessage isoMessage){
        IsoMessage messageToRemote = interchange.responseFromTranMgr(isoMessage); //call an interface bridge
        super.sendReply(ctx, messageToRemote);
    }

}

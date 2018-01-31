package com.avantir.blowfish.processor;

import com.avantir.blowfish.consumers.iso8583.ISO8583SrcNode;
import com.avantir.blowfish.consumers.iso8583.SrcNodeInfo;
import com.avantir.blowfish.messaging.Exchange;
import com.avantir.blowfish.messaging.endpoint.EndpointStarter;
import com.avantir.blowfish.model.Transaction;
import com.avantir.blowfish.services.TransactionService;
import com.avantir.blowfish.utils.IsoUtil;
import com.avantir.blowfish.utils.MessageUtil;
import com.solab.iso8583.IsoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lekanomotayo on 13/01/2018.
 */

@Component
public class IsoPostprocessor extends APostprocessor {


    private static final Logger logger = LoggerFactory.getLogger(IsoPostprocessor.class);

    @Autowired
    TransactionService transactionService;

    public void processIso8583(Exchange responseExchange){

        /*
        String isoPackagerName = responseExchange.getIsoPackagerName();
        boolean binaryBitmap = responseExchange.isBinaryBitmap();
        Node fromNode = responseExchange.getFromNode();
        ISO8583SinkNode iso8583SinkNode = (ISO8583SinkNode) responseExchange.getIso8583Node();
        ChannelHandlerContext ctx = responseExchange.getCtx();
        */

        IsoMessage isoMessage = responseExchange.getIsoMessage();

        // forward to transaction processor response handler
        // this is response, route back. find src node, forward response
        returnResponseToSrcNode(isoMessage);
        return;
    }


    private void returnResponseToSrcNode(IsoMessage isoResp){

        String msgType = String.format("%04X", isoResp.getType());
        String stan = IsoUtil.getFieldValue(isoResp, IsoUtil.f11);
        String transmissionDateTime = IsoUtil.getFieldValue(isoResp, IsoUtil.f7);
        String acqInstId = IsoUtil.getFieldValue(isoResp, IsoUtil.f32);
        String forwardingInstId = IsoUtil.getFieldValue(isoResp, IsoUtil.f33);
        String termId = IsoUtil.getFieldValue(isoResp, IsoUtil.f41);
        String merchantId = IsoUtil.getFieldValue(isoResp, IsoUtil.f42);

        Transaction transaction = transactionService.findByOriginalDataElements(msgType, stan, transmissionDateTime, acqInstId, forwardingInstId, termId, merchantId);

        if(transaction == null){
            logger.error("returnResponseToSrcNode: Transaction request cannot be found in the database");
            return;
        }

        transaction = MessageUtil.copyIsoMessageResponse(isoResp, transaction);
        transactionService.create(transaction);

        String tmsKey = transaction.getTmsKey();
        SrcNodeInfo srcNodeInfo = EndpointStarter.getSrcNodeInfoTreeMap().get(tmsKey);
        if(srcNodeInfo == null){
            logger.error("returnResponseToSrcNode: Src Node Info for this message (TMS Key: " + tmsKey + " cannot be found");
            return;
        }
        //find src node, forward response
        ISO8583SrcNode iso8583SrcNode = EndpointStarter.getISO8583SrcNode(srcNodeInfo.getSrcNodeId());
        if(srcNodeInfo == null){
            logger.error("returnResponseToSrcNode: Src Node Connection for this message (TMS Key: " + tmsKey + " cannot be found");
            return;
        }

        iso8583SrcNode.sendResponseToRemote(srcNodeInfo.getCtx(), isoResp);
    }


    public Object processRest(Exchange requestExchange)throws Exception{
        return null;
    }





}

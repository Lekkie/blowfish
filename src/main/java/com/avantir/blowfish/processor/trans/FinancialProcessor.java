package com.avantir.blowfish.processor.trans;

import com.avantir.blowfish.consumers.iso8583.SrcNodeInfo;
import com.avantir.blowfish.messaging.Exchange;
import com.avantir.blowfish.messaging.endpoint.EndpointStarter;
import com.avantir.blowfish.messaging.endpoint.IConnection;
import com.avantir.blowfish.messaging.model.DefaultSinkNode;
import com.avantir.blowfish.messaging.model.Node;
import com.avantir.blowfish.messaging.services.DefaultSinkNodeService;
import com.avantir.blowfish.messaging.services.NodeService;
import com.avantir.blowfish.processor.Router;
import com.avantir.blowfish.providers.iso8583.ISO8583SinkNode;
import com.avantir.blowfish.services.TransactionService;
import com.avantir.blowfish.utils.IsoUtil;
import com.avantir.blowfish.utils.StringUtil;
import com.solab.iso8583.IsoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lekanomotayo on 13/01/2018.
 */
@Component
public class FinancialProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FinancialProcessor.class);

    @Autowired
    Router router;
    @Autowired
    NodeService nodeService;
    @Autowired
    DefaultSinkNodeService defaultSinkNodeService;
    @Autowired
    TransactionService transactionService;


    public void process(Exchange requestExchange) throws Exception{

        // Log transaction
        transactionService.create(requestExchange.getTransaction());

        // validate financial things

        forwardToIsoEndpoint(requestExchange);
    }

    private void forwardToIsoEndpoint(Exchange requestExchange) throws Exception{

        IsoMessage isoMessage = requestExchange.getIsoMessage();
        // route to next endpoint
        String receivingInst = IsoUtil.getFieldValue(isoMessage, IsoUtil.f100);

        Long sinkNodeId = 0L;
        if(StringUtil.isEmpty(receivingInst)) {
            //get NIBSS sink node
            DefaultSinkNode defaultSinkNode = defaultSinkNodeService.getDefaultSinkNode();
            sinkNodeId = defaultSinkNode == null ? 0L : defaultSinkNode.getSinkNodeId();
        }
        else{
            sinkNodeId = router.getRouteByReceivingInst(receivingInst);
        }

        if(sinkNodeId < 1) {
            logger.error("routeToSinkNode: Sink Node Id cannot be less than 1");
            throw new Exception();
        }

        Node sinkNode = nodeService.findByNodeId(sinkNodeId);
        if(sinkNode == null) {
            logger.error("routeToSinkNode: Sink Node cannot be null");
            throw new Exception("routeToSinkNode: Sink Node cannot be null");
        }

        if(sinkNode.getType() == IsoUtil.SRC_NODE){
            logger.error("routeToSinkNode: Node cannot be a SrcNode");
            throw new Exception("routeToSinkNode: Node cannot be a SrcNode");
        }

        ISO8583SinkNode iso8583SinkNode = EndpointStarter.getISO8583SinkNode(sinkNodeId);
        if(iso8583SinkNode == null){
            logger.error("routeToSinkNode: ISO8583 Sink Node cannot be null. This Sink node has no connection running");
            throw new Exception("routeToSinkNode: ISO8583 Sink Node cannot be null. This Sink node has no connection running");
        }

        IConnection connection = iso8583SinkNode.getConnection();
        if(connection == null){
            logger.error("routeToSinkNode: Sink Node Connection cannot be null. This Sink node has no connection running");
            throw new Exception("routeToSinkNode: Sink Node Connection cannot be null. This Sink node has no connection running");
        }

        if(connection.isServer()){
            logger.error("routeToSinkNode: Sink Node Connection cannot be a server, it has to be a client");
            throw new Exception("routeToSinkNode: Sink Node Connection cannot be a server, it has to be a client");
        }


        if(requestExchange.isIso8583Exchange()){
            SrcNodeInfo srcNodeInfo = new SrcNodeInfo();
            srcNodeInfo.setCtx(requestExchange.getCtx());
            srcNodeInfo.setSrcNodeId(requestExchange.getFromNode().getNodeId());
            String tmsKey = IsoUtil.getCompositeFieldValue(isoMessage, IsoUtil.f127, IsoUtil.f2);
            EndpointStarter.addSrcNodeInfo(tmsKey, srcNodeInfo);
        }

        // Forward request to Sink Node
        iso8583SinkNode.sendRequestToRemote(connection, isoMessage);
    }

    public void processResponse(){

    }
}

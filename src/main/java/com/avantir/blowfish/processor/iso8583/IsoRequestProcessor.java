package com.avantir.blowfish.processor.iso8583;

import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.servers.iso8583.SrcNodeInfo;
import com.avantir.blowfish.entity.Key;
import com.avantir.blowfish.entity.Transaction;
import com.avantir.blowfish.messaging.endpoint.EndpointStarter;
import com.avantir.blowfish.messaging.endpoint.IConnection;
import com.avantir.blowfish.messaging.entity.DefaultSinkNode;
import com.avantir.blowfish.messaging.entity.Node;
import com.avantir.blowfish.messaging.services.DefaultSinkNodeService;
import com.avantir.blowfish.messaging.services.NodeService;
import com.avantir.blowfish.model.*;
import com.avantir.blowfish.processor.Router;
import com.avantir.blowfish.processor.request.RequestProcessor;
import com.avantir.blowfish.clients.iso8583.ISO8583SinkNode;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.KeyService;
import com.avantir.blowfish.services.StringService;
import com.avantir.blowfish.services.TransactionService;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by lekanomotayo on 10/04/2018.
 */
public class IsoRequestProcessor implements RequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(IsoRequestProcessor.class);

    @Autowired
    KeyService keyService;
    @Autowired
    Router router;
    @Autowired
    NodeService nodeService;
    @Autowired
    DefaultSinkNodeService defaultSinkNodeService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    IsoMessageService isoMessageService;
    @Autowired
    StringService stringService;



    public Optional<BlowfishResponse> process(BlowfishRequest blowfishRequest){

        Collectors.toList();
        IsoRequest isoRequest = (IsoRequest) blowfishRequest;
        IsoMessage isoMessage = isoRequest.getIsoMessage();
        ChannelHandlerContext ctx = isoRequest.getChannelHandlerContext();

        try{
            Transaction transaction = transactionService.copyIsoMessageRequest(isoMessage);
            //TranType tranType = tranTypeService.findByCode(tranTypeCode);
            transactionService.create(transaction);

            // ALl 0200 except Bill Payment & Recharge
            String receivingInst = isoMessageService.getFieldValue(isoMessage, IsoMessageService.f100);

            Long sinkNodeId = 0L;
            if(stringService.isEmpty(receivingInst)) {
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

            if(sinkNode.getType() == IsoMessageService.SRC_NODE){
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

            SrcNodeInfo srcNodeInfo = new SrcNodeInfo();
            srcNodeInfo.setCtx(isoRequest.getChannelHandlerContext());
            //srcNodeInfo.setSrcNodeId(isoRequest.getFromNode().getNodeId());
            String tmsKey = isoMessageService.getCompositeFieldValue(isoMessage, IsoMessageService.f127, IsoMessageService.f2);
            EndpointStarter.addSrcNodeInfo(tmsKey, srcNodeInfo);

            // Forward request to Sink Node
            iso8583SinkNode.sendRequestToRemote(connection, isoMessage);

        }
        catch(Exception ex){
            //isoMessageService.sendResponse(isoRequest.getIso8583Node(), ctx, isoMessage, isoPackagerName, binaryBitmap, IsoMessageService.RESP_06);
        }


        return Optional.empty();
    }
}

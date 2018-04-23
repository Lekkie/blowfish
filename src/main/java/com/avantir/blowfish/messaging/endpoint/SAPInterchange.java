package com.avantir.blowfish.messaging.endpoint;

import com.avantir.blowfish.servers.iso8583.ISO8583SrcNode;
import com.avantir.blowfish.servers.iso8583.ServerConnection;
import com.avantir.blowfish.servers.iso8583.SrcNodeListener;
import com.avantir.blowfish.messaging.entity.ISOBridge;
import com.avantir.blowfish.messaging.entity.Node;
import com.avantir.blowfish.messaging.entity.SAPEndpoint;
import com.avantir.blowfish.messaging.entity.TCPEndpoint;
import com.avantir.blowfish.messaging.services.NodeService;
import com.avantir.blowfish.clients.iso8583.ClientConnection;
import com.avantir.blowfish.clients.iso8583.ISO8583SinkNode;
import com.avantir.blowfish.clients.iso8583.SinkNodeListener;
import com.avantir.blowfish.services.SpringContextService;

/**
 * Created by lekanomotayo on 04/01/2018.
 */
public class SAPInterchange {

    SAPEndpoint sapEndpoint;
    ISO8583SrcNode iso8583SrcNode;
    ISO8583SinkNode iso8583SinkNode;
    ISOBridge isoBridge;
    TCPEndpoint tcpEndpoint;
    IConnection connection;


    public SAPInterchange(SAPEndpoint sapEndpoint, TCPEndpoint tcpEndpoint, ISOBridge isoBridge){
        this.sapEndpoint = sapEndpoint;
        this.tcpEndpoint = tcpEndpoint;
        this.isoBridge = isoBridge;
    }

    public void start() throws Exception{
        if(sapEndpoint == null)
            throw new Exception("Cannot load SAP Endpoint config");

        if(tcpEndpoint == null)
            throw new Exception("Cannot load TCP Endpoint config");

        if(tcpEndpoint.isServer()){
            try{
                connection = new ServerConnection(tcpEndpoint, isoBridge);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        else{
            try{
                connection = new ClientConnection(tcpEndpoint, isoBridge);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

        NodeService nodeService = (NodeService) SpringContextService.getApplicationContext().getBean("nodeService");
        SrcNodeListener srcNodeListener = null;
        SinkNodeListener sinkNodeListener = null;
        Long srcNodeId = sapEndpoint.getSrcNodeId();
        Long snkNodeId = sapEndpoint.getSnkNodeId();
        if(srcNodeId > 0) {
            Node node = nodeService.findByNodeId(srcNodeId);
            iso8583SrcNode = new ISO8583SrcNode(sapEndpoint, node, isoBridge, connection);
            EndpointStarter.addSrcNode(srcNodeId, iso8583SrcNode);
            srcNodeListener = new SrcNodeListener(iso8583SrcNode);
            connection.setSrcNodeListener(srcNodeListener);
        }
        if(snkNodeId > 0) {
            Node node = nodeService.findByNodeId(snkNodeId);
            iso8583SinkNode = new ISO8583SinkNode(sapEndpoint, node, isoBridge, connection);
            EndpointStarter.addSinkNode(snkNodeId, iso8583SinkNode);
            sinkNodeListener = new SinkNodeListener(iso8583SinkNode);
            connection.setSinkNodeListener(sinkNodeListener);
        }

        connection.start();
    }


    public void stop(){
        if(connection instanceof ServerConnection){
            ServerConnection serverTCPEndpoint = (ServerConnection) connection;
            serverTCPEndpoint.stop();
        }
        else if(connection instanceof ClientConnection){
            ClientConnection clientConnection = (ClientConnection) connection;
            clientConnection.stop();
        }
    }


    public ISO8583SinkNode getIso8583SinkNode() {
        return iso8583SinkNode;
    }

    public ISO8583SrcNode getIso8583SrcNode() {
        return iso8583SrcNode;
    }
}

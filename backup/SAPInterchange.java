package com.avantir.blowfish.api.iso8583;

import com.avantir.blowfish.config.IsoEndpointConfig;

/**
 * Created by lekanomotayo on 04/01/2018.
 */
public class SAPInterchange {

    ISO8583SrcNode iso8583SrcNode;
    IsoEndpointConfig isoEndpointConfig;
    ServerConnection connection;


    public SAPInterchange(IsoEndpointConfig isoEndpointConfig){
        this.isoEndpointConfig = isoEndpointConfig;
    }

    public void start() throws Exception{
        connection = new ServerConnection(isoEndpointConfig);
        iso8583SrcNode = new ISO8583SrcNode(connection);
        ISO8583MessageListener iso8583MessageListener = new ISO8583MessageListener(iso8583SrcNode);
        connection.setIso8583MessageListener(iso8583MessageListener);
        connection.start();
    }


    public void stop(){
        ServerConnection serverTCPEndpoint = (ServerConnection) connection;
        serverTCPEndpoint.stop();
    }


    public ISO8583SrcNode getIso8583SrcNode() {
        return iso8583SrcNode;
    }
}

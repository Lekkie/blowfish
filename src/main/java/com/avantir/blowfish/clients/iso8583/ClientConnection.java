package com.avantir.blowfish.clients.iso8583;

import com.avantir.blowfish.servers.iso8583.SrcNodeListener;
import com.avantir.blowfish.messaging.endpoint.IConnection;
import com.avantir.blowfish.messaging.entity.ISOBridge;
import com.avantir.blowfish.messaging.entity.TCPEndpoint;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.SpringContextService;
import com.github.kpavlov.jreactive8583.client.ClientConfiguration;
import com.github.kpavlov.jreactive8583.client.Iso8583Client;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by lekanomotayo on 01/01/2018.
 */
public class ClientConnection implements IConnection {

    private Iso8583Client<IsoMessage> client = null;
    TCPEndpoint tcpEndpoint;
    ISOBridge isoBridge;
    SinkNodeListener sinkNodeListener;
    SrcNodeListener srcNodeListener;

    public ClientConnection(TCPEndpoint tcpEndpoint, ISOBridge isoBridge) throws Exception{

        this.tcpEndpoint = tcpEndpoint;
        this.isoBridge = isoBridge;
    }

    public void start() throws Exception{

        if(isoBridge != null && isoBridge.getIsoPackagerName() != null){

            IsoMessageService isoMessageService = (IsoMessageService) SpringContextService.getApplicationContext().getBean("isoMessageService");
            MessageFactory<IsoMessage> messageFactory = isoMessageService.getMessageFactory(isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap());
            if(messageFactory != null && tcpEndpoint != null){

                SocketAddress socketAddress = new InetSocketAddress(tcpEndpoint.getIp(), tcpEndpoint.getPort());
                //client.getConfiguration().replyOnError(true);// [4]
                ClientConfiguration configuration = ClientConfiguration.newBuilder()
                        .withIdleTimeout(5) // idleTimeout
                        .withLogSensitiveData(false)
                        .build();
                client = new Iso8583Client<>(socketAddress, configuration, messageFactory);
                if(sinkNodeListener != null)
                    client.addMessageListener(sinkNodeListener);
                client.init();
                client.connect();
                //client.connectAsync();
                boolean isConnected = client.isConnected();
                System.out.println("connected: " + isConnected);
            }
        }else{
            throw new Exception("Missing IsoBridge details");
        }
    }

    public void send(IsoMessage isoMessage) throws Exception{
        //TODO
        try{
            client.send(isoMessage); // to send request
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @PreDestroy
    public void stop() {
        System.out.println("Shutting down connection");
        try{
            if(client != null)
                client.shutdown();
        }
        catch(Exception ex){}
    }

    public boolean isServer(){
        return false;
    }

    public SinkNodeListener getSinkNodeListener() {
        return sinkNodeListener;
    }

    public void setSinkNodeListener(SinkNodeListener sinkNodeListener) {
        this.sinkNodeListener = sinkNodeListener;
    }

    public SrcNodeListener getSrcNodeListener() {
        return srcNodeListener;
    }

    public void setSrcNodeListener(SrcNodeListener srcNodeListener) {
        this.srcNodeListener = srcNodeListener;
    }

}

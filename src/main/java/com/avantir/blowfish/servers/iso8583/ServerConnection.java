package com.avantir.blowfish.servers.iso8583;

import com.avantir.blowfish.messaging.endpoint.IConnection;
import com.avantir.blowfish.messaging.entity.ISOBridge;
import com.avantir.blowfish.messaging.entity.TCPEndpoint;
import com.avantir.blowfish.clients.iso8583.SinkNodeListener;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.SpringContextService;
import com.github.kpavlov.jreactive8583.server.Iso8583Server;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;

import javax.annotation.PreDestroy;
import java.net.URL;

/**
 * Created by lekanomotayo on 01/01/2018.
 */
public class ServerConnection  implements IConnection {

    private Iso8583Server server = null;
    TCPEndpoint tcpEndpoint;
    ISOBridge isoBridge;
    //SinkNodeListener sinkNodeListener;
    SrcNodeListener srcNodeListener;

    public ServerConnection(TCPEndpoint tcpEndpoint, ISOBridge isoBridge) {
        this.tcpEndpoint = tcpEndpoint;
        this.isoBridge = isoBridge;
    }

    public void start() throws Exception{
        if(isoBridge != null && isoBridge.getIsoPackagerName() != null) {

            IsoMessageService isoMessageService = (IsoMessageService) SpringContextService.getApplicationContext().getBean("isoMessageService");
            MessageFactory<IsoMessage> messageFactory = isoMessageService.getMessageFactory(isoBridge.getIsoPackagerName(), isoBridge.isBinaryBitmap());
            if(messageFactory != null && tcpEndpoint != null){

                server = new Iso8583Server<>(tcpEndpoint.getPort(), messageFactory);
                if(srcNodeListener != null)
                    server.addMessageListener(srcNodeListener);

                server.getConfiguration().setReplyOnError(true); // [4]
                server.init();
                server.start();
            }
        }else{
            throw new Exception("Missing IsoBridge details");
        }
    }

    public void send(IsoMessage isoMessage) throws Exception{
        //TODO
        // Not supported at the moment
        throw new Exception("Not Supported");
    }


    @PreDestroy
    public void stop() {
        System.out.println("Shutting down connection");
        try{server.shutdown();}
        catch(Exception ex){}
    }

    public boolean isServer(){
        return true;
    }


    public static void main(String[] args){

        try{
            ClassLoader classLoader = ServerConnection.class.getClassLoader();
            URL url = classLoader.getResource("packagers/POST_PACKAGER.xml");
            String templateFilename = url.getFile();
        }
        catch(Exception ex){

        }
    }

    public SinkNodeListener getSinkNodeListener() {
        return null;
        //return sinkNodeListener;
    }

    public void setSinkNodeListener(SinkNodeListener sinkNodeListener) {
        //this.sinkNodeListener = sinkNodeListener;
    }


    public SrcNodeListener getSrcNodeListener() {
        return srcNodeListener;
    }

    public void setSrcNodeListener(SrcNodeListener srcNodeListener) {
        this.srcNodeListener = srcNodeListener;
    }
}

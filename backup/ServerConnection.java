package com.avantir.blowfish.api.iso8583;

import com.avantir.blowfish.config.IsoEndpointConfig;
import com.avantir.blowfish.utils.IsoUtil;
import com.github.kpavlov.jreactive8583.server.Iso8583Server;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;

import javax.annotation.PreDestroy;
import java.net.URL;

/**
 * Created by lekanomotayo on 01/01/2018.
 */
public class ServerConnection{

    private Iso8583Server server = null;
    IsoEndpointConfig isoEndpointConfig;
    ISO8583MessageListener iso8583MessageListener;

    public ServerConnection(IsoEndpointConfig isoEndpointConfig) {
        this.isoEndpointConfig = isoEndpointConfig;
    }

    public void start() throws Exception{
        if(isoEndpointConfig != null && isoEndpointConfig.getIsoPackagerName() != null && isoEndpointConfig.getPort() > 0) {

            MessageFactory<IsoMessage> messageFactory = IsoUtil.getMessageFactory(isoEndpointConfig.getIsoPackagerName(), isoEndpointConfig.isBinaryBitmap());
            if(messageFactory != null){

                server = new Iso8583Server<>(isoEndpointConfig.getPort(), messageFactory);
                if(iso8583MessageListener != null)
                    server.addMessageListener(iso8583MessageListener);

                server.getConfiguration().setReplyOnError(true); // [4]
                server.init();
                server.start();
            }
        }else{
            throw new Exception("Missing IsoEndpointConfig details");
        }
    }

    @PreDestroy
    public void stop() {
        System.out.println("Shutting down connection");
        try{server.shutdown();}
        catch(Exception ex){}
    }

    public ISO8583MessageListener getIso8583MessageListener() {
        return iso8583MessageListener;
    }

    public void setIso8583MessageListener(ISO8583MessageListener iso8583MessageListener) {
        this.iso8583MessageListener = iso8583MessageListener;
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


}

package com.avantir.blowfish.messaging.endpoint;

import com.avantir.blowfish.servers.iso8583.SrcNodeListener;
import com.avantir.blowfish.clients.iso8583.SinkNodeListener;
import com.solab.iso8583.IsoMessage;

/**
 * Created by lekanomotayo on 04/01/2018.
 */
public interface IConnection {

    public boolean isServer();
    public void send(IsoMessage isoMessage) throws Exception;
    public void start() throws Exception;
    public SinkNodeListener getSinkNodeListener();
    public void setSinkNodeListener(SinkNodeListener sinkNodeListener);
    public SrcNodeListener getSrcNodeListener();
    public void setSrcNodeListener(SrcNodeListener srcNodeListener);
}

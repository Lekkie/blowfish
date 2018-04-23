package com.avantir.blowfish.clients.iso8583;

import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.SpringContextService;
import com.github.kpavlov.jreactive8583.IsoMessageListener;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by lekanomotayo on 04/01/2018.
 */
public class SinkNodeListener implements IsoMessageListener {

    ISO8583SinkNode iso8583SinkNode;

    public SinkNodeListener(ISO8583SinkNode iso8583SinkNode){
        this.iso8583SinkNode = iso8583SinkNode;
    }

    public boolean onMessage(ChannelHandlerContext ctx, IsoMessage isoMessage) {
        iso8583SinkNode.receiveResponseFromRemote(ctx, isoMessage);
        return false;
    }

    @Override
    public boolean applies(IsoMessage isoMessage) {
        IsoMessageService isoMessageService = (IsoMessageService) SpringContextService.getApplicationContext().getBean("isoMessageService");
        return isoMessageService.isResponse(isoMessage) && !isoMessageService.isEchoResponse(isoMessage);
    }

}

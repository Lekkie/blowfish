package com.avantir.blowfish.servers.iso8583;

import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.SpringContextService;
import com.github.kpavlov.jreactive8583.IsoMessageListener;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by lekanomotayo on 04/01/2018.
 */
public class SrcNodeListener  implements IsoMessageListener {

    ISO8583SrcNode iso8583SrcNode;

    public SrcNodeListener(ISO8583SrcNode iso8583SrcNode){
        this.iso8583SrcNode = iso8583SrcNode;
    }

    public boolean onMessage(ChannelHandlerContext ctx, IsoMessage isoMessage) {
        iso8583SrcNode.receiveRequestFromRemote(ctx, isoMessage);
        return false;
    }

    @Override
    public boolean applies(IsoMessage isoMessage) {
        IsoMessageService isoMessageService = (IsoMessageService) SpringContextService.getApplicationContext().getBean("isoMessageService");
        return isoMessageService.isRequest(isoMessage) && !isoMessageService.isEchoRequest(isoMessage);
    }

}

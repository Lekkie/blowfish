package com.avantir.blowfish.api.iso8583;

import com.avantir.blowfish.utils.IsoUtil;
import com.github.kpavlov.jreactive8583.IsoMessageListener;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by lekanomotayo on 04/01/2018.
 */
public class ISO8583MessageListener implements IsoMessageListener {

    ISO8583SrcNode iso8583SrcNode;

    public ISO8583MessageListener(ISO8583SrcNode iso8583SrcNode){
        this.iso8583SrcNode = iso8583SrcNode;
    }

    public boolean onMessage(ChannelHandlerContext ctx, IsoMessage isoMessage) {
        iso8583SrcNode.receiveRequestFromRemote(ctx, isoMessage);
        return false;
    }

    @Override
    public boolean applies(IsoMessage isoMessage) {
        return IsoUtil.isRequest(isoMessage) && !IsoUtil.isEchoRequest(isoMessage);
    }

}

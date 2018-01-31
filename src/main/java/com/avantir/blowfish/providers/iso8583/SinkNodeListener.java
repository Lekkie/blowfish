package com.avantir.blowfish.providers.iso8583;

import com.avantir.blowfish.utils.IsoUtil;
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
        return IsoUtil.isResponse(isoMessage) && !IsoUtil.isEchoResponse(isoMessage);
    }

}

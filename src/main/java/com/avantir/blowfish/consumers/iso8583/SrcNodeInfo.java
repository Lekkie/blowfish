package com.avantir.blowfish.consumers.iso8583;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by lekanomotayo on 06/01/2018.
 */
public class SrcNodeInfo {

    private Long srcNodeId;
    ChannelHandlerContext ctx;


    public Long getSrcNodeId() {
        return srcNodeId;
    }

    public void setSrcNodeId(Long srcNodeId) {
        this.srcNodeId = srcNodeId;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}

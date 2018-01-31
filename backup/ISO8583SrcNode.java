package com.avantir.blowfish.api.iso8583;

import com.avantir.blowfish.processor.IProcessor;
import com.avantir.blowfish.utils.IsoUtil;
import com.avantir.blowfish.utils.StringUtil;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Component
public class ISO8583SrcNode {

    protected static final Logger logger = LoggerFactory.getLogger(ISO8583SrcNode.class);
    protected IProcessor processor;
    protected ServerConnection connection;


    public ISO8583SrcNode(ServerConnection connection)throws Exception{

        this.connection = connection;
    }

    //  Ideally, only request shld go thru here...
    // send receive request to remote = send request to TM
    public void receiveRequestFromRemote(ChannelHandlerContext ctx, IsoMessage isoMessage){
        // translate ISO to local request object
        processor.process(null);
    }

    //Ideally, only response shld go thru here...
    // send response to remote = receive response from TM
    public void sendResponseToRemote(Object response){
        // translateresponse to ISO back
        ChannelHandlerContext ctx = null;
        IsoMessage isoMessage = null;
        ctx.writeAndFlush(isoMessage);
    }

}

package com.avantir.blowfish.model;

import com.avantir.blowfish.messaging.endpoint.ISO8583Node;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.RequestValidationService;
import com.avantir.blowfish.services.SpringContextService;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by lekanomotayo on 09/04/2018.
 */
public final class IsoRequest implements BlowfishRequest {

    private final static IsoMessageService isoMessageService = (IsoMessageService) SpringContextService.getApplicationContext().getBean("isoMessageService");
    private final static RequestValidationService requestValidationService = (RequestValidationService) SpringContextService.getApplicationContext().getBean("requestValidationService");

    private final IsoMessage isoMessage;
    private final ISO8583Node iso8583Node;
    private final ChannelHandlerContext channelHandlerContext;
    private final String isoPackagerName;
    private final boolean binaryBitmap;

    public IsoRequest(IsoMessage isoMessage, ISO8583Node iso8583Node,
                      ChannelHandlerContext channelHandlerContext, String isoPackagerName,
                      boolean binaryBitmap){
        this.isoMessage = new IsoMessage();
        int[] field = new int[]{0,1,2,3,4,5,6,7,8,9,
                10,11,12,13,14,15,16,17,18,19,
                20,21,22,23,24,25,26,27,28,29,
                30,31,32,33,34,35,36,37,38,39,
                40,41,42,43,44,45,46,47,48,49,
                50,51,52,53,54,55,56,57,58,59,
                60,61,62,63,64,65,66,67,68,69,
                70,71,72,73,74,75,76,77,78,79,
                80,81,82,83,84,85,86,87,88,89,
                90,91,92,93,94,95,96,97,98,99,
                100,101,102,103,104,105,106,107,108,109,
                110,111,112,113,114,115,116,117,118,119,
                120,121,122,123,124,125,126,127,128,129};
        isoMessage.copyFieldsFrom(isoMessage, field);
        this.iso8583Node = iso8583Node;
        this.channelHandlerContext = channelHandlerContext;
        this.isoPackagerName = isoPackagerName;
        this.binaryBitmap = binaryBitmap;
    }

    public void validate(){

    }

    public IsoMessage getIsoMessage() {
        IsoMessage isoMessageNew = new IsoMessage();
        int[] field = new int[]{0,1,2,3,4,5,6,7,8,9,
                10,11,12,13,14,15,16,17,18,19,
                20,21,22,23,24,25,26,27,28,29,
                30,31,32,33,34,35,36,37,38,39,
                40,41,42,43,44,45,46,47,48,49,
                50,51,52,53,54,55,56,57,58,59,
                60,61,62,63,64,65,66,67,68,69,
                70,71,72,73,74,75,76,77,78,79,
                80,81,82,83,84,85,86,87,88,89,
                90,91,92,93,94,95,96,97,98,99,
                100,101,102,103,104,105,106,107,108,109,
                110,111,112,113,114,115,116,117,118,119,
                120,121,122,123,124,125,126,127,128,129};
        isoMessageNew.copyFieldsFrom(isoMessage, field);
        return isoMessageNew;
    }

    public ISO8583Node getIso8583Node() {
        return iso8583Node;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public String getIsoPackagerName() {
        return isoPackagerName;
    }

    public boolean isBinaryBitmap() {
        return binaryBitmap;
    }

    // implement equals & hashcode & toString



}

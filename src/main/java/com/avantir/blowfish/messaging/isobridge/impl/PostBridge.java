package com.avantir.blowfish.messaging.isobridge.impl;

import com.avantir.blowfish.messaging.isobridge.AInterchange;
import com.avantir.blowfish.messaging.model.ISOBridge;
import com.solab.iso8583.IsoMessage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by lekanomotayo on 04/01/2018.
 */
@Component
public class PostBridge extends AInterchange {

    @PostConstruct
    public void register(){
        String name = PostBridge.class.getSimpleName();
        ISOBridge isoBridge = isoBridgeService.findByISOBridgeName(name);
        if(isoBridge == null){
            isoBridge = new ISOBridge();
            isoBridge.setName(name);
            isoBridge.setStatus(1);
            isoBridge.setClassName(name); //PostBridge.class.getName()
            isoBridge.setIsoPackagerName(PACKAGER + PACKAGER_EXT);
            isoBridge.setDescription("Postilion ISO8583 Bridge");
            isoBridge.setBinaryBitmap(true);
            isoBridgeService.create(isoBridge);
        }
    }



    public IsoMessage requestFromRemote(IsoMessage requestFromRemote){
        //TODO here
        return requestFromRemote;
    }
    public IsoMessage responseFromRemote(IsoMessage responseFromRemote){
        //TODO here
        return responseFromRemote;
    }
    public IsoMessage requestFromTranMgr(IsoMessage requestFromTranMgr){
        //TODO here
        return requestFromTranMgr;
    }
    public IsoMessage responseFromTranMgr(IsoMessage responseFromTranMgr){
        //TODO here
        return responseFromTranMgr;
    }
}

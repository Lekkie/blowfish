package com.avantir.blowfish.messaging.isobridge.impl;

import com.avantir.blowfish.messaging.isobridge.AInterchange;
import com.avantir.blowfish.messaging.services.ISOBridgeService;
import com.solab.iso8583.IsoMessage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by lekanomotayo on 04/01/2018.
 *
 * This bridge converts from Asoft protocol to Postilion protocol
 */
@Component
public class NIBSSBridge extends AInterchange {


    public NIBSSBridge(ISOBridgeService isoBridgeService){
        PACKAGER = "NIBSS_PACKAGER";
    }

    @PostConstruct
    public void register(){
        /*
        String name = AsoftBridge.class.getSimpleName();
        ISOBridge isoBridge = isoBridgeService.findByISOBridgeName(name);
        if(isoBridge == null){
            isoBridge = new ISOBridge();
            isoBridge.setName(name);
            isoBridge.setStatus(1);
            isoBridge.setClassName(name);
            isoBridge.setIsoPackagerName(PACKAGER + PACKAGER_EXT);
            isoBridge.setDescription("Asoft ISO8583 Bridge");
            isoBridge.setBinaryBitmap(true);
            isoBridgeService.create(isoBridge);
            ISOBridge defaultIsoBridge = isoBridgeService.findByISOBridgeName(DEFAULT_PACKAGER);
            messageFactory = IsoUtil.getMessageFactory(defaultIsoBridge.getIsoPackagerName(), defaultIsoBridge.isBinaryBitmap());
        }
        */
    }



    public IsoMessage requestFromRemote(IsoMessage requestFromRemote){
        //TODO here
        if(messageFactory!= null){
            int type = requestFromRemote.getType();
            IsoMessage message = messageFactory.newMessage(type);
            IsoMessage templ = messageFactory.getMessageTemplate(type);

            // Start copying here
        }
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

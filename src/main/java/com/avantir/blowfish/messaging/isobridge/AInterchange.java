package com.avantir.blowfish.messaging.isobridge;

import com.avantir.blowfish.messaging.services.ISOBridgeService;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lekanomotayo on 03/01/2018.
 */
public abstract class AInterchange implements Interchange {

    protected static String DEFAULT_PACKAGER = "POST_PACKAGER";
    protected static String PACKAGER = DEFAULT_PACKAGER;
    protected static String PACKAGER_EXT = ".xml";

    @Autowired
    protected ISOBridgeService isoBridgeService;
    protected MessageFactory<IsoMessage> messageFactory;


    public IsoMessage requestFromRemote(IsoMessage requestFromRemote){
        return requestFromRemote;
    }

    public IsoMessage responseFromRemote(IsoMessage responseFromRemote){
        return responseFromRemote;
    }

    public IsoMessage requestFromTranMgr(IsoMessage requestFromTranMgr){
        return requestFromTranMgr;
    }

    public IsoMessage responseFromTranMgr(IsoMessage responseFromTranMgr){
        return responseFromTranMgr;
    }


    protected IsoMessage copyToField(IsoMessage templ, int fieldNo, Object value, int len, IsoMessage destMessage){

        IsoValue templField = templ.getField(fieldNo);
        IsoType templFieldType = templField.getType();

        if(!templFieldType.needsLength())
            destMessage.setValue(fieldNo,  value, templFieldType, len);
        else
            destMessage.setValue(fieldNo, value, templFieldType, templField.getLength());

        return destMessage;
    }


}

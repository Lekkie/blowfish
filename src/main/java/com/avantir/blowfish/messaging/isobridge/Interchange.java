package com.avantir.blowfish.messaging.isobridge;

import com.solab.iso8583.IsoMessage;

/**
 * Created by lekanomotayo on 03/01/2018.
 */
public interface Interchange {

    // ISOBridge must self register
    public void register();
    // Transform request messages coming from remote endpoint
    public IsoMessage requestFromRemote(IsoMessage requestFromRemote);
    // Transform response messages coming from remote endpoint
    public IsoMessage responseFromRemote(IsoMessage responseFromRemote);
    // Transform request messages coming from Transaction Manager
    public IsoMessage requestFromTranMgr(IsoMessage requestFromTranMgr);
    // Transform response messages coming from Transaction Manager
    public IsoMessage responseFromTranMgr(IsoMessage responseFromTranMgr);

}

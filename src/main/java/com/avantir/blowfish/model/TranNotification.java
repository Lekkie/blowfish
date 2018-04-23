package com.avantir.blowfish.model;

import com.avantir.blowfish.messaging.Message;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.RequestValidationService;
import com.avantir.blowfish.services.SpringContextService;

/**
 * Created by lekanomotayo on 15/01/2018.
 */
public class TranNotification extends Message{

    public void validate(){

        assert getF0() != null;
        assert getF2() != null;
        assert getF3() != null;
        assert getF4() != null;
        assert getF7() != null;
        assert getF11() != null;
        assert getF14() != null;
        assert getF18() != null;
        assert getF22() != null;
        assert getF23() != null;
        assert getF25() != null;
        assert getF26() != null;
        assert getF32() != null;
        assert getF37() != null;
        assert getF39() != null;
        assert getF40() != null;
        assert getF41() != null;
        assert getF42() != null;
        assert getF43() != null;
        assert getF49() != null;
        assert getF102() != null;
        assert getF103() != null;
        assert getF123() != null;

    }

}

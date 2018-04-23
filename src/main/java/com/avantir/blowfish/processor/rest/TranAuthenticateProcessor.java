package com.avantir.blowfish.processor.rest;

import com.avantir.blowfish.model.BlowfishRequest;
import com.avantir.blowfish.model.BlowfishResponse;
import com.avantir.blowfish.model.TranAuthenticate;
import com.avantir.blowfish.model.TranResponse;
import com.avantir.blowfish.processor.request.RequestProcessor;
import com.avantir.blowfish.services.IsoMessageService;
import com.avantir.blowfish.services.RequestValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by lekanomotayo on 15/01/2018.
 */
@Component
public class TranAuthenticateProcessor implements RequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TranAuthenticateProcessor.class);

    @Autowired
    IsoMessageService isoMessageService;
    @Autowired
    RequestValidationService requestValidationService;

    public Optional<BlowfishResponse> process(BlowfishRequest blowfishRequest){

        TranAuthenticate tranAuthenticate = (TranAuthenticate) blowfishRequest;
        String pan = tranAuthenticate.getF2();
        String tranTypeCode = isoMessageService.getTranType(tranAuthenticate.getF3());
        String expDate = tranAuthenticate.getF14();
        String tid = tranAuthenticate.getF41();
        String mid = tranAuthenticate.getF42();

        requestValidationService.validate(mid, tid, tranTypeCode, pan, expDate);

        TranResponse tranResponse = new TranResponse("00", "Successful");
        return Optional.ofNullable(tranResponse);
    }

}

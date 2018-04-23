package com.avantir.blowfish.processor.protocol;

import com.avantir.blowfish.model.BlowfishRequest;
import com.avantir.blowfish.model.BlowfishResponse;

import java.util.Optional;

/**
 * Created by lekanomotayo on 09/04/2018.
 */
public interface ProtocolProcessor {

    public Optional<BlowfishResponse> processRequest(BlowfishRequest blowfishRequest);
    public void processResponse(BlowfishResponse blowfishResponse);

}

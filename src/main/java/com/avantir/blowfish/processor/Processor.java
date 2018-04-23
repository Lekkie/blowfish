package com.avantir.blowfish.processor;

import com.avantir.blowfish.model.BlowfishRequest;
import com.avantir.blowfish.model.BlowfishResponse;

import java.util.Optional;

/**
 * Created by lekanomotayo on 11/01/2018.
 */
public interface Processor {

    public Optional<BlowfishResponse> processRequest(BlowfishRequest blowfishRequest);

}

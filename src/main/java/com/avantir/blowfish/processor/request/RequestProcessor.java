package com.avantir.blowfish.processor.request;

import com.avantir.blowfish.model.BlowfishRequest;
import com.avantir.blowfish.model.BlowfishResponse;

import java.util.Optional;

/**
 * Created by lekanomotayo on 09/04/2018.
 */
public interface RequestProcessor {

    public Optional<BlowfishResponse> process(BlowfishRequest blowfishRequest);

}

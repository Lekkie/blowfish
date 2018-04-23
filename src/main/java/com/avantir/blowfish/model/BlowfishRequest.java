package com.avantir.blowfish.model;

/**
 * Created by lekanomotayo on 09/04/2018.
 */
public interface BlowfishRequest {


    /**
     *
     * Validate necessary fields are present depending on request type
     */
    public void validate();
}

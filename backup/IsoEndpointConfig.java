package com.avantir.blowfish.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lekanomotayo on 11/01/2018.
 */
@Configuration
public class IsoEndpointConfig {

    //@Value("${blowfish.port}")
    private int port;
    //@Value("${blowfish.isoPackagerName}")
    private String isoPackagerName;
    //@Value("${blowfish.isBinaryBitmap}")
    private boolean isBinaryBitmap;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIsoPackagerName() {
        return isoPackagerName;
    }

    public void setIsoPackagerName(String isoPackagerName) {
        this.isoPackagerName = isoPackagerName;
    }

    public boolean isBinaryBitmap() {
        return isBinaryBitmap;
    }

    public void setBinaryBitmap(boolean binaryBitmap) {
        isBinaryBitmap = binaryBitmap;
    }
}

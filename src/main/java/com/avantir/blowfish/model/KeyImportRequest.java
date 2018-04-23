package com.avantir.blowfish.model;

import com.avantir.blowfish.exceptions.BlowfishCryptographicException;
import com.avantir.blowfish.exceptions.BlowfishIllegalArgumentException;
import com.avantir.blowfish.services.SecurityService;
import com.avantir.blowfish.services.SpringContextService;

import java.util.List;

/**
 * Created by lekanomotayo on 20/04/2018.
 */
public final class KeyImportRequest implements BlowfishRequest {

    private static final SecurityService securityService = (SecurityService) SpringContextService.getApplicationContext().getBean("securityService");

    private String key;
    private String checkDigit;


    public void validate() {
        if(key == null || key.isEmpty())
            throw new BlowfishIllegalArgumentException("Key cannot be empty");
        if(checkDigit != null && !securityService.validCheckDigit(key, checkDigit))
                throw new BlowfishCryptographicException("Check digit failure " + key + "->" + checkDigit);
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCheckDigit() {
        return checkDigit;
    }

    public void setCheckDigit(String checkDigit) {
        this.checkDigit = checkDigit;
    }
}



package com.avantir.blowfish.model;

import com.avantir.blowfish.exceptions.BlowfishCryptographicException;
import com.avantir.blowfish.exceptions.BlowfishIllegalArgumentException;
import com.avantir.blowfish.exceptions.BlowfishIllegalStateException;
import com.avantir.blowfish.exceptions.BlowfishProcessingErrorException;
import com.avantir.blowfish.services.SecurityService;
import com.avantir.blowfish.services.SpringContextService;

import java.util.List;

/**
 * Created by lekanomotayo on 20/04/2018.
 */
public final class KeyComponent {

    private static final SecurityService securityService = (SecurityService) SpringContextService.getApplicationContext().getBean("securityService");

    private String component;
    private String checkDigit;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getCheckDigit() {
        return checkDigit;
    }

    public void setCheckDigit(String checkDigit) {
        this.checkDigit = checkDigit;
    }

    public void validate(){
        if(checkDigit != null && !checkDigit.isEmpty() && !securityService.validCheckDigit(component, checkDigit))
                throw new BlowfishCryptographicException("Check digit failure " + component + "->" + checkDigit);
    }
}

package com.avantir.blowfish.model;

import com.avantir.blowfish.exceptions.BlowfishIllegalArgumentException;
import com.avantir.blowfish.exceptions.BlowfishProcessingErrorException;

import java.util.List;

/**
 * Created by lekanomotayo on 20/04/2018.
 */
public final class KeyComponentImportRequest implements BlowfishRequest {

    private List<KeyComponent> components;


    public List<KeyComponent> getComponents() {
        return components;
    }

    public void setComponents(List<KeyComponent> components) {
        this.components = components;
    }

    public void validate() {
        for (KeyComponent keyComponent : components)
            keyComponent.validate();

    }

}



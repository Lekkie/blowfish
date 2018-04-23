package com.avantir.blowfish.model;

import com.avantir.blowfish.exceptions.BlowfishIllegalArgumentException;

import java.util.List;

/**
 * Created by lekanomotayo on 20/04/2018.
 */
public final class KeyGenerationRequest implements BlowfishRequest{
    
    private long keyCryptographicTypeId; // RSA, 3DES
    private long keyUsageTypeId; // LMK, ZMK, ZPK, DEK
    private long parentKeyId; // A ZMK key type can only have LMK parent, a ZPK, DEK can have ZMK parent

    public void validate(){
        if(keyCryptographicTypeId  < 0)
            throw new BlowfishIllegalArgumentException("Key cryptographic type cannot be empty");
        if(keyUsageTypeId  < 0)
            throw new BlowfishIllegalArgumentException("Key usage type cannot be empty");
    }


    public long getKeyUsageTypeId() {
        return keyUsageTypeId;
    }

    public void setKeyUsageTypeId(long keyUsageTypeId) {
        this.keyUsageTypeId = keyUsageTypeId;
    }

    public long getParentKeyId() {
        return parentKeyId;
    }

    public void setParentKeyId(long parentKeyId) {
        this.parentKeyId = parentKeyId;
    }

    public long getKeyCryptographicTypeId() {
        return keyCryptographicTypeId;
    }

    public void setKeyCryptographicTypeId(long keyCryptographicTypeId) {
        this.keyCryptographicTypeId = keyCryptographicTypeId;
    }
}

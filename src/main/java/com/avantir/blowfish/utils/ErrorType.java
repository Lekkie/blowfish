package com.avantir.blowfish.utils;

/**
 * Created by lekanomotayo on 02/04/2018.
 */
public enum ErrorType {

    ILLEGAL_ARGUMENT(1000),
    ENTITY_NOT_FOUND(1101),
    ENTITY_NOT_CREATED(1102),
    ENTITY_NOT_UPDATED(1103),
    ENTITY_NOT_DELETED(1104),
    ILLEGAL_STATE(1105),
    PROCESSING_ERROR(1106),
    HSM_CONNECTION(1107),
    OPERATION_NOT_SUPPORTED(1108),
    CRYPTOGRAPHIC_ERROR(1109),
    ENCRYPT_ERROR(1200);

    private final int code;

    ErrorType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

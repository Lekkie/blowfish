package com.avantir.blowfish.utils;

/**
 * Created by lekanomotayo on 02/04/2018.
 */
public enum EntityType {

    KEY(10),
    ACQUIRER(11),
    BIN(12),
    MERCHANT(13),
    TERMINAL(14);

    private final int id;

    EntityType(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

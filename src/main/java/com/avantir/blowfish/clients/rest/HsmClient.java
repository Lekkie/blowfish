package com.avantir.blowfish.clients.rest;

import com.avantir.blowfish.entity.Endpoint;
import com.avantir.blowfish.entity.Key;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.mock.HsmProvider;
import com.avantir.blowfish.services.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by lekanomotayo on 18/04/2018.
 */

//@Service
public final class HsmClient {

    private final Endpoint endpoint;

    //@Autowired
    HsmProvider hsmProvider;


    //@Autowired
    public HsmClient(EndpointService endpointService){
        endpoint = endpointService.findByEndpointId(1L).orElseThrow(() -> new BlowfishEntityNotFoundException("HSM Endpoint"));
    }

    public Optional<Key> generateKey(){
        Key key = new Key();
        return Optional.ofNullable(key);
    }

    public Optional<Key> saveKey(Key key){
        return Optional.ofNullable(key);
    }

    public Optional<Key> getKey(String keyId){
        Key key = new Key();
        return Optional.ofNullable(key);
    }

    public Optional<String> encryptWith3DES(String secretKey, String cipherText){
        return Optional.ofNullable(hsmProvider.encryptWith3DES(secretKey, cipherText));
    }

    public Optional<String> decryptWith3DES(String key, String cryptogram){
        return Optional.ofNullable(hsmProvider.decryptWith3DES(key, cryptogram));
    }

    public Optional<String> encryptWithRSA(String publicKeyBase64, String cipherText){
        return Optional.ofNullable(hsmProvider.encryptWithRSA(publicKeyBase64, cipherText));
    }

    public Optional<String> decryptWithRSA(String key, String cryptogram){
        return Optional.empty();
    }

}

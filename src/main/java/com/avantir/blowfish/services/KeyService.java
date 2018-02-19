package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.Key;
import com.avantir.blowfish.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class KeyService {

    public static final String ALL = "all";
    public static final String ACTIVE = "active";

    @Autowired
    private KeyRepository keyRepository;


    @Cacheable(value = "key")
    @Transactional(readOnly=true)
    public Key findById(Long id) {

        try
        {
            return keyRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "key")
    @Transactional(readOnly=true)
    public Key findByVersion(String version) {

        try
        {
            return keyRepository.findByVersion(version);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "keys")
    @Transactional(readOnly=true)
    public List<Key> findAllActive() {

        try
        {
            List<Key> list = keyRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

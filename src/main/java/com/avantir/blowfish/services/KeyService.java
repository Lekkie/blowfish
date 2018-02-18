package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Key;
import com.avantir.blowfish.repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class KeyService {

    @Autowired
    private KeyRepository keyRepository;


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


}

package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.KeyVersion;
import com.avantir.blowfish.model.TranType;
import com.avantir.blowfish.repository.KeyVersionRepository;
import com.avantir.blowfish.repository.TranTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class KeyVersionService {

    @Autowired
    private KeyVersionRepository keyVersionRepository;


    @Transactional(readOnly=true)
    public KeyVersion findById(Long id) {

        try
        {
            return keyVersionRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public KeyVersion findByVersion(String version) {

        try
        {
            return keyVersionRepository.findByVersion(version);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}

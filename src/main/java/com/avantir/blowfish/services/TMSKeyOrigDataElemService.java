package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.TMSKeyOrigDataElem;
import com.avantir.blowfish.repository.TMSKeyOrigDataElemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class TMSKeyOrigDataElemService {

    @Autowired
    private TMSKeyOrigDataElemRepository tmsKeyOrigDataElemRepository;

    @Transactional(readOnly=false)
    public TMSKeyOrigDataElem create(TMSKeyOrigDataElem switchKeyOrigDataElem) {
        return tmsKeyOrigDataElemRepository.save(switchKeyOrigDataElem);
    }

    @Transactional(readOnly=true)
    public TMSKeyOrigDataElem findByTmsKeyOrigDataElemId(Long switchKey) {

        try
        {
            return tmsKeyOrigDataElemRepository.findByTmsKeyOrigDataElemId(switchKey);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
    @Transactional(readOnly=true)
    public TMSKeyOrigDataElem findByOriginalDataElement(String originalDataElement) {

        try
        {
            return tmsKeyOrigDataElemRepository.findByOriginalDataElementAllIgnoringCase(originalDataElement);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}

package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.TMSKeyOrigDataElem;
import com.avantir.blowfish.repository.TMSKeyOrigDataElemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class TMSKeyOrigDataElemService {

    @Autowired
    private TMSKeyOrigDataElemRepository tmsKeyOrigDataElemRepository;

    @Transactional(readOnly=false)
    public Optional<TMSKeyOrigDataElem> create(TMSKeyOrigDataElem switchKeyOrigDataElem) {
        return Optional.ofNullable(tmsKeyOrigDataElemRepository.save(switchKeyOrigDataElem));
    }

    @Transactional(readOnly=true)
    public Optional<TMSKeyOrigDataElem> findByTmsKeyOrigDataElemId(Long switchKey) {
        return tmsKeyOrigDataElemRepository.findByTmsKeyOrigDataElemId(switchKey);
    }
    @Transactional(readOnly=true)
    public Optional<TMSKeyOrigDataElem> findByOriginalDataElement(String originalDataElement) {
        return tmsKeyOrigDataElemRepository.findByOriginalDataElementAllIgnoringCase(originalDataElement);
    }


}

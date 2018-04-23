package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.KeyCryptographicType;
import com.avantir.blowfish.repository.KeyCryptographicTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class KeyCryptographicTypeService {

    @Autowired
    private KeyCryptographicTypeRepository keyCryptographicTypeRepository;
    @Autowired
    StringService stringService;

    @Transactional(readOnly=true)
    public Optional<KeyCryptographicType> findByKeyCryptographicTypeId(Long id) {
        return keyCryptographicTypeRepository.findByKeyCryptographicTypeId(id);
    }

    @Transactional(readOnly=true)
    public Optional<KeyCryptographicType> findByCode(String code) {
        return keyCryptographicTypeRepository.findByCodeAllIgnoringCase(code);
    }


}

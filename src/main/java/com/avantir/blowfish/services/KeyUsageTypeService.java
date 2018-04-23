package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.KeyCryptographicType;
import com.avantir.blowfish.entity.KeyUsageType;
import com.avantir.blowfish.repository.KeyUsageTypeRepository;
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
public class KeyUsageTypeService {

    @Autowired
    private KeyUsageTypeRepository keyUsageTypeRepository;

    @Transactional(readOnly=true)
    public Optional<KeyUsageType> findByKeyUsageTypeId(Long id) {
        return keyUsageTypeRepository.findByKeyUsageTypeId(id);
    }

    @Transactional(readOnly=true)
    public Optional<KeyUsageType> findByCode(String code) {
        return keyUsageTypeRepository.findByCodeAllIgnoringCase(code);
    }


}

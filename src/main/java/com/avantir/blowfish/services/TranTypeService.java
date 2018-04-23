package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.TranType;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.TranTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class TranTypeService {

    @Autowired
    private TranTypeRepository tranTypeRepository;
    @Autowired
    StringService stringService;

    @Transactional(readOnly=false)
    public Optional<TranType> create(TranType tranType) {
        return Optional.ofNullable(tranTypeRepository.save(tranType));
    }

    @Transactional(readOnly=false)
    public Optional<TranType> update(TranType newTranType) {
        TranType oldTranType = tranTypeRepository.findByTranTypeId(newTranType.getTranTypeId()).orElseThrow(() -> new BlowfishEntityNotFoundException("TranType"));

        if(!stringService.isEmpty(newTranType.getName()))
            oldTranType.setName(newTranType.getName());
        if(!stringService.isEmpty(newTranType.getCode()))
            oldTranType.setCode(newTranType.getCode());
        if(!stringService.isEmpty(newTranType.getDescription()))
            oldTranType.setDescription(newTranType.getDescription());
        oldTranType.setStatus(newTranType.getStatus());
        return Optional.ofNullable(tranTypeRepository.save(oldTranType));
    }

    @Transactional(readOnly=true)
    public Optional<TranType> findByTranTypeId(Long id) {
        return tranTypeRepository.findByTranTypeId(id);
    }

    @Transactional(readOnly=true)
    public Optional<TranType> findByCode(String code) {
        return tranTypeRepository.findByCodeAllIgnoringCase(code);
    }


    @Transactional(readOnly=true)
    public Optional<List<TranType>> findAllActive() {
        return tranTypeRepository.findByStatus(1);
    }

}

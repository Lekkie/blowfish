package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.AcquirerTermParam;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.AcquirerTermParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
public class AcquirerTermParamService {


    public static final String ALL_ACQ_TERM_PARAM = "ALL_ACQ_TERM_PARAM";
    public static final String ACTIVE_ACQ_TERM_PARAM = "ACTIVE_ACQ_TERM_PARAM";

    @Autowired
    private AcquirerTermParamRepository acquirerTerminalParameterRepository;


    @CachePut(cacheNames="acqTermParam")
    @Transactional(readOnly=false)
    public Optional<AcquirerTermParam> create(AcquirerTermParam acquirerTermParam) {
        return Optional.ofNullable(acquirerTerminalParameterRepository.save(acquirerTermParam));
    }


    @CachePut(cacheNames="acqTermParam")
    @Transactional(readOnly=false)
    public Optional<AcquirerTermParam> update(AcquirerTermParam newAcquirerTermParam) {

        Optional<AcquirerTermParam> oldAcqTermParamOptional = acquirerTerminalParameterRepository.findByAcquirerTermParamId(newAcquirerTermParam.getAcquirerTermParamId());
        AcquirerTermParam oldAcqTermParam = oldAcqTermParamOptional.orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerTermParam"));

        if(newAcquirerTermParam.getAcquirerId() != 0)
            oldAcqTermParam.setAcquirerId(newAcquirerTermParam.getAcquirerId());
        if(newAcquirerTermParam.getTermParamId() != 0)
            oldAcqTermParam.setTermParamId(newAcquirerTermParam.getTermParamId());

        return Optional.ofNullable(acquirerTerminalParameterRepository.save(oldAcqTermParam));
    }


    @CacheEvict(value = "acqTermParam")
    @Transactional(readOnly=false)
    public void delete(long id) {
        acquirerTerminalParameterRepository.delete(id);
    }



    @Cacheable(value = "acqTermParam")
    @Transactional(readOnly=true)
    public Optional<AcquirerTermParam> findByAcquirerTermParamId(Long id) {
        return acquirerTerminalParameterRepository.findByAcquirerTermParamId(id);
    }

    @Cacheable(value = "acqTermParam")
    @Transactional(readOnly=true)
    public Optional<AcquirerTermParam> findByAcquirerId(Long acquirerId) {
        return acquirerTerminalParameterRepository.findByAcquirerId(acquirerId);
    }

    @Cacheable(value = "acqTermParam")
    @Transactional(readOnly=true)
    public Optional<List<AcquirerTermParam>> findByTermParamId(Long termParamId) {
        return acquirerTerminalParameterRepository.findByTermParamId(termParamId);
    }



    @Cacheable(value = "acqTermParam", key = "#root.target.ALL_ACQ_TERM_PARAM")
    @Transactional(readOnly=true)
    public Optional<List<AcquirerTermParam>> findAll() {
        return Optional.ofNullable(acquirerTerminalParameterRepository.findAll());
    }


}

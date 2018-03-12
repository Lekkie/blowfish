package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.AcquirerTermParam;
import com.avantir.blowfish.repository.AcquirerTermParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
public class AcquirerTermParamService {


    public static final String ALL_ACQ_TERM_PARAM = "ALL_ACQ_TERM_PARAM";
    public static final String ACTIVE_ACQ_TERM_PARAM = "ACTIVE_ACQ_TERM_PARAM";

    @Autowired
    private AcquirerTermParamRepository acquirerTerminalParameterRepository;


    @CachePut(cacheNames="acqTermParam")
    @Transactional(readOnly=false)
    public AcquirerTermParam create(AcquirerTermParam acquirerTermParam) {
        return acquirerTerminalParameterRepository.save(acquirerTermParam);
    }


    @CachePut(cacheNames="acqTermParam")
    @Transactional(readOnly=false)
    public AcquirerTermParam update(AcquirerTermParam newAcquirerTermParam) {
        if(newAcquirerTermParam != null){
            AcquirerTermParam oldAcquirerTermParam = acquirerTerminalParameterRepository.findByAcquirerTermParamId(newAcquirerTermParam.getAcquirerTermParamId());
            if(newAcquirerTermParam.getAcquirerId() != 0)
                oldAcquirerTermParam.setAcquirerId(newAcquirerTermParam.getAcquirerId());
            if(newAcquirerTermParam.getTermParamId() != 0)
                oldAcquirerTermParam.setTermParamId(newAcquirerTermParam.getTermParamId());
            return acquirerTerminalParameterRepository.save(oldAcquirerTermParam);
        }
        return null;
    }


    @CacheEvict(value = "acqTermParam")
    @Transactional(readOnly=false)
    public void delete(long id) {
        acquirerTerminalParameterRepository.delete(id);
    }



    @Cacheable(value = "acqTermParam")
    @Transactional(readOnly=true)
    public AcquirerTermParam findByAcquirerTermParamId(Long id) {

        try
        {
            //Optional<AcquirerMerchant> optional = acquirerMerchantRepository.findById(id);
            //return optional.orElse(null);
            return acquirerTerminalParameterRepository.findByAcquirerTermParamId(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "acqTermParam")
    @Transactional(readOnly=true)
    public AcquirerTermParam findByAcquirerId(Long acquirerId) {

        try
        {
            return acquirerTerminalParameterRepository.findByAcquirerId(acquirerId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "acqTermParam")
    @Transactional(readOnly=true)
    public List<AcquirerTermParam> findByTermParamId(Long termParamId) {

        try
        {
            return acquirerTerminalParameterRepository.findByTermParamId(termParamId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



    @Cacheable(value = "acqTermParam", key = "#root.target.ALL_ACQ_TERM_PARAM")
    @Transactional(readOnly=true)
    public List<AcquirerTermParam> findAll() {

        try
        {
            List<AcquirerTermParam> list = acquirerTerminalParameterRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



}

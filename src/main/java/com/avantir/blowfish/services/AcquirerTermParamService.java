package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.AcquirerTermParam;
import com.avantir.blowfish.repository.AcquirerTermParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class AcquirerTermParamService {

    @Autowired
    private AcquirerTermParamRepository acquirerTerminalParameterRepository;


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



}

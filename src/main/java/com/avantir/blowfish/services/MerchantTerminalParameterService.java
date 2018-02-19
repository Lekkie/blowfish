package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.AcquirerTerminalParameter;
import com.avantir.blowfish.model.MerchantTerminalParameter;
import com.avantir.blowfish.repository.AcquirerTerminalParameterRepository;
import com.avantir.blowfish.repository.MerchantTerminalParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class MerchantTerminalParameterService {

    @Autowired
    private MerchantTerminalParameterRepository merchantTerminalParameterRepository;


    @Transactional(readOnly=true)
    public MerchantTerminalParameter findById(Long id) {

        try
        {
            //Optional<AcquirerMerchant> optional = acquirerMerchantRepository.findById(id);
            //return optional.orElse(null);
            return merchantTerminalParameterRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public MerchantTerminalParameter findByMerchantId(Long merchantId) {

        try
        {
            return merchantTerminalParameterRepository.findByMerchantId(merchantId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



}

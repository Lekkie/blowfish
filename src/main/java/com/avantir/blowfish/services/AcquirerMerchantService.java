package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.AcquirerMerchant;
import com.avantir.blowfish.model.MerchantTerminal;
import com.avantir.blowfish.repository.AcquirerMerchantRepository;
import com.avantir.blowfish.repository.MerchantTerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class AcquirerMerchantService {

    @Autowired
    private AcquirerMerchantRepository acquirerMerchantRepository;


    @Transactional(readOnly=true)
    public AcquirerMerchant findById(Long id) {

        try
        {
            Optional<AcquirerMerchant> optional = acquirerMerchantRepository.findById(id);
            return optional.orElse(null);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public AcquirerMerchant findByMerchantId(Long merchantId) {

        try
        {
            return acquirerMerchantRepository.findByMerchantId(merchantId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



}

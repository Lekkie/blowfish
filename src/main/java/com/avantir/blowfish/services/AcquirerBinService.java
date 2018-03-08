package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.AcquirerBin;
import com.avantir.blowfish.model.AcquirerMerchant;
import com.avantir.blowfish.repository.AcquirerBinRepository;
import com.avantir.blowfish.repository.AcquirerMerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class AcquirerBinService {

    @Autowired
    private AcquirerBinRepository acquirerBinRepository;


    @Transactional(readOnly=true)
    public AcquirerBin findById(Long id) {

        try
        {
            //Optional<AcquirerMerchant> optional = acquirerMerchantRepository.findById(id);
            //return optional.orElse(null);
            return acquirerBinRepository.findByAcquirerBinId(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public AcquirerBin findByBinId(Long binId) {

        try
        {
            return acquirerBinRepository.findByBinId(binId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



}

package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.Merchant;
import com.avantir.blowfish.repository.AcquirerRepository;
import com.avantir.blowfish.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;


    @Transactional(readOnly=true)
    public Merchant findById(Long id) {

        try
        {
            return merchantRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public Merchant findByCode(String code) {

        try
        {
            return merchantRepository.findByCodeAllIgnoringCase(code);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Transactional(readOnly=true)
    public List<Merchant> findAllActive() {

        try
        {
            List<Merchant> list = merchantRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

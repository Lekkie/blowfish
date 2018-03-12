package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.AcquirerBin;
import com.avantir.blowfish.model.AcquirerMerchant;
import com.avantir.blowfish.repository.AcquirerBinRepository;
import com.avantir.blowfish.repository.AcquirerMerchantRepository;
import com.avantir.blowfish.utils.StringUtil;
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
public class AcquirerBinService {

    public static final String ALL_ACQUIRER_BIN = "ALL_ACQUIRER_BIN";
    public static final String ACTIVE_ACQUIRER_BIN = "ACTIVE_ACQUIRER_BIN";


    @Autowired
    private AcquirerBinRepository acquirerBinRepository;


    @CachePut(cacheNames="acquirerBin")
    // , key = "#result.firstName + #result.lastName"
    @Transactional(readOnly=false)
    public AcquirerBin create(AcquirerBin acquirerMerchant) {
        return acquirerBinRepository.save(acquirerMerchant);
    }


    @CachePut(cacheNames="acquirerBin", unless="#result==null", key = "#result.id")
    @Transactional(readOnly=false)
    public AcquirerBin update(AcquirerBin newAcquirerMerchant) {
        if(newAcquirerMerchant != null){
            AcquirerBin oldMerchantTerminal = acquirerBinRepository.findByAcquirerBinId(newAcquirerMerchant.getAcquirerBinId());
            if(newAcquirerMerchant.getBinId() != 0)
                oldMerchantTerminal.setBinId(newAcquirerMerchant.getBinId());
            if(newAcquirerMerchant.getAcquirerId() != 0)
                oldMerchantTerminal.setAcquirerId(newAcquirerMerchant.getAcquirerId());
            if(!StringUtil.isEmpty(newAcquirerMerchant.getCreatedBy()))
                oldMerchantTerminal.setCreatedBy(newAcquirerMerchant.getCreatedBy());
            if(newAcquirerMerchant.getCreatedOn() != null)
                oldMerchantTerminal.setCreatedOn(newAcquirerMerchant.getCreatedOn());
            AcquirerBin merchantTerminal1 = acquirerBinRepository.save(oldMerchantTerminal);
            //evictAllCache();
            return oldMerchantTerminal;
        }
        return null;
    }


    @CacheEvict(value = "acquirerBin")
    @Transactional(readOnly=false)
    public void delete(long id) {
        acquirerBinRepository.delete(id);
    }


    @Cacheable(value = "acquirerBin")
    @Transactional(readOnly=true)
    public AcquirerBin findByAcquirerBinId(Long acquirerBinId) {

        try
        {
            //Optional<AcquirerMerchant> optional = acquirerMerchantRepository.findById(id);
            //return optional.orElse(null);
            return acquirerBinRepository.findByAcquirerBinId(acquirerBinId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "acquirerBin")
    @Transactional(readOnly=true)
    public List<AcquirerBin> findByAcquirerId(Long acquirerId) {

        try
        {
            return acquirerBinRepository.findByAcquirerId(acquirerId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "acquirerBin")
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



    @Cacheable(value = "acquirerBin", key = "#root.target.ALL_ACQUIRER_BIN")
    @Transactional(readOnly=true)
    public List<AcquirerBin> findAll() {

        try
        {
            List<AcquirerBin> list = acquirerBinRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

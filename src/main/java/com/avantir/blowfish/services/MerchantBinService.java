package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.AcquirerBin;
import com.avantir.blowfish.model.MerchantBin;
import com.avantir.blowfish.repository.MerchantBinRepository;
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
public class MerchantBinService {

    public static final String ALL_MERCHANT_BIN = "ALL_MERCHANT_BIN";
    public static final String ACTIVE_MERCHANT_BIN = "ACTIVE_MERCHANT_BIN";


    @Autowired
    private MerchantBinRepository merchantBinRepository;


    @CachePut(cacheNames="merchantBin")
    // , key = "#result.firstName + #result.lastName"
    @Transactional(readOnly=false)
    public MerchantBin create(MerchantBin acquirerMerchant) {
        return merchantBinRepository.save(acquirerMerchant);
    }


    @CachePut(cacheNames="merchantBin", unless="#result==null", key = "#result.id")
    @Transactional(readOnly=false)
    public MerchantBin update(MerchantBin newAcquirerMerchant) {
        if(newAcquirerMerchant != null){
            MerchantBin oldMerchantTerminal = merchantBinRepository.findByMerchantBinId(newAcquirerMerchant.getMerchantBinId());
            if(newAcquirerMerchant.getBinId() != 0)
                oldMerchantTerminal.setBinId(newAcquirerMerchant.getBinId());
            if(newAcquirerMerchant.getMerchantId() != 0)
                oldMerchantTerminal.setMerchantId(newAcquirerMerchant.getMerchantId());
            if(!StringUtil.isEmpty(newAcquirerMerchant.getCreatedBy()))
                oldMerchantTerminal.setCreatedBy(newAcquirerMerchant.getCreatedBy());
            if(newAcquirerMerchant.getCreatedOn() != null)
                oldMerchantTerminal.setCreatedOn(newAcquirerMerchant.getCreatedOn());
            MerchantBin merchantTerminal1 = merchantBinRepository.save(oldMerchantTerminal);
            //evictAllCache();
            return oldMerchantTerminal;
        }
        return null;
    }


    @CacheEvict(value = "merchantBin")
    @Transactional(readOnly=false)
    public void delete(long id) {
        merchantBinRepository.delete(id);
    }


    @Cacheable(value = "merchantBin")
    @Transactional(readOnly=true)
    public MerchantBin findByMerchantBinId(Long merchantBinId) {

        try
        {
            //Optional<AcquirerMerchant> optional = acquirerMerchantRepository.findById(id);
            //return optional.orElse(null);
            return merchantBinRepository.findByMerchantBinId(merchantBinId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "merchantBin")
    @Transactional(readOnly=true)
    public List<MerchantBin> findByMerchantId(Long merchantId) {

        try
        {
            return merchantBinRepository.findByMerchantId(merchantId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "merchantBin")
    @Transactional(readOnly=true)
    public List<MerchantBin> findByBinId(Long binId) {

        try
        {
            return merchantBinRepository.findByBinId(binId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



    @Cacheable(value = "merchantBin", key = "#root.target.ALL_ACQUIRER_BIN")
    @Transactional(readOnly=true)
    public List<MerchantBin> findAll() {

        try
        {
            List<MerchantBin> list = merchantBinRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

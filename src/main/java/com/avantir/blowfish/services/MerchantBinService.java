package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.MerchantBin;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.MerchantBinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
public class MerchantBinService {

    public static final String ALL_MERCHANT_BIN = "ALL_MERCHANT_BIN";
    public static final String ACTIVE_MERCHANT_BIN = "ACTIVE_MERCHANT_BIN";


    @Autowired
    private MerchantBinRepository merchantBinRepository;
    @Autowired
    StringService stringService;


    @CachePut(cacheNames="merchantBin")
    // , key = "#result.firstName + #result.lastName"
    @Transactional(readOnly=false)
    public Optional<MerchantBin> create(MerchantBin acquirerMerchant) {
        return Optional.ofNullable(merchantBinRepository.save(acquirerMerchant));
    }


    @CachePut(cacheNames="merchantBin", unless="#result==null", key = "#result.id")
    @Transactional(readOnly=false)
    public Optional<MerchantBin> update(MerchantBin newAcquirerMerchant) {
        MerchantBin oldMerchantTerminal = merchantBinRepository.findByMerchantBinId(newAcquirerMerchant.getMerchantBinId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Acquirer"));

        if(newAcquirerMerchant.getBinId() != 0)
            oldMerchantTerminal.setBinId(newAcquirerMerchant.getBinId());
        if(newAcquirerMerchant.getMerchantId() != 0)
            oldMerchantTerminal.setMerchantId(newAcquirerMerchant.getMerchantId());
        if(!stringService.isEmpty(newAcquirerMerchant.getCreatedBy()))
            oldMerchantTerminal.setCreatedBy(newAcquirerMerchant.getCreatedBy());
        if(newAcquirerMerchant.getCreatedOn() != null)
            oldMerchantTerminal.setCreatedOn(newAcquirerMerchant.getCreatedOn());
        return Optional.ofNullable(merchantBinRepository.save(oldMerchantTerminal));
    }


    @CacheEvict(value = "merchantBin")
    @Transactional(readOnly=false)
    public void delete(long id) {
        merchantBinRepository.delete(id);
    }


    @Cacheable(value = "merchantBin")
    @Transactional(readOnly=true)
    public Optional<MerchantBin> findByMerchantBinId(Long merchantBinId) {
        return merchantBinRepository.findByMerchantBinId(merchantBinId);
    }


    @Cacheable(value = "merchantBin")
    @Transactional(readOnly=true)
    public Optional<List<MerchantBin>> findByMerchantId(Long merchantId) {
        return merchantBinRepository.findByMerchantId(merchantId);
    }

    @Cacheable(value = "merchantBin")
    @Transactional(readOnly=true)
    public Optional<List<MerchantBin>> findByBinId(Long binId) {
        return merchantBinRepository.findByBinId(binId);
    }



    @Cacheable(value = "merchantBin", key = "#root.target.ALL_ACQUIRER_BIN")
    @Transactional(readOnly=true)
    public Optional<List<MerchantBin>> findAll() {
        return Optional.ofNullable(merchantBinRepository.findAll());
    }

}

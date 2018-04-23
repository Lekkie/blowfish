package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.AcquirerMerchant;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.exceptions.BlowfishIllegalArgumentException;
import com.avantir.blowfish.repository.AcquirerMerchantRepository;
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
public class AcquirerMerchantService {

    public static final String ALL_ACQUIRER_MERCHANT = "ALL_ACQUIRER_MERCHANT";
    public static final String ACTIVE_ACQUIRER_MERCHANT = "ACTIVE_ACQUIRER_MERCHANT";


    @Autowired
    private AcquirerMerchantRepository acquirerMerchantRepository;
    @Autowired
    StringService stringService;



    @CachePut(cacheNames="acquirerMerchant")
    // , key = "#result.firstName + #result.lastName"
    @Transactional(readOnly=false)
    public Optional<AcquirerMerchant> create(AcquirerMerchant acquirerMerchant) {
        return Optional.ofNullable(acquirerMerchantRepository.save(acquirerMerchant));
    }


    @CachePut(cacheNames="acquirerMerchant", unless="#result==null", key = "#result.id")
    @Transactional(readOnly=false)
    public Optional<AcquirerMerchant> update(AcquirerMerchant newAcquirerMerchant) {

        Optional<AcquirerMerchant> oldAcqMerchOptional = acquirerMerchantRepository.findByAcquirerMerchantId(newAcquirerMerchant.getAcquirerMerchantId());
        AcquirerMerchant oldAcquirerMerchant = oldAcqMerchOptional.orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerMerchant"));

        if(newAcquirerMerchant.getMerchantId() != 0)
            oldAcquirerMerchant.setMerchantId(newAcquirerMerchant.getMerchantId());
        if(newAcquirerMerchant.getAcquirerId() != 0)
            oldAcquirerMerchant.setAcquirerId(newAcquirerMerchant.getAcquirerId());
        if(!stringService.isEmpty(newAcquirerMerchant.getCreatedBy()))
            oldAcquirerMerchant.setCreatedBy(newAcquirerMerchant.getCreatedBy());
        if(newAcquirerMerchant.getCreatedOn() != null)
            oldAcquirerMerchant.setCreatedOn(newAcquirerMerchant.getCreatedOn());

        return Optional.ofNullable(acquirerMerchantRepository.save(oldAcquirerMerchant));
    }


    @CacheEvict(value = "acquirerMerchant")
    @Transactional(readOnly=false)
    public void delete(long id) {
        acquirerMerchantRepository.delete(id);
    }

    @Cacheable(value = "acquirerMerchant")
    @Transactional(readOnly=true)
    public Optional<AcquirerMerchant> findByAcquirerMerchantId(Long id) {
        return acquirerMerchantRepository.findByAcquirerMerchantId(id);
    }

    @Cacheable(value = "acquirerMerchant")
    @Transactional(readOnly=true)
    public Optional<List<AcquirerMerchant>> findByAcquirerId(Long acquirerId) {
        return acquirerMerchantRepository.findByAcquirerId(acquirerId);
    }


    @Cacheable(value = "acquirerMerchant")
    @Transactional(readOnly=true)
    public Optional<AcquirerMerchant> findByMerchantId(Long merchantId) {
        return acquirerMerchantRepository.findByMerchantId(merchantId);
    }


    @Cacheable(value = "acquirerMerchant", key = "#root.target.ALL_ACQUIRER_MERCHANT")
    @Transactional(readOnly=true)
    public Optional<List<AcquirerMerchant>> findAll() {
        return Optional.ofNullable(acquirerMerchantRepository.findAll());
    }


}

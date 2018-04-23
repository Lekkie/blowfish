package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.Merchant;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.MerchantRepository;
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
public class MerchantService {


    public static final String ALL_MERCHANT = "ALL_MERCHANT";
    public static final String ACTIVE_MERCHANT = "ACTIVE_MERCHANT";


    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    StringService stringService;

    @CachePut(cacheNames="merchant", key = "#merchant.merchantId")
    @Transactional(readOnly=false)
    public Optional<Merchant> create(Merchant merchant) {
        return Optional.ofNullable(merchantRepository.save(merchant));
    }

    @CachePut(cacheNames="merchant", unless="#result==null", key = "#merchant.merchantId")
    @Transactional(readOnly=false)
    public Optional<Merchant> update(Merchant newMerchant) {
        Merchant oldMerchant = merchantRepository.findByMerchantId(newMerchant.getMerchantId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Merchant"));

        if(!stringService.isEmpty(newMerchant.getDescription()))
            oldMerchant.setDescription(newMerchant.getDescription());
        if(!stringService.isEmpty(newMerchant.getAddress()))
            oldMerchant.setAddress(newMerchant.getAddress());
        if(!stringService.isEmpty(newMerchant.getName()))
            oldMerchant.setName(newMerchant.getName());
        if(!stringService.isEmpty(newMerchant.getCode()))
            oldMerchant.setCode(newMerchant.getCode());
        if(!stringService.isEmpty(newMerchant.getName()))
            oldMerchant.setName(newMerchant.getName());
        if(!stringService.isEmpty(newMerchant.getPhoneNo()))
            oldMerchant.setPhoneNo(newMerchant.getPhoneNo());
        if(newMerchant.getDomainId() != 0)
            oldMerchant.setDomainId(newMerchant.getDomainId());
        if(!stringService.isEmpty(newMerchant.getCreatedBy()))
            oldMerchant.setCreatedBy(newMerchant.getCreatedBy());
        if(newMerchant.getCreatedOn() != null)
            oldMerchant.setCreatedOn(newMerchant.getCreatedOn());
        oldMerchant.setStatus(newMerchant.getStatus());
        return Optional.ofNullable(merchantRepository.save(oldMerchant));
    }

    @CacheEvict(value = "merchant", key = "#merchantId")
    @Transactional(readOnly=false)
    public void delete(long merchantId) {
        merchantRepository.delete(merchantId);
    }


    @Cacheable(value = "merchant", key = "#merchantId")
    @Transactional(readOnly=true)
    public Optional<Merchant> findByMerchantId(Long merchantId) {
        return merchantRepository.findByMerchantId(merchantId);
    }

    @Cacheable(value = "merchant", key = "#merchantCode")
    @Transactional(readOnly=true)
    public Optional<Merchant> findByCode(String merchantCode) {
        return merchantRepository.findByCodeAllIgnoringCase(merchantCode);
    }


    @Cacheable(value = "merchant", key = "#root.target.ACTIVE_MERCHANT")
    @Transactional(readOnly=true)
    public Optional<List<Merchant>> findAllActive() {
        return merchantRepository.findByStatus(1);
    }


    @Cacheable(value = "merchant", key = "#root.target.ALL_MERCHANT")
    @Transactional(readOnly=true)
    public Optional<List<Merchant>> findAll() {
        return Optional.ofNullable(merchantRepository.findAll());
    }


}

package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Merchant;
import com.avantir.blowfish.repository.MerchantRepository;
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
public class MerchantService {


    public static final String ALL_MERCHANT = "ALL_MERCHANT";
    public static final String ACTIVE_MERCHANT = "ACTIVE_MERCHANT";


    @Autowired
    private MerchantRepository merchantRepository;

    @CachePut(cacheNames="merchant", key = "#merchant.merchantId")
    @Transactional(readOnly=false)
    public Merchant create(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    @CachePut(cacheNames="merchant", unless="#result==null", key = "#merchant.merchantId")
    @Transactional(readOnly=false)
    public Merchant update(Merchant newMerchant) {
        if(newMerchant != null){
            Merchant oldMerchant = merchantRepository.findByMerchantId(newMerchant.getMerchantId());
            if(!StringUtil.isEmpty(newMerchant.getDescription()))
                oldMerchant.setDescription(newMerchant.getDescription());
            if(!StringUtil.isEmpty(newMerchant.getAddress()))
                oldMerchant.setAddress(newMerchant.getAddress());
            if(!StringUtil.isEmpty(newMerchant.getName()))
                oldMerchant.setName(newMerchant.getName());
            if(!StringUtil.isEmpty(newMerchant.getCode()))
                oldMerchant.setCode(newMerchant.getCode());
            if(!StringUtil.isEmpty(newMerchant.getName()))
                oldMerchant.setName(newMerchant.getName());
            if(!StringUtil.isEmpty(newMerchant.getPhoneNo()))
                oldMerchant.setPhoneNo(newMerchant.getPhoneNo());
            if(newMerchant.getDomainId() != 0)
                oldMerchant.setDomainId(newMerchant.getDomainId());
            if(!StringUtil.isEmpty(newMerchant.getCreatedBy()))
                oldMerchant.setCreatedBy(newMerchant.getCreatedBy());
            if(newMerchant.getCreatedOn() != null)
                oldMerchant.setCreatedOn(newMerchant.getCreatedOn());
            oldMerchant.setStatus(newMerchant.getStatus());
            return merchantRepository.save(oldMerchant);
        }
        return null;
    }

    @CacheEvict(value = "merchant", key = "#merchantId")
    @Transactional(readOnly=false)
    public void delete(long merchantId) {
        merchantRepository.delete(merchantId);
    }


    @Cacheable(value = "merchant", key = "#merchantId")
    @Transactional(readOnly=true)
    public Merchant findByMerchantId(Long merchantId) {

        try
        {
            return merchantRepository.findByMerchantId(merchantId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "merchant", key = "#merchantCode")
    @Transactional(readOnly=true)
    public Merchant findByCode(String merchantCode) {

        try
        {
            return merchantRepository.findByCodeAllIgnoringCase(merchantCode);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "merchant", key = "#root.target.ACTIVE_MERCHANT")
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


    @Cacheable(value = "merchant", key = "#root.target.ALL_MERCHANT")
    @Transactional(readOnly=true)
    public List<Merchant> findAll() {

        try
        {
            List<Merchant> list = merchantRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}

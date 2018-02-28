package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.Merchant;
import com.avantir.blowfish.model.TerminalParameter;
import com.avantir.blowfish.repository.AcquirerRepository;
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

    @CachePut(cacheNames="merchant")
    @Transactional(readOnly=false)
    public Merchant create(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    @CachePut(cacheNames="merchant", unless="#result==null")
    @Transactional(readOnly=false)
    public Merchant update(Merchant newMerchant) {
        if(newMerchant != null){
            Merchant oldMerchant = merchantRepository.findById(newMerchant.getId());
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

    @CacheEvict(value = "merchant")
    @Transactional(readOnly=false)
    public void delete(long id) {
        merchantRepository.delete(id);
    }


    @Cacheable(value = "merchant")
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

    @Cacheable(value = "merchant")
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


    @Cacheable(value = "merchants", key = "#root.target.ALL_MERCHANT")
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


    @Cacheable(value = "merchants", key = "#root.target.ACTIVE_MERCHANT")
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

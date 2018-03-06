package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.*;
import com.avantir.blowfish.repository.*;
import com.avantir.blowfish.utils.BlowfishUtil;
import com.avantir.blowfish.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class MerchantTerminalService {

    public static final String ID_MERCHANT_TERMINAL = "ID_MERCHANT_TERMINAL";
    public static final String ALL_MERCHANT_TERMINAL = "ALL_MERCHANT_TERMINAL";
    public static final String ACTIVE_MERCHANT_TERMINAL = "ACTIVE_MERCHANT_TERMINAL";

    @Autowired
    private MerchantTerminalRepository merchantTerminalRepository;

    // , key = "#result.firstName + #result.lastName"
    @Transactional(readOnly=false)
    public MerchantTerminal create(MerchantTerminal merchantTerminal) {
        MerchantTerminal merchantTerminal1 = merchantTerminalRepository.save(merchantTerminal);
        evictAllCache();
        return merchantTerminal1;
    }

    //@CachePut(cacheNames="merchantTerminal", unless="#result==null", key = "#result.id")
    @Transactional(readOnly=false)
    public MerchantTerminal update(MerchantTerminal newMerchantTerminal) {
        if(newMerchantTerminal != null){
            MerchantTerminal oldMerchantTerminal = merchantTerminalRepository.findById(newMerchantTerminal.getId());
            if(newMerchantTerminal.getMerchantId() != 0)
                oldMerchantTerminal.setMerchantId(newMerchantTerminal.getMerchantId());
            if(newMerchantTerminal.getTerminalId() != 0)
                oldMerchantTerminal.setTerminalId(newMerchantTerminal.getTerminalId());
            if(!StringUtil.isEmpty(newMerchantTerminal.getCreatedBy()))
                oldMerchantTerminal.setCreatedBy(newMerchantTerminal.getCreatedBy());
            if(newMerchantTerminal.getCreatedOn() != null)
                oldMerchantTerminal.setCreatedOn(newMerchantTerminal.getCreatedOn());
            MerchantTerminal merchantTerminal1 = merchantTerminalRepository.save(oldMerchantTerminal);
            evictAllCache();
            return oldMerchantTerminal;
        }
        return null;
    }


    @CacheEvict(value = "merchantTerminal")
    @Transactional(readOnly=false)
    public void delete(long id) {
        merchantTerminalRepository.delete(id);
    }



    @Cacheable(value = "merchantTerminal")
    @Transactional(readOnly=true)
    public MerchantTerminal findById(Long id) {

        try
        {
            return merchantTerminalRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "merchantTerminal")
    @Transactional(readOnly=true)
    public MerchantTerminal findByTerminalId(Long terminalId) {

        try
        {
            return merchantTerminalRepository.findByTerminalId(terminalId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "merchantTerminals")
    @Transactional(readOnly=true)
    public List<MerchantTerminal> findByMerchantId(Long merchantId) {

        try
        {
            return merchantTerminalRepository.findByMerchantId(merchantId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "merchantTerminals", key = "#root.target.ALL_MERCHANT_TERMINAL")
    @Transactional(readOnly=true)
    public List<MerchantTerminal> findAll() {

        try
        {
            List<MerchantTerminal> list = merchantTerminalRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @CacheEvict(value="merchantTerminals",allEntries=true)
    public void evictAllCache() {
        //LogUtil.log("Evicting all entries from fooCache.");
    }

}

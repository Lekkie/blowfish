package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.*;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
public class MerchantTerminalService {

    public static final String ID_MERCHANT_TERMINAL = "ID_MERCHANT_TERMINAL";
    public static final String ALL_MERCHANT_TERMINAL = "ALL_MERCHANT_TERMINAL";
    public static final String ACTIVE_MERCHANT_TERMINAL = "ACTIVE_MERCHANT_TERMINAL";

    @Autowired
    private MerchantTerminalRepository merchantTerminalRepository;
    @Autowired
    StringService stringService;

    // , key = "#result.firstName + #result.lastName"
    @Transactional(readOnly=false)
    public Optional<MerchantTerminal> create(MerchantTerminal merchantTerminal) {
        return Optional.ofNullable(merchantTerminalRepository.save(merchantTerminal));
    }

    //@CachePut(cacheNames="merchantTerminal", unless="#result==null", key = "#result.id")
    @Transactional(readOnly=false)
    public Optional<MerchantTerminal> update(MerchantTerminal newMerchantTerminal) {
        MerchantTerminal oldMerchantTerminal = merchantTerminalRepository.findByMerchantTerminalId(newMerchantTerminal.getMerchantTerminalId()).orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantTerminal"));

        if(newMerchantTerminal.getMerchantId() > 0)
            oldMerchantTerminal.setMerchantId(newMerchantTerminal.getMerchantId());
        if(newMerchantTerminal.getTerminalId() > 0)
            oldMerchantTerminal.setTerminalId(newMerchantTerminal.getTerminalId());
        if(!stringService.isEmpty(newMerchantTerminal.getCreatedBy()))
            oldMerchantTerminal.setCreatedBy(newMerchantTerminal.getCreatedBy());
        if(newMerchantTerminal.getCreatedOn() != null)
            oldMerchantTerminal.setCreatedOn(newMerchantTerminal.getCreatedOn());
        return Optional.ofNullable(merchantTerminalRepository.save(oldMerchantTerminal));
    }


    @CacheEvict(value = "merchantTerminal")
    @Transactional(readOnly=false)
    public void delete(long id) {
        merchantTerminalRepository.delete(id);
    }



    @Cacheable(value = "merchantTerminal")
    @Transactional(readOnly=true)
    public Optional<MerchantTerminal> findByMerchantTerminalId(Long id) {
        return merchantTerminalRepository.findByMerchantTerminalId(id);
    }

    @Cacheable(value = "merchantTerminal")
    @Transactional(readOnly=true)
    public Optional<MerchantTerminal> findByTerminalId(long terminalId) {
        return merchantTerminalRepository.findByTerminalId(terminalId);
    }

    @Cacheable(value = "merchantTerminal")
    @Transactional(readOnly=true)
    public Optional<List<MerchantTerminal>> findByMerchantId(long merchantId) {
        return merchantTerminalRepository.findByMerchantId(merchantId);
    }

    @Cacheable(value = "merchantTerminal", key = "#root.target.ALL_MERCHANT_TERMINAL")
    @Transactional(readOnly=true)
    public Optional<List<MerchantTerminal>> findAll() {
        return Optional.ofNullable(merchantTerminalRepository.findAll());
    }

}

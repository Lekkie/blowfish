package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.MerchantTermParam;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.MerchantTermParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
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
public class MerchantTermParamService {

    public static final String ALL_MERCH_TERM_PARAM = "ALL_MERCH_TERM_PARAM";
    public static final String ACTIVE_MERCH_TERM_PARAM = "ACTIVE_MERCH_TERM_PARAM";

    @Autowired
    private MerchantTermParamRepository merchantTerminalParameterRepository;

    @CachePut(cacheNames="merchTermParam")
    @Transactional(readOnly=false)
    public Optional<MerchantTermParam> create(MerchantTermParam merchantTermParam) {
        return Optional.ofNullable(merchantTerminalParameterRepository.save(merchantTermParam));
    }


    @CachePut(cacheNames="merchTermParam")
    @Transactional(readOnly=false)
    public Optional<MerchantTermParam> update(MerchantTermParam newMerchantTermParam) {
        MerchantTermParam oldMerchantTermParam = merchantTerminalParameterRepository.findByMerchantTermParamId(newMerchantTermParam.getMerchantTermParamId()).orElseThrow(() -> new BlowfishEntityNotFoundException("MerchantTermParam"));

        if(newMerchantTermParam.getMerchantId() != 0)
            oldMerchantTermParam.setMerchantId(newMerchantTermParam.getMerchantId());
        if(newMerchantTermParam.getTermParamId() != 0)
            oldMerchantTermParam.setTermParamId(newMerchantTermParam.getTermParamId());
        return Optional.ofNullable(merchantTerminalParameterRepository.save(oldMerchantTermParam));
    }


    @CacheEvict(value = "merchTermParam")
    @Transactional(readOnly=false)
    public void delete(long id) {
        merchantTerminalParameterRepository.delete(id);
    }


    @Cacheable(value = "merchTermParam")
    @Transactional(readOnly=true)
    public Optional<MerchantTermParam> findByMerchantTermParamId(Long merchantTermParamId) {
        return merchantTerminalParameterRepository.findByMerchantTermParamId(merchantTermParamId);
    }

    @Cacheable(value = "merchTermParam")
    @Transactional(readOnly=true)
    public Optional<MerchantTermParam> findByMerchantId(Long merchantId) {
        return merchantTerminalParameterRepository.findByMerchantId(merchantId);
    }

    @Cacheable(value = "merchTermParam")
    @Transactional(readOnly=true)
    public Optional<List<MerchantTermParam>> findByTermParamId(Long termParamId) {
        return merchantTerminalParameterRepository.findByTermParamId(termParamId);
    }



    @Cacheable(value = "merchTermParam", key = "#root.target.ALL_MERCH_TERM_PARAM")
    @Transactional(readOnly=true)
    public Optional<List<MerchantTermParam>> findAll() {
        return Optional.ofNullable(merchantTerminalParameterRepository.findAll());
    }

}

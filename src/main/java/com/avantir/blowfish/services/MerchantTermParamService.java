package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.MerchantTermParam;
import com.avantir.blowfish.repository.MerchantTermParamRepository;
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
public class MerchantTermParamService {

    public static final String ALL_MERCH_TERM_PARAM = "ALL_MERCH_TERM_PARAM";
    public static final String ACTIVE_MERCH_TERM_PARAM = "ACTIVE_MERCH_TERM_PARAM";

    @Autowired
    private MerchantTermParamRepository merchantTerminalParameterRepository;

    @CachePut(cacheNames="merchTermParam")
    @Transactional(readOnly=false)
    public MerchantTermParam create(MerchantTermParam merchantTermParam) {
        return merchantTerminalParameterRepository.save(merchantTermParam);
    }


    @CachePut(cacheNames="merchTermParam")
    @Transactional(readOnly=false)
    public MerchantTermParam update(MerchantTermParam newMerchantTermParam) {
        if(newMerchantTermParam != null){
            MerchantTermParam oldMerchantTermParam = merchantTerminalParameterRepository.findByMerchantTermParamId(newMerchantTermParam.getMerchantTermParamId());
            if(newMerchantTermParam.getMerchantId() != 0)
                oldMerchantTermParam.setMerchantId(newMerchantTermParam.getMerchantId());
            if(newMerchantTermParam.getTermParamId() != 0)
                oldMerchantTermParam.setTermParamId(newMerchantTermParam.getTermParamId());
            return merchantTerminalParameterRepository.save(oldMerchantTermParam);
        }
        return null;
    }


    @CacheEvict(value = "merchTermParam")
    @Transactional(readOnly=false)
    public void delete(long id) {
        merchantTerminalParameterRepository.delete(id);
    }


    @Cacheable(value = "merchTermParam")
    @Transactional(readOnly=true)
    public MerchantTermParam findByMerchantTermParamId(Long merchantTermParamId) {

        try
        {
            //Optional<AcquirerMerchant> optional = acquirerMerchantRepository.findById(id);
            //return optional.orElse(null);
            return merchantTerminalParameterRepository.findByMerchantTermParamId(merchantTermParamId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "merchTermParam")
    @Transactional(readOnly=true)
    public MerchantTermParam findByMerchantId(Long merchantId) {

        try
        {
            return merchantTerminalParameterRepository.findByMerchantId(merchantId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "merchTermParam")
    @Transactional(readOnly=true)
    public List<MerchantTermParam> findByTermParamId(Long termParamId) {

        try
        {
            return merchantTerminalParameterRepository.findByTermParamId(termParamId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



    @Cacheable(value = "merchTermParam", key = "#root.target.ALL_MERCH_TERM_PARAM")
    @Transactional(readOnly=true)
    public List<MerchantTermParam> findAll() {

        try
        {
            List<MerchantTermParam> list = merchantTerminalParameterRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

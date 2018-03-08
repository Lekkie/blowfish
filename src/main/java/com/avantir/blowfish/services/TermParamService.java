package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.TermParam;
import com.avantir.blowfish.repository.TermParamRepository;
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
public class TermParamService {


    public static final String ALL_TERM_PARAM = "ALL_TERM_PARAM";
    public static final String ACTIVE_TERM_PARAM = "ACTIVE_TERM_PARAM";

    @Autowired
    private TermParamRepository termParamRepository;


    @CachePut(cacheNames="termParam")
    @Transactional(readOnly=false)
    public TermParam create(TermParam termParam) {
        return termParamRepository.save(termParam);
    }


    @CachePut(cacheNames="termParam", unless="#result==null")
    @Transactional(readOnly=false)
    public TermParam update(TermParam newTermParam) {
        if(newTermParam != null){
            //Optional<Acquirer> optional = acquirerRepository.findById(newAcquirer.getId());
            //Acquirer oldAcquirer = optional.orElse(null);
            TermParam oldTermParam = termParamRepository.findByTermParamId(newTermParam.getTermParamId());
            if(!StringUtil.isEmpty(newTermParam.getDescription()))
                oldTermParam.setDescription(newTermParam.getDescription());
            if(!StringUtil.isEmpty(newTermParam.getIccData()))
                oldTermParam.setIccData(newTermParam.getIccData());
            if(!StringUtil.isEmpty(newTermParam.getName()))
                oldTermParam.setName(newTermParam.getName());
            if(!StringUtil.isEmpty(newTermParam.getPosDataCode()))
                oldTermParam.setPosDataCode(newTermParam.getPosDataCode());
            if(!StringUtil.isEmpty(newTermParam.getTerminalCapabilities()))
                oldTermParam.setTerminalCapabilities(newTermParam.getTerminalCapabilities());
            if(!StringUtil.isEmpty(newTermParam.getTerminalExtraCapabilities()))
                oldTermParam.setTerminalExtraCapabilities(newTermParam.getTerminalExtraCapabilities());
            if(!StringUtil.isEmpty(newTermParam.getTransactionCurrency()))
                oldTermParam.setTransactionCurrency(newTermParam.getTransactionCurrency());
            if(!StringUtil.isEmpty(newTermParam.getTransactionReferenceCurrency()))
                oldTermParam.setTransactionReferenceCurrency(newTermParam.getTransactionReferenceCurrency());
            if(newTermParam.getTransactionCurrencyExponent() != 0)
                oldTermParam.setTransactionCurrencyExponent(newTermParam.getTransactionCurrencyExponent());
            if(newTermParam.getReferenceCurrencyExponent() != 0)
                oldTermParam.setReferenceCurrencyExponent(newTermParam.getReferenceCurrencyExponent());
            if(newTermParam.getReferenceCurrencyConversion() != 0)
                oldTermParam.setReferenceCurrencyConversion(newTermParam.getReferenceCurrencyConversion());
            if(newTermParam.getBdkKeyId() != 0)
                oldTermParam.setBdkKeyId(newTermParam.getBdkKeyId());
            if(newTermParam.getCtmkKeyId() != 0)
                oldTermParam.setCtmkKeyId(newTermParam.getCtmkKeyId());
            if(newTermParam.getKeyDownloadIntervalInMin() != 0)
                oldTermParam.setKeyDownloadIntervalInMin(newTermParam.getKeyDownloadIntervalInMin());
            if(newTermParam.getKeyDownloadTimeInMin() != 0)
                oldTermParam.setKeyDownloadTimeInMin(newTermParam.getKeyDownloadTimeInMin());
            if(newTermParam.getTerminalType() != 0)
                oldTermParam.setTerminalType(newTermParam.getTerminalType());
            if(newTermParam.getTmsEndpointId() != 0)
                oldTermParam.setTmsEndpointId(newTermParam.getTmsEndpointId());
            if(!StringUtil.isEmpty(newTermParam.getCreatedBy()))
                oldTermParam.setCreatedBy(newTermParam.getCreatedBy());
            if(newTermParam.getCreatedOn() != null)
                oldTermParam.setCreatedOn(newTermParam.getCreatedOn());
            oldTermParam.setForceOnline(newTermParam.isForceOnline());
            oldTermParam.setSupportDefaultDDOL(newTermParam.isSupportDefaultDDOL());
            oldTermParam.setSupportDefaultTDOL(newTermParam.isSupportDefaultTDOL());
            oldTermParam.setSupportPSESelection(newTermParam.isSupportPSESelection());
            oldTermParam.setStatus(newTermParam.getStatus());
            return termParamRepository.save(oldTermParam);
        }
        return null;
    }

    @CacheEvict(value = "termParam")
    @Transactional(readOnly=false)
    public void delete(long id) {
        termParamRepository.delete(id);
    }


    @Cacheable(value = "termParam")
    @Transactional(readOnly=true)
    public TermParam findByTermParamId(Long id) {

        try
        {
            return termParamRepository.findByTermParamId(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "termParam")
    @Transactional(readOnly=true)
    public TermParam findByName(String name) {

        try
        {
            return termParamRepository.findByNameAllIgnoringCase(name);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



    @Cacheable(value = "termParams", key = "#root.target.ACTIVE_TERM_PARAM")
    @Transactional(readOnly=true)
    public List<TermParam> findAllActive() {

        try
        {
            List<TermParam> list = termParamRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "termParams", key = "#root.target.ALL_TERM_PARAM")
    @Transactional(readOnly=true)
    public List<TermParam> findAll() {

        try
        {
            List<TermParam> list = termParamRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}

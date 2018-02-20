package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.TerminalParameter;
import com.avantir.blowfish.repository.TerminalParameterRepository;
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
public class TerminalParameterService {


    public static final String ALL = "all";
    public static final String ACTIVE = "active";

    @Autowired
    private TerminalParameterRepository terminalParameterRepository;


    @CachePut(cacheNames="termParam")
    @Transactional(readOnly=false)
    public TerminalParameter create(TerminalParameter terminalParameter) {
        return terminalParameterRepository.save(terminalParameter);
    }


    @CachePut(cacheNames="termParam", unless="#result==null")
    @Transactional(readOnly=false)
    public TerminalParameter update(TerminalParameter newTerminalParameter) {
        if(newTerminalParameter != null){
            //Optional<Acquirer> optional = acquirerRepository.findById(newAcquirer.getId());
            //Acquirer oldAcquirer = optional.orElse(null);
            TerminalParameter oldTerminalParameter = terminalParameterRepository.findById(newTerminalParameter.getId());
            if(!StringUtil.isEmpty(newTerminalParameter.getDescription()))
                oldTerminalParameter.setDescription(newTerminalParameter.getDescription());
            if(!StringUtil.isEmpty(newTerminalParameter.getIccData()))
                oldTerminalParameter.setIccData(newTerminalParameter.getIccData());
            if(!StringUtil.isEmpty(newTerminalParameter.getName()))
                oldTerminalParameter.setName(newTerminalParameter.getName());
            if(!StringUtil.isEmpty(newTerminalParameter.getPosDataCode()))
                oldTerminalParameter.setPosDataCode(newTerminalParameter.getPosDataCode());
            if(!StringUtil.isEmpty(newTerminalParameter.getTerminalCapabilities()))
                oldTerminalParameter.setTerminalCapabilities(newTerminalParameter.getTerminalCapabilities());
            if(!StringUtil.isEmpty(newTerminalParameter.getTerminalExtraCapabilities()))
                oldTerminalParameter.setTerminalExtraCapabilities(newTerminalParameter.getTerminalExtraCapabilities());
            if(!StringUtil.isEmpty(newTerminalParameter.getTransactionCurrency()))
                oldTerminalParameter.setTransactionCurrency(newTerminalParameter.getTransactionCurrency());
            if(!StringUtil.isEmpty(newTerminalParameter.getTransactionReferenceCurrency()))
                oldTerminalParameter.setTransactionReferenceCurrency(newTerminalParameter.getTransactionReferenceCurrency());
            if(newTerminalParameter.getTransactionCurrencyExponent() != 0)
                oldTerminalParameter.setTransactionCurrencyExponent(newTerminalParameter.getTransactionCurrencyExponent());
            if(newTerminalParameter.getReferenceCurrencyExponent() != 0)
                oldTerminalParameter.setReferenceCurrencyExponent(newTerminalParameter.getReferenceCurrencyExponent());
            if(newTerminalParameter.getReferenceCurrencyConversion() != 0)
                oldTerminalParameter.setReferenceCurrencyConversion(newTerminalParameter.getReferenceCurrencyConversion());
            if(newTerminalParameter.getBdkKeyId() != 0)
                oldTerminalParameter.setBdkKeyId(newTerminalParameter.getBdkKeyId());
            if(newTerminalParameter.getCtmkKeyId() != 0)
                oldTerminalParameter.setCtmkKeyId(newTerminalParameter.getCtmkKeyId());
            if(newTerminalParameter.getKeyDownloadIntervalInMin() != 0)
                oldTerminalParameter.setKeyDownloadIntervalInMin(newTerminalParameter.getKeyDownloadIntervalInMin());
            if(newTerminalParameter.getKeyDownloadTimeInMin() != 0)
                oldTerminalParameter.setKeyDownloadTimeInMin(newTerminalParameter.getKeyDownloadTimeInMin());
            if(newTerminalParameter.getTerminalType() != 0)
                oldTerminalParameter.setTerminalType(newTerminalParameter.getTerminalType());
            if(newTerminalParameter.getTmsEndpointId() != 0)
                oldTerminalParameter.setTmsEndpointId(newTerminalParameter.getTmsEndpointId());
            oldTerminalParameter.setForceOnline(newTerminalParameter.isForceOnline());
            oldTerminalParameter.setSupportDefaultDDOL(newTerminalParameter.isSupportDefaultDDOL());
            oldTerminalParameter.setSupportDefaultTDOL(newTerminalParameter.isSupportDefaultTDOL());
            oldTerminalParameter.setSupportPSESelection(newTerminalParameter.isSupportPSESelection());
            oldTerminalParameter.setStatus(newTerminalParameter.getStatus());
            return terminalParameterRepository.save(oldTerminalParameter);
        }
        return null;
    }

    @CacheEvict(value = "termParam")
    @Transactional(readOnly=false)
    public void delete(long id) {
        terminalParameterRepository.delete(id);
    }


    @Cacheable(value = "termParam")
    @Transactional(readOnly=true)
    public TerminalParameter findById(Long id) {

        try
        {
            return terminalParameterRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "termParam")
    @Transactional(readOnly=true)
    public TerminalParameter findByName(String name) {

        try
        {
            return terminalParameterRepository.findByNameAllIgnoringCase(name);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



    @Cacheable(value = "termParams", key = "#root.target.ACTIVE")
    @Transactional(readOnly=true)
    public List<TerminalParameter> findAllActive() {

        try
        {
            List<TerminalParameter> list = terminalParameterRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "termParams", key = "#root.target.ALL")
    @Transactional(readOnly=true)
    public List<TerminalParameter> findAll() {

        try
        {
            List<TerminalParameter> list = terminalParameterRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}

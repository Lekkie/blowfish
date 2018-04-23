package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.TermParam;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.TermParamRepository;
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
public class TermParamService {


    public static final String ALL_TERM_PARAM = "ALL_TERM_PARAM";
    public static final String ACTIVE_TERM_PARAM = "ACTIVE_TERM_PARAM";

    @Autowired
    private TermParamRepository termParamRepository;
    @Autowired
    StringService stringService;


    @CachePut(cacheNames="termParam")
    @Transactional(readOnly=false)
    public Optional<TermParam> create(TermParam termParam) {
        return Optional.ofNullable(termParamRepository.save(termParam));
    }


    @CachePut(cacheNames="termParam", unless="#result==null")
    @Transactional(readOnly=false)
    public Optional<TermParam> update(TermParam newTermParam) {

        Optional<TermParam> optional = termParamRepository.findByTermParamId(newTermParam.getTermParamId());
        TermParam oldTermParam = optional.orElseThrow(() -> new BlowfishEntityNotFoundException("TermParam"));

        if(!stringService.isEmpty(newTermParam.getDescription()))
            oldTermParam.setDescription(newTermParam.getDescription());
        if(!stringService.isEmpty(newTermParam.getIccData()))
            oldTermParam.setIccData(newTermParam.getIccData());
        if(!stringService.isEmpty(newTermParam.getName()))
            oldTermParam.setName(newTermParam.getName());
        if(!stringService.isEmpty(newTermParam.getPosDataCode()))
            oldTermParam.setPosDataCode(newTermParam.getPosDataCode());
        if(!stringService.isEmpty(newTermParam.getTerminalCapabilities()))
            oldTermParam.setTerminalCapabilities(newTermParam.getTerminalCapabilities());
        if(!stringService.isEmpty(newTermParam.getTerminalExtraCapabilities()))
            oldTermParam.setTerminalExtraCapabilities(newTermParam.getTerminalExtraCapabilities());
        if(!stringService.isEmpty(newTermParam.getTransactionCurrency()))
            oldTermParam.setTransactionCurrency(newTermParam.getTransactionCurrency());
        if(!stringService.isEmpty(newTermParam.getTransactionReferenceCurrency()))
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
        if(!stringService.isEmpty(newTermParam.getCreatedBy()))
            oldTermParam.setCreatedBy(newTermParam.getCreatedBy());
        if(newTermParam.getCreatedOn() != null)
            oldTermParam.setCreatedOn(newTermParam.getCreatedOn());
        oldTermParam.setForceOnline(newTermParam.isForceOnline());
        oldTermParam.setSupportDefaultDDOL(newTermParam.isSupportDefaultDDOL());
        oldTermParam.setSupportDefaultTDOL(newTermParam.isSupportDefaultTDOL());
        oldTermParam.setSupportPSESelection(newTermParam.isSupportPSESelection());
        oldTermParam.setStatus(newTermParam.getStatus());

        return Optional.ofNullable(termParamRepository.save(oldTermParam));
    }

    @CacheEvict(value = "termParam")
    @Transactional(readOnly=false)
    public void delete(long id) {
        termParamRepository.delete(id);
    }


    @Cacheable(value = "termParam")
    @Transactional(readOnly=true)
    public Optional<TermParam> findByTermParamId(Long id) {
        return termParamRepository.findByTermParamId(id);
    }


    @Cacheable(value = "termParam")
    @Transactional(readOnly=true)
    public Optional<TermParam> findByName(String name) {
        return termParamRepository.findByNameAllIgnoringCase(name);
    }



    @Cacheable(value = "termParam", key = "#root.target.ACTIVE_TERM_PARAM")
    @Transactional(readOnly=true)
    public Optional<List<TermParam>> findAllActive() {
        return termParamRepository.findByStatus(1);
    }



    @Cacheable(value = "termParam", key = "#root.target.ALL_TERM_PARAM")
    @Transactional(readOnly=true)
    public Optional<List<TermParam>> findAll() {
        return Optional.ofNullable(termParamRepository.findAll());
    }


}

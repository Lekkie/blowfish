package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Merchant;
import com.avantir.blowfish.model.Terminal;
import com.avantir.blowfish.repository.MerchantRepository;
import com.avantir.blowfish.repository.TerminalRepository;
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
public class TerminalService {


    public static final String ALL_TERMINAL = "ALL_TERMINAL";
    public static final String ACTIVE_TERMINAL = "ACTIVE_TERMINAL";


    @Autowired
    private TerminalRepository terminalRepository;

    @CachePut(cacheNames="terminal")
    @Transactional(readOnly=false)
    public Terminal create(Terminal terminal) {
        return terminalRepository.save(terminal);
    }


    @CachePut(cacheNames="terminal", unless="#result==null")
    @Transactional(readOnly=false)
    public Terminal update(Terminal newTerminal) {
        if(newTerminal != null){
            Terminal oldTerminal = terminalRepository.findById(newTerminal.getId());
            if(!StringUtil.isEmpty(newTerminal.getDescription()))
                oldTerminal.setDescription(newTerminal.getDescription());
            if(!StringUtil.isEmpty(newTerminal.getSerialNo()))
                oldTerminal.setSerialNo(newTerminal.getSerialNo());
            if(!StringUtil.isEmpty(newTerminal.getManufacturer()))
                oldTerminal.setManufacturer(newTerminal.getManufacturer());
            if(!StringUtil.isEmpty(newTerminal.getCode()))
                oldTerminal.setCode(newTerminal.getCode());
            if(!StringUtil.isEmpty(newTerminal.getModelNo()))
                oldTerminal.setModelNo(newTerminal.getModelNo());
            if(!StringUtil.isEmpty(newTerminal.getBuildNo()))
                oldTerminal.setBuildNo(newTerminal.getBuildNo());
            if(!StringUtil.isEmpty(newTerminal.getOs()))
                oldTerminal.setOs(newTerminal.getOs());
            if(!StringUtil.isEmpty(newTerminal.getOsVersion()))
                oldTerminal.setOsVersion(newTerminal.getOsVersion());
            if(!StringUtil.isEmpty(newTerminal.getFirmwareNo()))
                oldTerminal.setFirmwareNo(newTerminal.getFirmwareNo());
            if(!StringUtil.isEmpty(newTerminal.getCreatedBy()))
                oldTerminal.setCreatedBy(newTerminal.getCreatedBy());
            if(newTerminal.getCreatedOn() != null)
                oldTerminal.setCreatedOn(newTerminal.getCreatedOn());
            oldTerminal.setStatus(newTerminal.getStatus());
            return terminalRepository.save(oldTerminal);
        }
        return null;
    }

    @CacheEvict(value = "terminal")
    @Transactional(readOnly=false)
    public void delete(long id) {
        terminalRepository.delete(id);
    }


    @Cacheable(value = "terminal")
    @Transactional(readOnly=true)
    public Terminal findById(Long id) {

        try
        {
            return terminalRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "terminal")
    @Transactional(readOnly=true)
    public Terminal findByCode(String code) {

        try
        {
            return terminalRepository.findByCodeAllIgnoringCase(code);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "terminal")
    @Transactional(readOnly=true)
    public Terminal findBySerialNo(String serialNo) {

        try
        {
            return terminalRepository.findBySerialNoAllIgnoringCase(serialNo);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "terminals", key = "#root.target.ACTIVE_TERMINAL")
    @Transactional(readOnly=true)
    public List<Terminal> findAllActive() {

        try
        {
            List<Terminal> list = terminalRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



    @Cacheable(value = "terminals", key = "#root.target.ALL_TERMINAL")
    @Transactional(readOnly=true)
    public List<Terminal> findAll() {

        try
        {
            List<Terminal> list = terminalRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

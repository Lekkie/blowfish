package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.Terminal;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.TerminalRepository;
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
public class TerminalService {


    public static final String ALL_TERMINAL = "ALL_TERMINAL";
    public static final String ACTIVE_TERMINAL = "ACTIVE_TERMINAL";


    @Autowired
    private TerminalRepository terminalRepository;
    @Autowired
    StringService stringService;

    @CachePut(cacheNames="terminal")
    @Transactional(readOnly=false)
    public Optional<Terminal> create(Terminal terminal) {
        return Optional.ofNullable(terminalRepository.save(terminal));
    }


    @CachePut(cacheNames="terminal", unless="#result==null")
    @Transactional(readOnly=false)
    public Optional<Terminal> update(Terminal newTerminal) {
        Terminal oldTerminal = terminalRepository.findByTerminalId(newTerminal.getTerminalId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Terminal"));

        if(!stringService.isEmpty(newTerminal.getDescription()))
            oldTerminal.setDescription(newTerminal.getDescription());
        if(!stringService.isEmpty(newTerminal.getSerialNo()))
            oldTerminal.setSerialNo(newTerminal.getSerialNo());
        if(!stringService.isEmpty(newTerminal.getManufacturer()))
            oldTerminal.setManufacturer(newTerminal.getManufacturer());
        if(!stringService.isEmpty(newTerminal.getCode()))
            oldTerminal.setCode(newTerminal.getCode());
        if(!stringService.isEmpty(newTerminal.getModelNo()))
            oldTerminal.setModelNo(newTerminal.getModelNo());
        if(!stringService.isEmpty(newTerminal.getBuildNo()))
            oldTerminal.setBuildNo(newTerminal.getBuildNo());
        if(!stringService.isEmpty(newTerminal.getOs()))
            oldTerminal.setOs(newTerminal.getOs());
        if(!stringService.isEmpty(newTerminal.getOsVersion()))
            oldTerminal.setOsVersion(newTerminal.getOsVersion());
        if(!stringService.isEmpty(newTerminal.getFirmwareNo()))
            oldTerminal.setFirmwareNo(newTerminal.getFirmwareNo());
        if(!stringService.isEmpty(newTerminal.getCreatedBy()))
            oldTerminal.setCreatedBy(newTerminal.getCreatedBy());
        if(newTerminal.getCreatedOn() != null)
            oldTerminal.setCreatedOn(newTerminal.getCreatedOn());
        oldTerminal.setStatus(newTerminal.getStatus());
        return Optional.ofNullable(terminalRepository.save(oldTerminal));
    }

    @CacheEvict(value = "terminal")
    @Transactional(readOnly=false)
    public void delete(long id) {
        terminalRepository.delete(id);
    }


    @Cacheable(value = "terminal")
    @Transactional(readOnly=true)
    public Optional<Terminal> findByTerminalId(Long id) {
        return terminalRepository.findByTerminalId(id);
    }

    @Cacheable(value = "terminal")
    @Transactional(readOnly=true)
    public Optional<Terminal> findByCode(String code) {
        return terminalRepository.findByCodeAllIgnoringCase(code);
    }

    @Cacheable(value = "terminal")
    @Transactional(readOnly=true)
    public Optional<Terminal> findBySerialNo(String serialNo) {
        return terminalRepository.findBySerialNoAllIgnoringCase(serialNo);
    }


    @Cacheable(value = "terminal", key = "#root.target.ACTIVE_TERMINAL")
    @Transactional(readOnly=true)
    public Optional<List<Terminal>> findAllActive() {
        return terminalRepository.findByStatus(1);
    }



    @Cacheable(value = "terminal", key = "#root.target.ALL_TERMINAL")
    @Transactional(readOnly=true)
    public Optional<List<Terminal>> findAll() {
        return Optional.ofNullable(terminalRepository.findAll());
    }

}

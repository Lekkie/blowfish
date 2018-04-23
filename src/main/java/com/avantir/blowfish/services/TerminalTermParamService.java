package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.TerminalTermParam;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.TerminalTermParamRepository;
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
public class TerminalTermParamService {

    public static final String ALL_TERM_TERM_PARAM = "ALL_TERM_TERM_PARAM";
    public static final String ACTIVE_TERM_TERM_PARAM = "ACTIVE_TERM_TERM_PARAM";

    @Autowired
    private TerminalTermParamRepository terminalTerminalParameterRepository;


    @CachePut(cacheNames="termTermParam")
    @Transactional(readOnly=false)
    public Optional<TerminalTermParam> create(TerminalTermParam terminalTermParam) {
        return Optional.ofNullable(terminalTerminalParameterRepository.save(terminalTermParam));
    }


    @CachePut(cacheNames="termTermParam")
    @Transactional(readOnly=false)
    public Optional<TerminalTermParam> update(TerminalTermParam newTerminalTermParam) {
        TerminalTermParam oldTerminalTermParam = terminalTerminalParameterRepository.findByTerminalTermParamId(newTerminalTermParam.getTerminalTermParamId()).orElseThrow(() -> new BlowfishEntityNotFoundException("TerminalTermParam"));

        if(newTerminalTermParam.getTerminalId() != 0)
            oldTerminalTermParam.setTerminalId(newTerminalTermParam.getTerminalId());
        if(newTerminalTermParam.getTerminalTermParamId() != 0)
            oldTerminalTermParam.setTerminalTermParamId(newTerminalTermParam.getTerminalTermParamId());
        return Optional.ofNullable(terminalTerminalParameterRepository.save(oldTerminalTermParam));
    }



    @CacheEvict(value = "termTermParam")
    @Transactional(readOnly=false)
    public void delete(long id) {
        terminalTerminalParameterRepository.delete(id);
    }



    @Cacheable(value = "termTermParam")
    @Transactional(readOnly=true)
    public Optional<TerminalTermParam> findByTerminalTermParamId(Long terminalTermParamId) {
        return terminalTerminalParameterRepository.findByTerminalTermParamId(terminalTermParamId);
    }

    @Cacheable(value = "termTermParam")
    @Transactional(readOnly=true)
    public Optional<TerminalTermParam> findByTerminalId(Long terminalId) {
        return terminalTerminalParameterRepository.findByTerminalId(terminalId);
    }


    @Cacheable(value = "termTermParam")
    @Transactional(readOnly=true)
    public Optional<List<TerminalTermParam>> findByTermParamId(Long termParamId) {
        return terminalTerminalParameterRepository.findByTermParamId(termParamId);
    }

    @Cacheable(value = "termTermParam", key = "#root.target.ALL_TERM_TERM_PARAM")
    @Transactional(readOnly=true)
    public Optional<List<TerminalTermParam>> findAll() {
        return Optional.ofNullable(terminalTerminalParameterRepository.findAll());
    }

}

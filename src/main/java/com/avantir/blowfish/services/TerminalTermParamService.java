package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.TerminalTermParam;
import com.avantir.blowfish.repository.TerminalTermParamRepository;
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
public class TerminalTermParamService {

    public static final String ALL_TERM_TERM_PARAM = "ALL_TERM_TERM_PARAM";
    public static final String ACTIVE_TERM_TERM_PARAM = "ACTIVE_TERM_TERM_PARAM";

    @Autowired
    private TerminalTermParamRepository terminalTerminalParameterRepository;


    @CachePut(cacheNames="termTermParam")
    @Transactional(readOnly=false)
    public TerminalTermParam create(TerminalTermParam terminalTermParam) {
        return terminalTerminalParameterRepository.save(terminalTermParam);
    }


    @CachePut(cacheNames="termTermParam")
    @Transactional(readOnly=false)
    public TerminalTermParam update(TerminalTermParam newTerminalTermParam) {
        if(newTerminalTermParam != null){
            TerminalTermParam oldTerminalTermParam = terminalTerminalParameterRepository.findByTerminalTermParamId(newTerminalTermParam.getTerminalTermParamId());
            if(newTerminalTermParam.getTerminalId() != 0)
                oldTerminalTermParam.setTerminalId(newTerminalTermParam.getTerminalId());
            if(newTerminalTermParam.getTerminalTermParamId() != 0)
                oldTerminalTermParam.setTerminalTermParamId(newTerminalTermParam.getTerminalTermParamId());
            return terminalTerminalParameterRepository.save(oldTerminalTermParam);
        }
        return null;
    }



    @CacheEvict(value = "termTermParam")
    @Transactional(readOnly=false)
    public void delete(long id) {
        terminalTerminalParameterRepository.delete(id);
    }



    @Cacheable(value = "termTermParam")
    @Transactional(readOnly=true)
    public TerminalTermParam findByTerminalTermParamId(Long terminalTermParamId) {

        try
        {
            return terminalTerminalParameterRepository.findByTerminalTermParamId(terminalTermParamId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "termTermParam")
    @Transactional(readOnly=true)
    public TerminalTermParam findByTerminalId(Long terminalId) {

        try
        {
            return terminalTerminalParameterRepository.findByTerminalId(terminalId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "termTermParam")
    @Transactional(readOnly=true)
    public List<TerminalTermParam> findByTermParamId(Long termParamId) {

        try
        {
            return terminalTerminalParameterRepository.findByTermParamId(termParamId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "termTermParams", key = "#root.target.ALL_TERM_TERM_PARAM")
    @Transactional(readOnly=true)
    public List<TerminalTermParam> findAll() {

        try
        {
            List<TerminalTermParam> list = terminalTerminalParameterRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

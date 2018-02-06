package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Bin;
import com.avantir.blowfish.model.Domain;
import com.avantir.blowfish.model.TMSKeyOrigDataElem;
import com.avantir.blowfish.repository.BinRepository;
import com.avantir.blowfish.repository.DomainRepository;
import com.avantir.blowfish.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class DomainService {

    @Autowired
    private DomainRepository domainRepository;


    @CachePut(cacheNames="domain", key="#id")
    @Transactional(readOnly=false)
    public Domain create(Domain domain) {
        return domainRepository.save(domain);
    }

    @CachePut(cacheNames="domain", key="#id")
    @Transactional(readOnly=false)
    public Domain update(Domain newDomain) {
        if(newDomain != null){
            Optional<Domain> optional = domainRepository.findById(newDomain.getId());
            Domain oldDomain = optional.orElse(null);
            if(!StringUtil.isEmpty(newDomain.getName()))
                oldDomain.setName(newDomain.getName());
            if(!StringUtil.isEmpty(newDomain.getDescription()))
                oldDomain.setDescription(newDomain.getDescription());
            oldDomain.setStatus(newDomain.getStatus());
            return domainRepository.save(oldDomain);
        }
        return null;
    }

    @CacheEvict(value = "domain", key = "#id")
    @Transactional(readOnly=false)
    public void delete(Long id) {
        domainRepository.deleteById(id);
    }

    @Cacheable(value = "domain", key = "#id")
    @Transactional(readOnly=true)
    public Domain findById(Long id) {

        try
        {
            Optional<Domain> optional = domainRepository.findById(id);
            return optional.orElse(null);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "domains", key = "1")
    @Transactional(readOnly=true)
    public List<Domain> findAllActive() {

        try
        {
            List<Domain> list = domainRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

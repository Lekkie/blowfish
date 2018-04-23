package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.Domain;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.DomainRepository;
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
public class DomainService {

    public static final String ALL_DOMAIN = "ALL_DOMAIN";
    public static final String ACTIVE_DOMAINL = "ACTIVE_DOMAIN";

    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    StringService stringService;


    @CachePut(cacheNames="domain")
    @Transactional(readOnly=false)
    public Optional<Domain> create(Domain domain) {
        return Optional.ofNullable(domainRepository.save(domain));
    }

    @CachePut(cacheNames="domain")
    @Transactional(readOnly=false)
    public Optional<Domain> update(Domain newDomain) {

        Domain oldDomain = domainRepository.findByDomainId(newDomain.getDomainId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Domain"));

        if(!stringService.isEmpty(newDomain.getName()))
            oldDomain.setName(newDomain.getName());
        if(!stringService.isEmpty(newDomain.getDescription()))
            oldDomain.setDescription(newDomain.getDescription());
        oldDomain.setStatus(newDomain.getStatus());
        return Optional.ofNullable(domainRepository.save(oldDomain));
    }

    @CacheEvict(value = "domain")
    @Transactional(readOnly=false)
    public void delete(Long id) {
        domainRepository.delete(id);
    }

    @Cacheable(value = "domain")
    @Transactional(readOnly=true)
    public Optional<Domain> findByDomainId(Long id) {
        return domainRepository.findByDomainId(id);
    }

    @Cacheable(value = "domain")
    @Transactional(readOnly=true)
    public Optional<Domain> findByCode(String code) {
        return domainRepository.findOneByCode(code);
    }


    @Cacheable(value = "domain", key = "#root.target.ACTIVE_DOMAIN")
    @Transactional(readOnly=true)
    public Optional<List<Domain>> findAllActive() {
        return domainRepository.findByStatus(1);
    }

    @Cacheable(value = "domain", key = "#root.target.ALL_DOMAIN")
    @Transactional(readOnly=true)
    public Optional<List<Domain>> findAll() {
        return Optional.ofNullable(domainRepository.findAll());
    }

}

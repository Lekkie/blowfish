package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.Acquirer;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.exceptions.BlowfishIllegalArgumentException;
import com.avantir.blowfish.repository.AcquirerRepository;
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
public class AcquirerService {

    public static final String ALL_ACQUIRER = "ALL_ACQUIRER";
    public static final String ACTIVE_ACQUIRER = "ACTIVE_ACQUIRER";

    @Autowired
    private AcquirerRepository acquirerRepository;
    @Autowired
    StringService stringService;

    @CachePut(cacheNames="acquirer", key="#acquirer.acquirerId")
    @Transactional(readOnly=false)
    public Optional<Acquirer> create(Acquirer acquirer) {
        return Optional.ofNullable(acquirerRepository.save(acquirer));
    }


    @CachePut(cacheNames="acquirer", key="#newAcquirer.acquirerId")
    @Transactional(readOnly=false)
    public Optional<Acquirer> update(Acquirer newAcquirer) {

        Acquirer oldAcquirer = acquirerRepository.findByAcquirerId(newAcquirer.getAcquirerId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Acquirer"));

        if(!stringService.isEmpty(newAcquirer.getName()))
            oldAcquirer.setName(newAcquirer.getName());
        if(!stringService.isEmpty(newAcquirer.getDescription()))
            oldAcquirer.setDescription(newAcquirer.getDescription());
        oldAcquirer.setStatus(newAcquirer.getStatus());
        return Optional.ofNullable(acquirerRepository.save(oldAcquirer));
    }

    @CacheEvict(value = "acquirer")
    @Transactional(readOnly=false)
    public void delete(long id) {
        acquirerRepository.delete(id);
    }


    @Cacheable(value = "acquirer")
    @Transactional(readOnly=true)
    public Optional<Acquirer> findByAcquirerId(Long id) {
        return acquirerRepository.findByAcquirerId(id);
    }

    @Cacheable(value = "acquirer")
    @Transactional(readOnly=true)
    public Optional<Acquirer> findByCode(String code) {
        return acquirerRepository.findByCodeAllIgnoringCase(code);
    }


    @Cacheable(value = "acquirer", key = "#root.target.ACTIVE_ACQUIRER")
    @Transactional(readOnly=true)
    public Optional<List<Acquirer>> findAllActive() {
        return acquirerRepository.findByStatus(1);
    }

    @Cacheable(value = "acquirer", key = "#root.target.ALL_ACQUIRER")
    @Transactional(readOnly=true)
    public Optional<List<Acquirer>> findAll() {
        return Optional.ofNullable(acquirerRepository.findAll());
    }

}

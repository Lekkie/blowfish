package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.AcquirerBin;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.AcquirerBinRepository;
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
public class AcquirerBinService {

    public static final String ALL_ACQUIRER_BIN = "ALL_ACQUIRER_BIN";
    public static final String ACTIVE_ACQUIRER_BIN = "ACTIVE_ACQUIRER_BIN";


    @Autowired
    private AcquirerBinRepository acquirerBinRepository;
    @Autowired
    StringService stringService;


    @CachePut(cacheNames="acquirerBin")
    // , key = "#result.firstName + #result.lastName"
    @Transactional(readOnly=false)
    public Optional<AcquirerBin> create(AcquirerBin acquirerMerchant) {
        return Optional.ofNullable(acquirerBinRepository.save(acquirerMerchant));
    }


    @CachePut(cacheNames="acquirerBin", unless="#result==null", key = "#result.id")
    @Transactional(readOnly=false)
    public Optional<AcquirerBin> update(AcquirerBin newAcquirerBin) {

        Optional<AcquirerBin> oldAcqBinOptional = acquirerBinRepository.findByAcquirerBinId(newAcquirerBin.getAcquirerBinId());
        AcquirerBin oldAcquirerBin = oldAcqBinOptional.orElseThrow(() -> new BlowfishEntityNotFoundException("AcquirerBin"));

        if(newAcquirerBin.getBinId() != 0)
            oldAcquirerBin.setBinId(newAcquirerBin.getBinId());
        if(newAcquirerBin.getAcquirerId() != 0)
            oldAcquirerBin.setAcquirerId(newAcquirerBin.getAcquirerId());
        if(!stringService.isEmpty(newAcquirerBin.getCreatedBy()))
            oldAcquirerBin.setCreatedBy(newAcquirerBin.getCreatedBy());
        if(newAcquirerBin.getCreatedOn() != null)
            oldAcquirerBin.setCreatedOn(newAcquirerBin.getCreatedOn());
        return  Optional.ofNullable(acquirerBinRepository.save(oldAcquirerBin));
    }


    @CacheEvict(value = "acquirerBin")
    @Transactional(readOnly=false)
    public void delete(long id) {
        acquirerBinRepository.delete(id);
    }


    @Cacheable(value = "acquirerBin")
    @Transactional(readOnly=true)
    public Optional<AcquirerBin> findByAcquirerBinId(Long acquirerBinId) {
        return acquirerBinRepository.findByAcquirerBinId(acquirerBinId);
    }


    @Cacheable(value = "acquirerBin")
    @Transactional(readOnly=true)
    public Optional<List<AcquirerBin>> findByAcquirerId(Long acquirerId) {
            return acquirerBinRepository.findByAcquirerId(acquirerId);
    }

    @Cacheable(value = "acquirerBin")
    @Transactional(readOnly=true)
    public Optional<AcquirerBin> findByBinId(Long binId) {
        return acquirerBinRepository.findByBinId(binId);
    }



    @Cacheable(value = "acquirerBin", key = "#root.target.ALL_ACQUIRER_BIN")
    @Transactional(readOnly=true)
    public Optional<List<AcquirerBin>> findAll() {
        return Optional.ofNullable(acquirerBinRepository.findAll());
    }

}

package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.Domain;
import com.avantir.blowfish.model.TranType;
import com.avantir.blowfish.repository.AcquirerRepository;
import com.avantir.blowfish.repository.TranTypeRepository;
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
public class AcquirerService {

    @Autowired
    private AcquirerRepository acquirerRepository;

    @CachePut(cacheNames="acquirer", key="#acquirer.id")
    @Transactional(readOnly=false)
    public Acquirer create(Acquirer acquirer) {
        return acquirerRepository.save(acquirer);
    }


    @CachePut(cacheNames="acquirer", key="#newAcquirer.id")
    @Transactional(readOnly=false)
    public Acquirer update(Acquirer newAcquirer) {
        if(newAcquirer != null){
            //Optional<Acquirer> optional = acquirerRepository.findById(newAcquirer.getId());
            //Acquirer oldAcquirer = optional.orElse(null);
            Acquirer oldAcquirer = acquirerRepository.findById(newAcquirer.getId());
            if(!StringUtil.isEmpty(newAcquirer.getName()))
                oldAcquirer.setName(newAcquirer.getName());
            if(!StringUtil.isEmpty(newAcquirer.getDescription()))
                oldAcquirer.setDescription(newAcquirer.getDescription());
            oldAcquirer.setStatus(newAcquirer.getStatus());
            return acquirerRepository.save(oldAcquirer);
        }
        return null;
    }

    @CacheEvict(value = "acquirer", key = "#id")
    @Transactional(readOnly=false)
    public void delete(long id) {
        acquirerRepository.delete(id);
    }


    @Cacheable(value = "acquirer", key = "#id")
    @Transactional(readOnly=true)
    public Acquirer findById(Long id) {

        try
        {
            //Optional<Acquirer> optional = acquirerRepository.findById(id);
            //return optional.orElse(null);
            return acquirerRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "acquirer", key = "#code")
    @Transactional(readOnly=true)
    public Acquirer findByCode(String code) {

        try
        {
            return acquirerRepository.findByCodeAllIgnoringCase(code);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "acquirers", key = "1")
    @Transactional(readOnly=true)
    public List<Acquirer> findAllActive() {

        try
        {
            List<Acquirer> list = acquirerRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.Endpoint;
import com.avantir.blowfish.model.Terminal;
import com.avantir.blowfish.repository.AcquirerRepository;
import com.avantir.blowfish.repository.EndpointRepository;
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
public class EndpointService {

    @Autowired
    private EndpointRepository endpointRepository;

    @CachePut(cacheNames="endpoint", key="#endpoint.id")
    @Transactional(readOnly=false)
    public Endpoint create(Endpoint endpoint) {
        return endpointRepository.save(endpoint);
    }


    @CachePut(cacheNames="endpoint", key="#newEndpoint.id")
    @Transactional(readOnly=false)
    public Endpoint update(Endpoint newEndpoint) {
        if(newEndpoint != null){
            //Optional<Acquirer> optional = acquirerRepository.findById(newAcquirer.getId());
            //Acquirer oldAcquirer = optional.orElse(null);
            Endpoint oldEndpoint = endpointRepository.findById(newEndpoint.getId());
            if(!StringUtil.isEmpty(newEndpoint.getIp()))
                oldEndpoint.setIp(newEndpoint.getIp());
            if(newEndpoint.getPort() != 0)
                oldEndpoint.setPort(newEndpoint.getPort());
            if(newEndpoint.getTimeout() != 0)
                oldEndpoint.setTimeout(newEndpoint.getTimeout());
            if(!StringUtil.isEmpty(newEndpoint.getDescription()))
                oldEndpoint.setDescription(newEndpoint.getDescription());
            oldEndpoint.setSsl(newEndpoint.isSsl());
            oldEndpoint.setStatus(newEndpoint.getStatus());
            return endpointRepository.save(oldEndpoint);
        }
        return null;
    }

    @CacheEvict(value = "endpoint", key = "#id")
    @Transactional(readOnly=false)
    public void delete(long id) {
        endpointRepository.delete(id);
    }


    @Cacheable(value = "endpoint", key = "#id")
    @Transactional(readOnly=true)
    public Endpoint findById(Long id) {

        try
        {
            //Optional<Acquirer> optional = acquirerRepository.findById(id);
            //return optional.orElse(null);
            return endpointRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "endpoints", key = "#ip_port")
    @Transactional(readOnly=true)
    public Endpoint findByIpPort(String ip, int port) {

        try
        {
            return endpointRepository.findByIpPort(ip, port);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "endpoints", key = "1")
    @Transactional(readOnly=true)
    public List<Endpoint> findAllActive() {

        try
        {
            List<Endpoint> list = endpointRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

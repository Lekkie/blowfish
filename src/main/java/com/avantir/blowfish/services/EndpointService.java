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

    public static final String ALL_ENDPOINT = "ALL_ENDPOINT";
    public static final String ACTIVE_ENDPOINT = "ACTIVE_ENDPOINT";

    @Autowired
    private EndpointRepository endpointRepository;

    @CachePut(cacheNames="endpoint")
    @Transactional(readOnly=false)
    public Endpoint create(Endpoint endpoint) {
        return endpointRepository.save(endpoint);
    }


    @CachePut(cacheNames="endpoint")
    @Transactional(readOnly=false)
    public Endpoint update(Endpoint newEndpoint) {
        if(newEndpoint != null){
            //Optional<Acquirer> optional = acquirerRepository.findById(newAcquirer.getId());
            //Acquirer oldAcquirer = optional.orElse(null);
            Endpoint oldEndpoint = endpointRepository.findByEndpointId(newEndpoint.getEndpointId());
            if(!StringUtil.isEmpty(newEndpoint.getIp()))
                oldEndpoint.setIp(newEndpoint.getIp());
            if(newEndpoint.getPort() != 0)
                oldEndpoint.setPort(newEndpoint.getPort());
            if(newEndpoint.getTimeout() != 0)
                oldEndpoint.setTimeout(newEndpoint.getTimeout());
            if(!StringUtil.isEmpty(newEndpoint.getDescription()))
                oldEndpoint.setDescription(newEndpoint.getDescription());
            oldEndpoint.setSslEnabled(newEndpoint.isSslEnabled());
            oldEndpoint.setStatus(newEndpoint.getStatus());
            return endpointRepository.save(oldEndpoint);
        }
        return null;
    }

    @CacheEvict(value = "endpoint")
    @Transactional(readOnly=false)
    public void delete(long id) {
        endpointRepository.delete(id);
    }


    @Cacheable(value = "endpoint")
    @Transactional(readOnly=true)
    public Endpoint findByEndpointId(Long id) {

        try
        {
            //Optional<Acquirer> optional = acquirerRepository.findById(id);
            //return optional.orElse(null);
            return endpointRepository.findByEndpointId(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "endpoints", key = "{#ip, #port}")
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


    @Cacheable(value = "endpoint", key = "#root.target.ACTIVE_ENDPOINT")
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


    @Cacheable(value = "endpoint", key = "#root.target.ALL_ENDPOINT")
    @Transactional(readOnly=true)
    public List<Endpoint> findAll() {

        try
        {
            List<Endpoint> list = endpointRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}

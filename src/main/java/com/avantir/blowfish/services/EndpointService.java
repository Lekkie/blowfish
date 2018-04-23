package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.Endpoint;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.EndpointRepository;
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
public class EndpointService {

    public static final String ALL_ENDPOINT = "ALL_ENDPOINT";
    public static final String ACTIVE_ENDPOINT = "ACTIVE_ENDPOINT";

    @Autowired
    private EndpointRepository endpointRepository;
    @Autowired
    StringService stringService;

    @CachePut(cacheNames="endpoint")
    @Transactional(readOnly=false)
    public Optional<Endpoint> create(Endpoint endpoint) {
        return Optional.ofNullable(endpointRepository.save(endpoint));
    }


    @CachePut(cacheNames="endpoint")
    @Transactional(readOnly=false)
    public Optional<Endpoint> update(Endpoint newEndpoint) {
        Endpoint oldEndpoint = endpointRepository.findByEndpointId(newEndpoint.getEndpointId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Endpoint"));

        if(!stringService.isEmpty(newEndpoint.getIp()))
            oldEndpoint.setIp(newEndpoint.getIp());
        if(newEndpoint.getPort() != 0)
            oldEndpoint.setPort(newEndpoint.getPort());
        if(newEndpoint.getTimeout() != 0)
            oldEndpoint.setTimeout(newEndpoint.getTimeout());
        if(!stringService.isEmpty(newEndpoint.getDescription()))
            oldEndpoint.setDescription(newEndpoint.getDescription());
        oldEndpoint.setSslEnabled(newEndpoint.isSslEnabled());
        oldEndpoint.setStatus(newEndpoint.getStatus());
        return Optional.ofNullable(endpointRepository.save(oldEndpoint));
    }

    @CacheEvict(value = "endpoint")
    @Transactional(readOnly=false)
    public void delete(long id) {
        endpointRepository.delete(id);
    }


    @Cacheable(value = "endpoint")
    @Transactional(readOnly=true)
    public Optional<Endpoint> findByEndpointId(Long id) {
        return endpointRepository.findByEndpointId(id);
    }

    @Cacheable(value = "endpoints", key = "{#ip, #port}")
    @Transactional(readOnly=true)
    public Optional<Endpoint> findByIpPort(String ip, int port) {
        return endpointRepository.findByIpPort(ip, port);
    }


    @Cacheable(value = "endpoint", key = "#root.target.ACTIVE_ENDPOINT")
    @Transactional(readOnly=true)
    public Optional<List<Endpoint>> findAllActive() {
        return  endpointRepository.findByStatus(1);
    }


    @Cacheable(value = "endpoint", key = "#root.target.ALL_ENDPOINT")
    @Transactional(readOnly=true)
    public Optional<List<Endpoint>> findAll() {
        return Optional.ofNullable(endpointRepository.findAll());
    }


}

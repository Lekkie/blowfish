package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.Endpoint;
import com.avantir.blowfish.entity.HsmEndpoint;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.HsmEndpointRepository;
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
public class HsmEndpointService {

    public static final String ALL_HSM_ENDPOINT = "ALL_HSM_ENDPOINT";
    public static final String ACTIVE_HSM_ENDPOINT = "ACTIVE_HSM_ENDPOINT";

    @Autowired
    private HsmEndpointRepository hsmEndpointRepository;
    @Autowired
    StringService stringService;

    @CachePut(cacheNames="hsmendpoint")
    @Transactional(readOnly=false)
    public Optional<HsmEndpoint> create(HsmEndpoint endpoint) {
        return Optional.ofNullable(hsmEndpointRepository.save(endpoint));
    }


    @CachePut(cacheNames="hsmendpoint")
    @Transactional(readOnly=false)
    public Optional<HsmEndpoint> update(HsmEndpoint newHsmEndpoint) {
        HsmEndpoint oldHsmEndpoint = hsmEndpointRepository.findByHsmEndpointId(newHsmEndpoint.getHsmEndpointId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Endpoint"));

        if(!stringService.isEmpty(newHsmEndpoint.getIp()))
            oldHsmEndpoint.setIp(newHsmEndpoint.getIp());
        if(newHsmEndpoint.getPort() != 0)
            oldHsmEndpoint.setPort(newHsmEndpoint.getPort());
        if(newHsmEndpoint.getTimeout() != 0)
            oldHsmEndpoint.setTimeout(newHsmEndpoint.getTimeout());
        if(!stringService.isEmpty(newHsmEndpoint.getDescription()))
            oldHsmEndpoint.setDescription(newHsmEndpoint.getDescription());
        oldHsmEndpoint.setSslEnabled(newHsmEndpoint.isSslEnabled());
        oldHsmEndpoint.setStatus(newHsmEndpoint.getStatus());
        return Optional.ofNullable(hsmEndpointRepository.save(oldHsmEndpoint));
    }

    @CacheEvict(value = "hsmendpoint")
    @Transactional(readOnly=false)
    public void delete(long id) {
        hsmEndpointRepository.delete(id);
    }


    @Cacheable(value = "hsmendpoint")
    @Transactional(readOnly=true)
    public Optional<HsmEndpoint> findByHsmEndpointId(Long id) {
        return hsmEndpointRepository.findByHsmEndpointId(id);
    }

    @Cacheable(value = "hsmendpoints", key = "{#ip, #port}")
    @Transactional(readOnly=true)
    public Optional<HsmEndpoint> findByIpPort(String ip, int port) {
        return hsmEndpointRepository.findByIpPort(ip, port);
    }



    @Cacheable(value = "endpoint", key = "#root.target.ALL_HSM_ENDPOINT")
    @Transactional(readOnly=true)
    public Optional<List<HsmEndpoint>> findAll() {
        return Optional.ofNullable(hsmEndpointRepository.findAll());
    }


}

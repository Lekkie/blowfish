package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.ActiveHsm;
import com.avantir.blowfish.entity.HsmEndpoint;
import com.avantir.blowfish.entity.KeyMap;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.ActiveHsmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ActiveHsmService {

    private static final Logger logger = LoggerFactory.getLogger(ActiveHsmService.class);

    public static final String ALL_ACTIVE_HSM = "ALL_ACTIVE_HSM";
    public static final String ACTIVE_ACTIVE_HSM = "ACTIVE_ACTIVE_HSM";

    @Autowired
    private ActiveHsmRepository activeHsmRepository;
    @Autowired
    StringService stringService;
    @Autowired
    HsmEndpointService hsmEndpointService;

    @CachePut(cacheNames="key")
    @Transactional(readOnly=false)
    public Optional<ActiveHsm> create(ActiveHsm keyMap) {
        return Optional.ofNullable(activeHsmRepository.save(keyMap));
    }

    @CachePut(cacheNames="key")
    @Transactional(readOnly=false)
    public Optional<ActiveHsm> update(ActiveHsm newKeyMap) {
        ActiveHsm oldKeyMap = activeHsmRepository.findByActiveHsmId(newKeyMap.getActiveHsmId()).orElseThrow(() -> new BlowfishEntityNotFoundException("KeyMap"));

        if(!stringService.isEmpty(newKeyMap.getCode()))
            oldKeyMap.setCode(newKeyMap.getCode());
        if(newKeyMap.getHsmEndpointId() > 0)
            oldKeyMap.setHsmEndpointId(newKeyMap.getHsmEndpointId());
        return Optional.ofNullable(activeHsmRepository.save(oldKeyMap));
    }

    @CacheEvict(value = "key")
    @Transactional(readOnly=false)
    public void delete(long id) {
        activeHsmRepository.delete(id);
    }




    @Cacheable(value = "key")
    @Transactional(readOnly=true)
    public Optional<ActiveHsm> findByKeyMapId(Long id) {
        return activeHsmRepository.findByActiveHsmId(id);
    }

    @Cacheable(value = "key")
    @Transactional(readOnly=true)
    public Optional<ActiveHsm> findByCode(String code) {
        return activeHsmRepository.findByCode(code);
    }

    @Cacheable(value = "key", key = "#root.target.ALL_KEY")
    @Transactional(readOnly=true)
    public Optional<List<ActiveHsm>> findAll() {
        return Optional.ofNullable(activeHsmRepository.findAll());
    }


    public Optional<HsmEndpoint> getActiveTMSHsm(){
        ActiveHsm activeHsm = findByCode("TMS_HSM").orElseThrow(() -> new BlowfishEntityNotFoundException("ActiveHSM for TMS_HSM"));
        return hsmEndpointService.findByHsmEndpointId(activeHsm.getHsmEndpointId());
    }




}

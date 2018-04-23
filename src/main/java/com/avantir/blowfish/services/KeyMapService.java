package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.Key;
import com.avantir.blowfish.entity.KeyMap;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.KeyMapRepository;
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
public class KeyMapService {

    private static final Logger logger = LoggerFactory.getLogger(KeyMapService.class);

    public static final String ALL = "all";
    public static final String ACTIVE_KEY = "ACTIVE_KEY";

    @Autowired
    private KeyMapRepository keyMapRepository;
    @Autowired
    StringService stringService;

    @CachePut(cacheNames="key")
    @Transactional(readOnly=false)
    public Optional<KeyMap> create(KeyMap keyMap) {
        return Optional.ofNullable(keyMapRepository.save(keyMap));
    }

    @CachePut(cacheNames="key")
    @Transactional(readOnly=false)
    public Optional<KeyMap> update(KeyMap newKeyMap) {
        KeyMap oldKeyMap = keyMapRepository.findByKeyMapId(newKeyMap.getKeyMapId()).orElseThrow(() -> new BlowfishEntityNotFoundException("KeyMap"));

        if(!stringService.isEmpty(newKeyMap.getCode()))
            oldKeyMap.setCode(newKeyMap.getCode());
        if(newKeyMap.getKeyId() > 0)
            oldKeyMap.setKeyId(newKeyMap.getKeyId());
        return Optional.ofNullable(keyMapRepository.save(oldKeyMap));
    }

    @CacheEvict(value = "key")
    @Transactional(readOnly=false)
    public void delete(long id) {
        keyMapRepository.delete(id);
    }




    @Cacheable(value = "key")
    @Transactional(readOnly=true)
    public Optional<KeyMap> findByKeyMapId(Long id) {
        return keyMapRepository.findByKeyMapId(id);
    }

    @Cacheable(value = "key")
    @Transactional(readOnly=true)
    public Optional<KeyMap> findByCode(String code) {
        return keyMapRepository.findByCode(code);
    }

    @Cacheable(value = "key", key = "#root.target.ALL_KEY")
    @Transactional(readOnly=true)
    public Optional<List<KeyMap>> findAll() {
        return Optional.ofNullable(keyMapRepository.findAll());
    }






}

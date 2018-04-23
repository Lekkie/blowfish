package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.Key;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.KeyRepository;
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
public class KeyService {

    private static final Logger logger = LoggerFactory.getLogger(KeyService.class);

    public static final String ALL = "all";
    public static final String ACTIVE_KEY = "ACTIVE_KEY";

    @Autowired
    private KeyRepository keyRepository;
    @Autowired
    StringService stringService;

    @CachePut(cacheNames="key")
    @Transactional(readOnly=false)
    public Optional<Key> create(Key key) {
        return Optional.ofNullable(keyRepository.save(key));
    }

    @CachePut(cacheNames="key")
    @Transactional(readOnly=false)
    public Optional<Key> update(Key newKey) {
        Key oldKey = keyRepository.findByKeyId(newKey.getKeyId()).orElseThrow(() -> new BlowfishEntityNotFoundException("Key"));

        if(!stringService.isEmpty(newKey.getCheckDigit()))
            oldKey.setCheckDigit(newKey.getCheckDigit());
        if(!stringService.isEmpty(newKey.getData()))
            oldKey.setData(newKey.getData());
        if(!stringService.isEmpty(newKey.getDescription()))
            oldKey.setDescription(newKey.getDescription());
        if(newKey.getKeyCryptographicTypeId() != 0)
            oldKey.setKeyCryptographicTypeId(newKey.getKeyCryptographicTypeId());
        if(newKey.getKeyUsageTypeId() != 0)
            oldKey.setKeyUsageTypeId(newKey.getKeyUsageTypeId());
        oldKey.setStatus(newKey.getStatus());
        return Optional.ofNullable(keyRepository.save(oldKey));
    }

    @CacheEvict(value = "key")
    @Transactional(readOnly=false)
    public void delete(long id) {
        keyRepository.delete(id);
    }




    @Cacheable(value = "key")
    @Transactional(readOnly=true)
    public Optional<Key> findByKeyId(Long id) {
        return keyRepository.findByKeyId(id);
    }

    @Cacheable(value = "key", key = "#root.target.ACTIVE_KEY")
    @Transactional(readOnly=true)
    public Optional<List<Key>> findAllActive() {
        return keyRepository.findByStatus(1);
    }

    @Cacheable(value = "key", key = "#root.target.ALL_KEY")
    @Transactional(readOnly=true)
    public Optional<List<Key>> findAll() {
        return Optional.ofNullable(keyRepository.findAll());
    }






}

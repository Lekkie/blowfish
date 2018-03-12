package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.Key;
import com.avantir.blowfish.repository.KeyRepository;
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
public class KeyService {

    public static final String ALL = "all";
    public static final String ACTIVE_KEY = "ACTIVE_KEY";

    @Autowired
    private KeyRepository keyRepository;

    @CachePut(cacheNames="key")
    @Transactional(readOnly=false)
    public Key create(Key key) {
        return keyRepository.save(key);
    }

    @CachePut(cacheNames="key")
    @Transactional(readOnly=false)
    public Key update(Key newKey) {
        if(newKey != null){
            Key oldKey = keyRepository.findByKeyId(newKey.getKeyId());
            if(!StringUtil.isEmpty(newKey.getAlgo()))
                oldKey.setAlgo(newKey.getAlgo());
            if(!StringUtil.isEmpty(newKey.getCheckDigit()))
                oldKey.setCheckDigit(newKey.getCheckDigit());
            if(!StringUtil.isEmpty(newKey.getData()))
                oldKey.setData(newKey.getData());
            if(!StringUtil.isEmpty(newKey.getDescription()))
                oldKey.setDescription(newKey.getDescription());
            if(!StringUtil.isEmpty(newKey.getSalt()))
                oldKey.setSalt(newKey.getSalt());
            if(!StringUtil.isEmpty(newKey.getVersion()))
                oldKey.setVersion(newKey.getVersion());
            if(newKey.getUsage() != 0)
                oldKey.setUsage(newKey.getUsage());
            oldKey.setStatus(newKey.getStatus());
            return keyRepository.save(oldKey);
        }
        return null;
    }

    @CacheEvict(value = "key")
    @Transactional(readOnly=false)
    public void delete(long id) {
        keyRepository.delete(id);
    }




    @Cacheable(value = "key")
    @Transactional(readOnly=true)
    public Key findByKeyId(Long id) {

        try
        {
            return keyRepository.findByKeyId(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "key")
    @Transactional(readOnly=true)
    public Key findByVersion(String version) {

        try
        {
            return keyRepository.findByVersion(version);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "key", key = "#root.target.ACTIVE_KEY")
    @Transactional(readOnly=true)
    public List<Key> findAllActive() {

        try
        {
            List<Key> list = keyRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



    @Cacheable(value = "key", key = "#root.target.ALL_KEY")
    @Transactional(readOnly=true)
    public List<Key> findAll() {

        try
        {
            List<Key> list = keyRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}

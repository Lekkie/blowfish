package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.Bin;
import com.avantir.blowfish.repository.AcquirerRepository;
import com.avantir.blowfish.repository.BinRepository;
import com.avantir.blowfish.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class BinService {

    public static final String ALL_BIN = "ALL_BIN";
    public static final String ACTIVE_BIN = "ACTIVE_BIN";

    @Autowired
    private BinRepository binRepository;




    @CachePut(cacheNames="bin", key="#bin.binId")
    @Transactional(readOnly=false)
    public Bin create(Bin acquirer) {
        return binRepository.save(acquirer);
    }


    @CachePut(cacheNames="bin", key="#newBin.binId")
    @Transactional(readOnly=false)
    public Bin update(Bin newBin) {
        if(newBin != null){
            Bin oldBin = binRepository.findByBinId(newBin.getBinId());
            if(!StringUtil.isEmpty(newBin.getCode()))
                oldBin.setCode(newBin.getCode());
            if(!StringUtil.isEmpty(newBin.getDescription()))
                oldBin.setDescription(newBin.getDescription());
            oldBin.setStatus(newBin.getStatus());
            return binRepository.save(oldBin);
        }
        return null;
    }

    @CacheEvict(value = "bin")
    @Transactional(readOnly=false)
    public void delete(long id) {
        binRepository.delete(id);
    }


    @Cacheable(value = "bin")
    @Transactional(readOnly=true)
    public Bin findByBinId(Long id) {

        try
        {
            return binRepository.findByBinId(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "bin")
    @Transactional(readOnly=true)
    public Bin findByCode(String code) {

        try
        {
            return binRepository.findByCodeAllIgnoringCase(code);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "bin")
    @Transactional(readOnly=true)
    public Bin findByPan(String pan) {

        try
        {
            List<Bin> binList = binRepository.findAll();
            List<Bin> matchedBinList = new ArrayList<Bin>();
            for(Bin bin: binList){
                String binCode = bin.getCode();
                if(pan.startsWith(binCode))
                    matchedBinList.add(bin);
            }
            if(matchedBinList.size() > 1){
                Bin maxLenBin = matchedBinList.get(0);
                int maxLenBinLen = maxLenBin.getCode().length();
                for(Bin bin : matchedBinList){
                    if(maxLenBinLen < bin.getCode().length())
                        maxLenBin = bin;
                }
                return maxLenBin;
            }
            else if(matchedBinList.size() == 1){
                return matchedBinList.get(0);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(value = "bin", key = "#root.target.ACTIVE_BIN")
    @Transactional(readOnly=true)
    public List<Bin> findAllActive() {

        try
        {
            List<Bin> list = binRepository.findByStatus(1);
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "bin", key = "#root.target.ALL_BIN")
    @Transactional(readOnly=true)
    public List<Bin> findAll() {

        try
        {
            List<Bin> list = binRepository.findAll();
            return list;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}

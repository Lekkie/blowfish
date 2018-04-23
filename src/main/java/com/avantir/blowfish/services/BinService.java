package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.entity.Bin;
import com.avantir.blowfish.exceptions.BlowfishEntityNotFoundException;
import com.avantir.blowfish.repository.BinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class BinService {

    public static final String ALL_BIN = "ALL_BIN";
    public static final String ACTIVE_BIN = "ACTIVE_BIN";

    @Autowired
    private BinRepository binRepository;
    @Autowired
    StringService stringService;




    @CachePut(cacheNames="bin", key="#bin.binId")
    @Transactional(readOnly=false)
    public Optional<Bin> create(Bin acquirer) {
        return Optional.ofNullable(binRepository.save(acquirer));
    }


    @CachePut(cacheNames="bin", key="#newBin.binId")
    @Transactional(readOnly=false)
    public Optional<Bin> update(Bin newBin) {
        Optional<Bin> oldAcqTermParamOptional = binRepository.findByBinId(newBin.getBinId());
        Bin oldBin = oldAcqTermParamOptional.orElseThrow(() -> new BlowfishEntityNotFoundException("Bin"));

        if(!stringService.isEmpty(newBin.getCode()))
            oldBin.setCode(newBin.getCode());
        if(!stringService.isEmpty(newBin.getDescription()))
            oldBin.setDescription(newBin.getDescription());
        oldBin.setStatus(newBin.getStatus());

        return Optional.ofNullable(binRepository.save(oldBin));
    }



    @CacheEvict(value = "bin")
    @Transactional(readOnly=false)
    public void delete(long id) {
        binRepository.delete(id);
    }


    @Cacheable(value = "bin")
    @Transactional(readOnly=true)
    public Optional<Bin> findByBinId(Long id) {
        return binRepository.findByBinId(id);
    }

    @Cacheable(value = "bin")
    @Transactional(readOnly=true)
    public Optional<Bin> findByCode(String code) {
        return binRepository.findByCodeAllIgnoringCase(code);
    }


    @Cacheable(value = "bin")
    @Transactional(readOnly=true)
    public Optional<Bin> findByPan(String pan) {
        Optional<List<Bin>> optionalBinList = Optional.ofNullable(binRepository.findAll());
        List<Bin> binList = optionalBinList.orElseThrow(() -> new BlowfishEntityNotFoundException("Pan"));
        return findByPan(binList, pan);
    }

    @Cacheable(value = "bin")
    @Transactional(readOnly=true)
    public Optional<Bin> findByPan(List<Bin> binList, String pan) {
        List<Bin> matchedBinList = binList.stream()
                .filter(bin -> pan.startsWith(bin.getCode()))
                .collect(Collectors.toList());

        Bin maxLenBin = matchedBinList.stream()
                .max((a, b)  -> Bin.BY_BIN_CODE_LENGTH.compare(a, b))
                .get();

        return Optional.ofNullable(maxLenBin);
    }


    @Cacheable(value = "bin", key = "#root.target.ACTIVE_BIN")
    @Transactional(readOnly=true)
    public Optional<List<Bin>> findAllActive() {
        return binRepository.findByStatus(1);
    }

    @Cacheable(value = "bin", key = "#root.target.ALL_BIN")
    @Transactional(readOnly=true)
    public Optional<List<Bin>> findAll() {
        return Optional.ofNullable(binRepository.findAll());
    }



}

package com.avantir.blowfish.messaging.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.messaging.model.ISOBridge;
import com.avantir.blowfish.messaging.repository.ISOBridgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class ISOBridgeService {

    @Autowired
    private ISOBridgeRepository isoBridgeRepository;


    @Transactional(readOnly=false)
    public ISOBridge create(ISOBridge isoBridge) {
        return isoBridgeRepository.save(isoBridge);
    }

    @Transactional(readOnly=true)
    public List<ISOBridge> findAllActive() {

        try
        {
            List<ISOBridge> isoBridgeList = isoBridgeRepository.findByStatus(1);
            return isoBridgeList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public ISOBridge findByISOBridgeId(Long isoBridgeId) {

        try
        {
            return isoBridgeRepository.findById(isoBridgeId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Transactional(readOnly=true)
    public ISOBridge findByISOBridgeName(String name) {

        try
        {
            return isoBridgeRepository.findByNameAllIgnoringCase(name);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

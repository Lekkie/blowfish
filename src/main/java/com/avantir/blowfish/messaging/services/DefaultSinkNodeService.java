package com.avantir.blowfish.messaging.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.messaging.model.DefaultSinkNode;
import com.avantir.blowfish.messaging.model.Node;
import com.avantir.blowfish.messaging.respository.DefaultSinkNodeRepository;
import com.avantir.blowfish.messaging.respository.NodeRepository;
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
public class DefaultSinkNodeService {

    @Autowired
    private DefaultSinkNodeRepository defaultSinkNodeRepository;


    @Transactional(readOnly=true)
    public DefaultSinkNode findByNodeId(Long nodeId) {

        try
        {
            return defaultSinkNodeRepository.findById(nodeId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Transactional(readOnly=true)
    public DefaultSinkNode findByName(String name) {

        try
        {
            return defaultSinkNodeRepository.findByNameAllIgnoringCase(name);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public DefaultSinkNode getDefaultSinkNode() {

        try
        {
            return defaultSinkNodeRepository.findByNameAllIgnoringCase("DEFAULT");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}

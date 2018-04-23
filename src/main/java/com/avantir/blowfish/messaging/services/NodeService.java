package com.avantir.blowfish.messaging.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.messaging.entity.Node;
import com.avantir.blowfish.messaging.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class NodeService {

    @Autowired
    private NodeRepository nodeRepository;


    @Transactional(readOnly=true)
    public Node findByNodeId(Long nodeId) {

        try
        {
            return nodeRepository.findByNodeId(nodeId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public List<Node> findAll() {

        try
        {
            return nodeRepository.findAll();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Transactional(readOnly=true)
    public Node findByName(String name) {

        try
        {
            return nodeRepository.findByNameStatusAllIgnoringCase(name, 1);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly=true)
    public Node findBySrcNode(String name) {

        try
        {
            return nodeRepository.findByTypeStatusAllIgnoringCase(0, 1);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    @Transactional(readOnly=true)
    public Node findBySinkNode(String name) {

        try
        {
            return nodeRepository.findByTypeStatusAllIgnoringCase(1, 1);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



    @Transactional(readOnly=true)
    public List<Node> findAllActive() {

        try
        {
            List<Node> endpointList = nodeRepository.findByStatus(1);
            return endpointList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}

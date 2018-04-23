package com.avantir.blowfish.messaging.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.messaging.entity.RouteByReceivingInst;
import com.avantir.blowfish.messaging.repository.RouteByReceivingInstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Service
public class RouteByReceivingInstService {

    @Autowired
    private RouteByReceivingInstRepository routeByReceivingInstRepository;


    @Transactional(readOnly=true)
    public RouteByReceivingInst findByReceivingInst(String recevingInst) {

        try
        {
            return routeByReceivingInstRepository.findByReceivingInstId(recevingInst);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}

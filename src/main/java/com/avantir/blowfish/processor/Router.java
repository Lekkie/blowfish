package com.avantir.blowfish.processor;

import com.avantir.blowfish.messaging.model.Node;
import com.avantir.blowfish.messaging.model.RouteByReceivingInst;
import com.avantir.blowfish.messaging.services.RouteByReceivingInstService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lekanomotayo on 05/01/2018.
 */

@Component
public class Router {

    private static final Logger logger = LoggerFactory.getLogger(Router.class);


    @Autowired
    RouteByReceivingInstService routeByReceivingInstService;

    public Long getRouteByReceivingInst(String receivingInst){

        RouteByReceivingInst routeByReceivingInst = routeByReceivingInstService.findByReceivingInst(receivingInst);
        if(routeByReceivingInst == null)
            return 0L;

        return routeByReceivingInst.getSinkNodeId();
    }


}

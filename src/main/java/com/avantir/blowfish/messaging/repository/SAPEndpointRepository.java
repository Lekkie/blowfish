package com.avantir.blowfish.messaging.repository;

import com.avantir.blowfish.messaging.entity.SAPEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface SAPEndpointRepository extends JpaRepository<SAPEndpoint, String> {

    //@Cacheable(value = "endpointById")
    SAPEndpoint findBySapEndpointId(@Param("sapEndpointId") Long sapEndpointId);
    //@Cacheable(value = "routeByRouteId")
    SAPEndpoint findByNameAllIgnoringCase(String name);
    //@Cacheable(value = "endpointById")
    SAPEndpoint findByTcpEndpointId(@Param("tcpEndpointId") Long tcpEndpointId);


}

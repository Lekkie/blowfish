package com.avantir.blowfish.messaging.repository;

import com.avantir.blowfish.messaging.entity.TCPEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface TCPEndpointRepository extends JpaRepository<TCPEndpoint, String> {

    //@Cacheable(value = "endpointById")
    @Query("FROM TCPEndpoint e WHERE e.tcpEndpointId = :tcpEndpointId")
    TCPEndpoint findByTcpEndpointId(@Param("tcpEndpointId") Long tcpEndpointId);
    //@Cacheable(value = "endpointByName")
    TCPEndpoint findByNameAllIgnoringCase(@Param("name") String name);
    //@Cacheable(value = "endpointByEndpointPortIp")
    //TCPEndpoint findByEndpointPortIPAllIgnoringCase(TCPEndpointPortIP tcpEndpointPortIP);
    @Query("FROM TCPEndpoint e WHERE e.status = :status")
    List<TCPEndpoint> findByStatus(@Param("status") int status);


}

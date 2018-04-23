package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface EndpointRepository extends JpaRepository<Endpoint, Long> {

    Optional<Endpoint> findByEndpointId(@Param("endpointId") Long endpointId);
    @Query("FROM Endpoint e WHERE e.ip = :ip AND e.port = :port")
    Optional<Endpoint> findByIpPort(@Param("ip") String ip, @Param("port") int port);
    @Query("FROM Endpoint e WHERE e.status = :status")
    Optional<List<Endpoint>> findByStatus(@Param("status") int status);


}

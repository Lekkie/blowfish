package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.Endpoint;
import com.avantir.blowfish.model.Terminal;
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
public interface EndpointRepository extends JpaRepository<Endpoint, Long> {

    //@Cacheable(value = "endpointById")
    Endpoint findById(@Param("id") Long id);
    @Query("FROM Endpoint e WHERE e.ip = :ip AND e.port = :port")
    Endpoint findByIpPort(@Param("ip") String ip, @Param("port") int port);

    @Query("FROM Endpoint e WHERE e.status = :status")
    List<Endpoint> findByStatus(@Param("status") int status);


}

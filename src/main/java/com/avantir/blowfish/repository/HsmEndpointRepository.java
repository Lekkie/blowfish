package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.Endpoint;
import com.avantir.blowfish.entity.HsmEndpoint;
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
public interface HsmEndpointRepository extends JpaRepository<HsmEndpoint, Long> {

    Optional<HsmEndpoint> findByHsmEndpointId(@Param("hsmEndpointId") Long hsmEndpointId);
    @Query("FROM HsmEndpoint e WHERE e.ip = :ip AND e.port = :port")
    Optional<HsmEndpoint> findByIpPort(@Param("ip") String ip, @Param("port") int port);


}

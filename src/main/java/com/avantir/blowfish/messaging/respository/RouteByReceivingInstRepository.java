package com.avantir.blowfish.messaging.respository;

import com.avantir.blowfish.messaging.model.RouteByReceivingInst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface RouteByReceivingInstRepository extends JpaRepository<RouteByReceivingInst, String> {

    //@Cacheable(value = "endpointById")
    RouteByReceivingInst findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    @Query("FROM RouteByReceivingInst r WHERE r.receivingInstId = :receivingInstId")
    RouteByReceivingInst findByReceivingInstId(@Param("receivingInstId") String receivingInstId);

}

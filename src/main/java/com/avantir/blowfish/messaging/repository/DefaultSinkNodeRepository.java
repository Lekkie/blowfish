package com.avantir.blowfish.messaging.repository;

import com.avantir.blowfish.messaging.model.DefaultSinkNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface DefaultSinkNodeRepository extends JpaRepository<DefaultSinkNode, String> {

    //@Cacheable(value = "endpointById")
    DefaultSinkNode findByDefaultSinkNodeId(@Param("defaultSinkNodeId") Long defaultSinkNodeId);
    //@Cacheable(value = "endpointByName")
    DefaultSinkNode findByNameAllIgnoringCase(@Param("name") String name);


}

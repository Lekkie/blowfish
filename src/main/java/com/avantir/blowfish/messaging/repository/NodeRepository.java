package com.avantir.blowfish.messaging.repository;

import com.avantir.blowfish.messaging.entity.Node;
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
public interface NodeRepository extends JpaRepository<Node, Long> {

    //@Cacheable(value = "endpointById")
    Node findByNodeId(@Param("nodeId") Long nodeId);
    //@Cacheable(value = "endpointByName")
    @Query("FROM Node n WHERE n.name = :name AND n.status = :status")
    Node findByNameStatusAllIgnoringCase(@Param("name") String name, @Param("status") int status);
    @Query("FROM Node n WHERE n.type = :type AND n.status = :status")
    Node findByTypeStatusAllIgnoringCase(@Param("type") int type, @Param("status") int status);
    @Query("FROM Node n WHERE n.status = :status")
    List<Node> findByStatus(@Param("status") int status);


}

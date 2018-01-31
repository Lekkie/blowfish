package com.avantir.blowfish.messaging.respository;

import com.avantir.blowfish.messaging.model.DefaultSinkNode;
import com.avantir.blowfish.messaging.model.Node;
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
public interface DefaultSinkNodeRepository extends JpaRepository<DefaultSinkNode, String> {

    //@Cacheable(value = "endpointById")
    DefaultSinkNode findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    DefaultSinkNode findByNameAllIgnoringCase(@Param("name") String name);


}

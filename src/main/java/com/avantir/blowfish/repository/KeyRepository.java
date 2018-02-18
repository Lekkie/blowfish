package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface KeyRepository extends JpaRepository<Key, String> {

    //@Cacheable(value = "endpointById")
    Key findById(@Param("id") Long id);
    Key findByVersion(@Param("version") String version);


}

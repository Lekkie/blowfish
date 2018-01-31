package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.Domain;
import com.avantir.blowfish.model.KeyVersion;
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
public interface KeyVersionRepository extends JpaRepository<KeyVersion, String> {

    //@Cacheable(value = "endpointById")
    KeyVersion findById(@Param("id") Long id);
    KeyVersion findByVersion(@Param("version") String version);


}

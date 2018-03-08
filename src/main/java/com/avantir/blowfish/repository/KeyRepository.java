package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.Key;
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
public interface KeyRepository extends JpaRepository<Key, Long> {

    //@Cacheable(value = "endpointById")
    Key findByKeyId(@Param("keyId") Long keyId);
    Key findByVersion(@Param("version") String version);
    @Query("FROM Key n WHERE n.status = :status")
    List<Key> findByStatus(@Param("status") int status);


}

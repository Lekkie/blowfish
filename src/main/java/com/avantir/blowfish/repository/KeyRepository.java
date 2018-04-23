package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.Key;
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
public interface KeyRepository extends JpaRepository<Key, Long> {

    Optional<Key> findByKeyId(@Param("keyId") Long keyId);
    @Query("FROM Key n WHERE n.status = :status")
    Optional<List<Key>> findByStatus(@Param("status") int status);


}

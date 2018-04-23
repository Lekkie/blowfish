package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.Bin;
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
public interface BinRepository extends JpaRepository<Bin, Long> {

    //@Cacheable(value = "endpointById")
    Optional<Bin> findByBinId(@Param("binId") Long binId);
    //@Cacheable(value = "endpointByName")
    Optional<Bin> findByCodeAllIgnoringCase(@Param("code") String code);
    @Query("FROM Bin n WHERE n.status = :status")
    Optional<List<Bin>> findByStatus(@Param("status") int status);


}

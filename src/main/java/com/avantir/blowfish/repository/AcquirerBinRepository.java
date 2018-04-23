package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.AcquirerBin;
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
public interface AcquirerBinRepository extends JpaRepository<AcquirerBin, Long> {

    //@Cacheable(value = "endpointById")
    Optional<AcquirerBin> findByAcquirerBinId(@Param("acquirerBinId") Long acquirerBinId);
    Optional<List<AcquirerBin>> findByAcquirerId(@Param("acquirerId") Long acquirerId);
    Optional<AcquirerBin> findByBinId(@Param("binId") Long binId);
    @Query("FROM AcquirerBin a WHERE a.acquirerId = :acquirerId AND a.binId = :binId")
    Optional<AcquirerBin> findByAcquirerIdBinId(@Param("acquirerId") Long acquirerId, @Param("binId") Long binId);


}

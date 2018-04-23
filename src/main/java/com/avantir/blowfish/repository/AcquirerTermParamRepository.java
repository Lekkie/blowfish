package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.AcquirerTermParam;
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
public interface AcquirerTermParamRepository extends JpaRepository<AcquirerTermParam, Long> {

    @Query("FROM AcquirerTermParam a WHERE a.acquirerTermParamId = :acquirerTermParamId")
    Optional<AcquirerTermParam> findByAcquirerTermParamId(@Param("acquirerTermParamId") Long acquirerTermParamId);
    Optional<AcquirerTermParam> findByAcquirerId(@Param("acquirerId") Long acquirerId);
    Optional<List<AcquirerTermParam>> findByTermParamId(@Param("termParamId") Long termParamId);
    @Query("FROM AcquirerTermParam a WHERE a.acquirerId = :acquirerId AND a.termParamId = :termParamId")
    Optional<AcquirerTermParam> findByAcquirerIdTermParamId(@Param("acquirerId") Long acquirerId, @Param("termParamId") Long termParamId);


}

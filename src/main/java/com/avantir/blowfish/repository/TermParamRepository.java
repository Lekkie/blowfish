package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.TermParam;
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
public interface TermParamRepository extends JpaRepository<TermParam, Long> {

    @Query("FROM TermParam t WHERE t.termParamId = :termParamId")
    Optional<TermParam> findByTermParamId(@Param("termParamId") Long termParamId);
    Optional<TermParam> findByNameAllIgnoringCase(@Param("name") String name);
    @Query("FROM TermParam t WHERE t.status = :status")
    Optional<List<TermParam>> findByStatus(@Param("status") int status);

}

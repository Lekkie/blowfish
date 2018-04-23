package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.TranType;
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
public interface TranTypeRepository extends JpaRepository<TranType, String> {

    Optional<TranType> findByTranTypeId(@Param("tranTypeId") Long tranTypeId);
    Optional<TranType> findByCodeAllIgnoringCase(@Param("code") String code);
    @Query("FROM TranType n WHERE n.status = :status")
    Optional<List<TranType>> findByStatus(@Param("status") int status);


}

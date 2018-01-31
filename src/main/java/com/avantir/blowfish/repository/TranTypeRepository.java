package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.TranType;
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
public interface TranTypeRepository extends JpaRepository<TranType, String> {

    //@Cacheable(value = "endpointById")
    TranType findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    TranType findByCodeAllIgnoringCase(@Param("code") String code);
    @Query("FROM Node n WHERE n.status = :status")
    List<TranType> findByStatus(@Param("status") int status);


}

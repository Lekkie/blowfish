package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.Bin;
import org.springframework.cache.annotation.Cacheable;
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
public interface AcquirerRepository extends JpaRepository<Acquirer, Long> {

    //@Cacheable(value = "endpointById")
    Acquirer findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    Acquirer findByCodeAllIgnoringCase(@Param("code") String code);
    @Query("FROM Acquirer n WHERE n.status = :status")
    List<Acquirer> findByStatus(@Param("status") int status);


}

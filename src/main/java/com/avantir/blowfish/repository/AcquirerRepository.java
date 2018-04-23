package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.Acquirer;
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
public interface AcquirerRepository extends JpaRepository<Acquirer, Long> {

    //@Cacheable(value = "endpointById")
    Optional<Acquirer> findByAcquirerId(@Param("acquirerId") Long acquirerId);
    //@Cacheable(value = "endpointByName")
    Optional<Acquirer> findByCodeAllIgnoringCase(@Param("code") String code);
    @Query("FROM Acquirer n WHERE n.status = :status")
    Optional<List<Acquirer>> findByStatus(@Param("status") int status);


}

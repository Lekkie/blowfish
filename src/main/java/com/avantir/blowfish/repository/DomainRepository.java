package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.Domain;
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
public interface DomainRepository extends JpaRepository<Domain, Long> {

    //@Cacheable(value = "endpointById")
    Optional<Domain> findByDomainId(@Param("domainId") Long domainId);
    Optional<Domain> findOneByCode(String code);
    @Query("FROM Domain n WHERE n.status = :status")
    Optional<List<Domain>> findByStatus(@Param("status") int status);


}

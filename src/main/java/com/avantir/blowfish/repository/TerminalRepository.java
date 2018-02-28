package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.Terminal;
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
public interface TerminalRepository extends JpaRepository<Terminal, Long> {

    //@Cacheable(value = "endpointById")
    Terminal findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    Terminal findByCodeAllIgnoringCase(@Param("code") String code);
    Terminal findBySerialNoAllIgnoringCase(@Param("serialNo") String serialNo);
    @Query("FROM Terminal n WHERE n.status = :status")
    List<Terminal> findByStatus(@Param("status") int status);


}

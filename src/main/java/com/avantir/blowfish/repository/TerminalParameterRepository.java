package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.Terminal;
import com.avantir.blowfish.model.TerminalParameter;
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
public interface TerminalParameterRepository extends JpaRepository<TerminalParameter, Long> {

    //@Cacheable(value = "endpointById")
    TerminalParameter findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    TerminalParameter findByNameAllIgnoringCase(@Param("name") String name);
    @Query("FROM TerminalParameter n WHERE n.status = :status")
    List<TerminalParameter> findByStatus(@Param("status") int status);


}

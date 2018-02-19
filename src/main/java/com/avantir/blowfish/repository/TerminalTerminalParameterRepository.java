package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.MerchantTerminalParameter;
import com.avantir.blowfish.model.TerminalTerminalParameter;
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
public interface TerminalTerminalParameterRepository extends JpaRepository<TerminalTerminalParameter, Long> {

    //@Cacheable(value = "endpointById")
    TerminalTerminalParameter findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    TerminalTerminalParameter findByTerminalId(@Param("terminalId") Long terminalId);
    List<TerminalTerminalParameter> findByTerminalParameterId(@Param("terminalParameterId") Long terminalParameterId);
    @Query("FROM TerminalTerminalParameter t WHERE t.terminalId = :terminalId AND t.terminalParameterId = :terminalParameterId")
    TerminalTerminalParameter findByTerminalIdTerminalParameterId(@Param("terminalId") Long terminalId, @Param("terminalParameterId") Long terminalParameterId);


}

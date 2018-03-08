package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.TerminalTermParam;
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
public interface TerminalTermParamRepository extends JpaRepository<TerminalTermParam, Long> {

    //@Cacheable(value = "endpointById")
    @Query("FROM TerminalTermParam t WHERE t.terminalTermParamId = :terminalTermParamId")
    TerminalTermParam findByTerminalTermParamId(@Param("terminalTermParamId") Long terminalTermParamId);
    //@Cacheable(value = "endpointByName")
    TerminalTermParam findByTerminalId(@Param("terminalId") Long terminalId);
    List<TerminalTermParam> findByTermParamId(@Param("termParamId") Long termParamId);
    @Query("FROM TerminalTermParam t WHERE t.terminalId = :terminalId AND t.termParamId = :termParamId")
    TerminalTermParam findByTerminalIdTermParamId(@Param("terminalId") Long terminalId, @Param("termParamId") Long termParamId);


}

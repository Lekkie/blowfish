package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.Terminal;
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
public interface TerminalRepository extends JpaRepository<Terminal, Long> {

    Optional<Terminal> findByTerminalId(@Param("terminalId") Long terminalId);
    Optional<Terminal> findByCodeAllIgnoringCase(@Param("code") String code);
    Optional<Terminal> findBySerialNoAllIgnoringCase(@Param("serialNo") String serialNo);
    @Query("FROM Terminal n WHERE n.status = :status")
    Optional<List<Terminal>> findByStatus(@Param("status") int status);


}

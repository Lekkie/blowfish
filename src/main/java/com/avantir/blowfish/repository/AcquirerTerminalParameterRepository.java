package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.AcquirerBin;
import com.avantir.blowfish.model.AcquirerTerminalParameter;
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
public interface AcquirerTerminalParameterRepository extends JpaRepository<AcquirerTerminalParameter, Long> {

    //@Cacheable(value = "endpointById")
    AcquirerTerminalParameter findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    AcquirerTerminalParameter findByAcquirerId(@Param("acquirerId") Long acquirerId);
    List<AcquirerTerminalParameter> findByTerminalParameterId(@Param("terminalParameterId") Long terminalParameterId);
    @Query("FROM AcquirerTerminalParameter a WHERE a.acquirerId = :acquirerId AND a.terminalParameterId = :terminalParameterId")
    AcquirerTerminalParameter findByAcquirerIdTerminalParameterId(@Param("acquirerId") Long acquirerId, @Param("terminalParameterId") Long terminalParameterId);


}

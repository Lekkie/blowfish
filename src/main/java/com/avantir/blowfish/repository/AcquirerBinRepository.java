package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.Acquirer;
import com.avantir.blowfish.model.AcquirerBin;
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
public interface AcquirerBinRepository extends JpaRepository<AcquirerBin, Long> {

    //@Cacheable(value = "endpointById")
    //AcquirerBin findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    List<AcquirerBin> findByAcquirerId(@Param("acquirerId") Long acquirerId);
    AcquirerBin findByBinId(@Param("binId") Long binId);
    AcquirerBin findByAcquirerIdBinId(@Param("acquirerId") Long acquirerId, @Param("binId") Long binId);


}

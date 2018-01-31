package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.AcquirerMerchant;
import com.avantir.blowfish.model.AcquirerTranType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface AcquirerTranTypeRepository extends JpaRepository<AcquirerTranType, Long> {

    //@Cacheable(value = "endpointById")
    AcquirerTranType findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    List<AcquirerTranType> findByAcquirerId(@Param("acquirerId") Long acquirerId);
    List<AcquirerTranType> findByTranTypeId(@Param("tranTypeId") Long tranTypeId);
    AcquirerTranType findByAcquirerIdTranTypeId(@Param("acquirerId") Long acquirerId, @Param("tranTypeId") Long tranTypeId);


}

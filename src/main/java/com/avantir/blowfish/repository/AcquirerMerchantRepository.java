package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.AcquirerBin;
import com.avantir.blowfish.model.AcquirerMerchant;
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
public interface AcquirerMerchantRepository extends JpaRepository<AcquirerMerchant, Long> {

    //@Cacheable(value = "endpointById")
    AcquirerMerchant findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    List<AcquirerMerchant> findByAcquirerId(@Param("acquirerId") Long acquirerId);
    AcquirerMerchant findByMerchantId(@Param("merchantId") Long merchantId);
    AcquirerMerchant findByAcquirerIdMerchantId(@Param("acquirerId") Long acquirerId, @Param("merchantId") Long merchantId);


}

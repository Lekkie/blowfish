package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.AcquirerMerchant;
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
public interface AcquirerMerchantRepository extends JpaRepository<AcquirerMerchant, Long> {

    //@Cacheable(value = "endpointById")
    Optional<AcquirerMerchant> findByAcquirerMerchantId(@Param("acquirerMerchantId") Long acquirerMerchantId);
    //@Cacheable(value = "endpointByName")
    Optional<List<AcquirerMerchant>> findByAcquirerId(@Param("acquirerId") Long acquirerId);
    Optional<AcquirerMerchant> findByMerchantId(@Param("merchantId") Long merchantId);
    @Query("FROM AcquirerMerchant a WHERE a.acquirerId = :acquirerId AND a.merchantId = :merchantId")
    Optional<AcquirerMerchant> findByAcquirerIdMerchantId(@Param("acquirerId") Long acquirerId, @Param("merchantId") Long merchantId);


}

package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.AcquirerMerchant;
import com.avantir.blowfish.model.AcquirerMerchantBin;
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
public interface AcquirerMerchantBinRepository extends JpaRepository<AcquirerMerchantBin, Long> {

    //@Cacheable(value = "endpointById")
    AcquirerMerchantBin findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    List<AcquirerMerchantBin> findByAcquirerId(@Param("acquirerId") Long acquirerId);
    List<AcquirerMerchantBin> findByMerchantId(@Param("merchantId") Long merchantId);
    List<AcquirerMerchantBin> findByBinId(@Param("binId") Long binId);
    List<AcquirerMerchantBin> findByAcquirerIdMerchantId(@Param("acquirerId") Long acquirerId, @Param("merchantId") Long merchantId);
    List<AcquirerMerchantBin> findByAcquirerIdBinId(@Param("acquirerId") Long acquirerId, @Param("binId") Long binId);
    AcquirerMerchantBin findByMerchantIdBinId(@Param("merchantId") Long merchantId, @Param("binId") Long binId);
    AcquirerMerchantBin findByAcquirerIdMerchantIdBinId(@Param("acquirerId") Long acquirerId, @Param("merchantId") Long merchantId, @Param("binId") Long binId);


}

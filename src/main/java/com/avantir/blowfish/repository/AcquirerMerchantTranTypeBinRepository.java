package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.AcquirerMerchantTranTypeBin;
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
public interface AcquirerMerchantTranTypeBinRepository extends JpaRepository<AcquirerMerchantTranTypeBin, Long> {

    //@Cacheable(value = "endpointById")
    @Query("FROM AcquirerMerchantTranTypeBin a WHERE a.acquirerMerchantTranTypeBinId = :acquirerMerchantTranTypeBinId")
    Optional<AcquirerMerchantTranTypeBin> findByAcquirerMerchantTranTypeBinId(@Param("acquirerMerchantTranTypeBinId") Long acquirerMerchantTranTypeBinId);
    //@Cacheable(value = "endpointByName")
    Optional<List<AcquirerMerchantTranTypeBin>> findByAcquirerId(@Param("acquirerId") Long acquirerId);
    Optional<List<AcquirerMerchantTranTypeBin>> findByMerchantId(@Param("merchantId") Long merchantId);
    Optional<List<AcquirerMerchantTranTypeBin>> findByTranTypeId(@Param("tranTypeId") Long tranTypeId);
    Optional<List<AcquirerMerchantTranTypeBin>> findByBinId(@Param("binId") Long binId);
    //List<AcquirerMerchantTranTypeBin> findByAcquirerIdMerchantId(@Param("acquirerId") Long acquirerId, @Param("merchantId") Long merchantId);
    //List<AcquirerMerchantTranTypeBin> findByAcquirerIdTranTypeId(@Param("acquirerId") Long acquirerId, @Param("tranTypeId") Long tranTypeId);
    //AcquirerMerchantTranTypeBin findByMerchantIdTranTypeId(@Param("merchantId") Long merchantId, @Param("tranTypeId") Long tranTypeId);
    //List<AcquirerMerchantTranTypeBin> findByAcquirerIdMerchantIdTranTypeId(@Param("acquirerId") Long acquirerId, @Param("merchantId") Long merchantId, @Param("tranTypeId") Long tranTypeId);
    @Query("FROM AcquirerMerchantTranTypeBin a WHERE a.acquirerId = :acquirerId AND a.merchantId = :merchantId AND a.tranTypeId = :tranTypeId AND a.binId = :binId")
    Optional<AcquirerMerchantTranTypeBin> findByAcquirerIdMerchantIdTranTypeIdBinId(@Param("acquirerId") Long acquirerId, @Param("merchantId") Long merchantId, @Param("tranTypeId") Long tranTypeId, @Param("binId") Long binId);

}

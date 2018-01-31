package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.MerchantTerminal;
import com.avantir.blowfish.model.MerchantTranType;
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
public interface MerchantTranTypeRepository extends JpaRepository<MerchantTranType, Long> {

    //@Cacheable(value = "endpointById")
    MerchantTranType findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    List<MerchantTranType> findByMerchantId(@Param("merchantId") Long merchantId);
    List<MerchantTranType> findByTranTypeId(@Param("tranTypeId") Long tranTypeId);
    MerchantTranType findByMerchantIdTranTypeId(@Param("merchantId") Long merchantId, @Param("tranTypeId") Long tranTypeId);


}

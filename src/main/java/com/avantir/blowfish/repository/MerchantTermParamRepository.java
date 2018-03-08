package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.MerchantTermParam;
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
public interface MerchantTermParamRepository extends JpaRepository<MerchantTermParam, Long> {

    @Query("FROM MerchantTermParam m WHERE m.merchantTermParamId = :merchantTermParamId")
    MerchantTermParam findByMerchantTermParamId(@Param("merchantTermParamId") Long merchantTermParamId);
    //@Cacheable(value = "endpointByName")
    MerchantTermParam findByMerchantId(@Param("merchantId") Long merchantId);
    List<MerchantTermParam> findByTermParamId(@Param("termParamId") Long termParamId);
    @Query("FROM MerchantTermParam m WHERE m.merchantId = :merchantId AND m.termParamId = :termParamId")
    MerchantTermParam findByMerchantIdTermParamId(@Param("merchantId") Long merchantId, @Param("termParamId") Long termParamId);


}

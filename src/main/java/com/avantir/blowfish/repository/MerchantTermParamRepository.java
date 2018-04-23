package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.MerchantTermParam;
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
public interface MerchantTermParamRepository extends JpaRepository<MerchantTermParam, Long> {

    @Query("FROM MerchantTermParam m WHERE m.merchantTermParamId = :merchantTermParamId")
    Optional<MerchantTermParam> findByMerchantTermParamId(@Param("merchantTermParamId") Long merchantTermParamId);
    Optional<MerchantTermParam> findByMerchantId(@Param("merchantId") Long merchantId);
    Optional<List<MerchantTermParam>> findByTermParamId(@Param("termParamId") Long termParamId);
    @Query("FROM MerchantTermParam m WHERE m.merchantId = :merchantId AND m.termParamId = :termParamId")
    Optional<MerchantTermParam> findByMerchantIdTermParamId(@Param("merchantId") Long merchantId, @Param("termParamId") Long termParamId);


}

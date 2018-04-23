package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.MerchantBin;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface MerchantBinRepository extends JpaRepository<MerchantBin, Long> {

    Optional<MerchantBin> findByMerchantBinId(@Param("merchantBinId") Long merchantBinId);
    Optional<List<MerchantBin>> findByMerchantId(@Param("merchantId") Long merchantId);
    Optional<List<MerchantBin>> findByBinId(@Param("binId") Long binId);


}

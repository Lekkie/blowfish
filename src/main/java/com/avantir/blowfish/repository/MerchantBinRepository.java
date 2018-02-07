package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.AcquirerBin;
import com.avantir.blowfish.model.MerchantBin;
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
public interface MerchantBinRepository extends JpaRepository<MerchantBin, Long> {

    //@Cacheable(value = "endpointById")
    //MerchantBin findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    List<MerchantBin> findByMerchantId(@Param("merchantId") Long merchantId);
    List<MerchantBin> findByBinId(@Param("binId") Long binId);


}

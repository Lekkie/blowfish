package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.AcquirerMerchantBin;
import com.avantir.blowfish.model.MerchantTerminalBin;
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
public interface MerchantTerminalBinRepository extends JpaRepository<MerchantTerminalBin, Long> {

    //@Cacheable(value = "endpointById")
    MerchantTerminalBin findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    List<MerchantTerminalBin> findByMerchantId(@Param("merchantId") Long merchantId);
    List<MerchantTerminalBin> findByTerminalId(@Param("terminalId") Long terminalId);
    List<MerchantTerminalBin> findByBinId(@Param("binId") Long binId);
    List<MerchantTerminalBin> findByMerchantIdTerminalId(@Param("merchantId") Long merchantId, @Param("terminalId") Long terminalId);
    List<MerchantTerminalBin> findByMerchantIdBinId(@Param("merchantId") Long merchantId, @Param("binId") Long binId);
    MerchantTerminalBin findByTerminalIdBinId(@Param("terminalId") Long terminalId, @Param("binId") Long binId);
    MerchantTerminalBin findByMerchantIdTerminalIdBinId(@Param("merchantId") Long merchantId, @Param("terminalId") Long terminalId, @Param("binId") Long binId);


}

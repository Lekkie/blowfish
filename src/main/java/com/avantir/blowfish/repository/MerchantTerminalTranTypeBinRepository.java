package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.MerchantTerminalTranTypeBin;
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
public interface MerchantTerminalTranTypeBinRepository extends JpaRepository<MerchantTerminalTranTypeBin, Long> {

    //@Cacheable(value = "endpointById")
    MerchantTerminalTranTypeBin findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    List<MerchantTerminalTranTypeBin> findByMerchantId(@Param("merchantId") Long merchantId);
    List<MerchantTerminalTranTypeBin> findByTerminalId(@Param("terminalId") Long terminalId);
    List<MerchantTerminalTranTypeBin> findByTranTypeId(@Param("tranTypeId") Long tranTypeId);
    List<MerchantTerminalTranTypeBin> findByBinId(@Param("binId") Long binId);
    //List<MerchantTerminalTranTypeBin> findByMerchantIdTerminalId(@Param("merchantId") Long merchantId, @Param("terminalId") Long terminalId);
    //List<MerchantTerminalTranTypeBin> findByMerchantIdTranTypeId(@Param("merchantId") Long merchantId, @Param("tranTypeId") Long tranTypeId);
    //List<MerchantTerminalTranTypeBin> findByTerminalIdTranTypeId(@Param("terminalId") Long terminalId, @Param("tranTypeId") Long tranTypeId);
    //List<MerchantTerminalTranTypeBin> findByMerchantIdTerminalIdTranTypeId(@Param("merchantId") Long merchantId, @Param("terminalId") Long terminalId, @Param("tranTypeId") Long tranTypeId);
    @Query("FROM MerchantTerminalTranTypeBin m WHERE m.merchantId = :merchantId AND m.terminalId = :terminalId AND m.tranTypeId = :tranTypeId AND m.binId = :binId")
    MerchantTerminalTranTypeBin findByMerchantIdTerminalIdTranTypeIdBinId(@Param("merchantId") Long merchantId, @Param("terminalId") Long terminalId, @Param("tranTypeId") Long tranTypeId, @Param("binId") Long binId);

}

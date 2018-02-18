package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.MerchantBin;
import com.avantir.blowfish.model.MerchantTerminal;
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
public interface MerchantTerminalRepository extends JpaRepository<MerchantTerminal, Long> {

    //@Cacheable(value = "endpointById")
    MerchantTerminal findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    List<MerchantTerminal> findByMerchantId(@Param("merchantId") Long merchantId);
    MerchantTerminal findByTerminalId(@Param("terminalId") Long terminalId);
    MerchantTerminal findByMerchantIdTerminalId(@Param("merchantId") Long merchantId, @Param("terminalId") Long terminalId);


}

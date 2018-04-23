package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.MerchantTerminal;
import org.springframework.cache.annotation.CachePut;
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
public interface MerchantTerminalRepository extends JpaRepository<MerchantTerminal, Long> {

    Optional<MerchantTerminal> findByMerchantTerminalId(@Param("merchantTerminalId") long merchantTerminalId);
    Optional<List<MerchantTerminal>> findByMerchantId(@Param("merchantId") long merchantId);
    Optional<MerchantTerminal> findByTerminalId(@Param("terminalId") long terminalId);
    @Query("FROM MerchantTerminal m WHERE m.merchantId = :merchantId AND m.terminalId = :terminalId")
    Optional<MerchantTerminal> findByMerchantIdTerminalId(@Param("merchantId") long merchantId, @Param("terminalId") Long terminalId);


}

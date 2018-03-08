package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.Bin;
import com.avantir.blowfish.model.Transaction;
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
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    //@Cacheable(value = "endpointById")
    Transaction findByTransactionId(@Param("transactionId") Long transactionId);
    //@Cacheable(value = "endpointByName")
    @Query("FROM Transaction t WHERE t.msgType = :msgType " +
            "AND t.stan = :stan " +
            "AND t.transmissionDateTime = :transmissionDateTime " +
            "AND t.acquirerInstId = :acquirerInstId " +
            "AND t.forwardingInstId = :forwardingInstId " +
            "AND t.terminalId = :terminalId " +
            "AND t.merchantId = :merchantId ")
    Transaction findByMsgTypeStanTransmissionDateTimeAcquirerInstIdForwardingInstIdTerminalIdMerchantIdAllIgnoringCase(@Param("msgType") String msgType,
                                         @Param("stan") String stan,
                                         @Param("transmissionDateTime") String transmissionDateTime,
                                         @Param("acquirerInstId") String acquirerInstId,
                                         @Param("forwardingInstId") String forwardingInstId,
                                         @Param("terminalId") String terminalId,
                                         @Param("merchantId") String merchantId);




}

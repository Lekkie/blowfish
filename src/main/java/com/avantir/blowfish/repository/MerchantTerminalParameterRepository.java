package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.AcquirerTerminalParameter;
import com.avantir.blowfish.model.Merchant;
import com.avantir.blowfish.model.MerchantTerminalParameter;
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
public interface MerchantTerminalParameterRepository extends JpaRepository<MerchantTerminalParameter, Long> {

    //@Cacheable(value = "endpointById")
    MerchantTerminalParameter findById(@Param("id") Long id);
    //@Cacheable(value = "endpointByName")
    MerchantTerminalParameter findByMerchantId(@Param("merchantId") Long merchantId);
    List<MerchantTerminalParameter> findByTerminalParameterId(@Param("terminalParameterId") Long terminalParameterId);
    @Query("FROM MerchantTerminalParameter m WHERE m.merchantId = :merchantId AND m.terminalParameterId = :terminalParameterId")
    MerchantTerminalParameter findByMerchantIdTerminalParameterId(@Param("merchantId") Long merchantId, @Param("terminalParameterId") Long terminalParameterId);


}

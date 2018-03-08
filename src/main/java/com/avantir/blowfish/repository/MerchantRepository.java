package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.Merchant;
import com.avantir.blowfish.model.Terminal;
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
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    //@Cacheable(value = "endpointById")
    Merchant findByMerchantId(@Param("merchantId") Long merchantId);
    //@Cacheable(value = "endpointByName")
    Merchant findByCodeAllIgnoringCase(@Param("code") String code);
    @Query("FROM Merchant n WHERE n.status = :status")
    List<Merchant> findByStatus(@Param("status") int status);


}

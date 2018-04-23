package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.Merchant;
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
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Optional<Merchant> findByMerchantId(@Param("merchantId") Long merchantId);
    Optional<Merchant> findByCodeAllIgnoringCase(@Param("code") String code);
    @Query("FROM Merchant n WHERE n.status = :status")
    Optional<List<Merchant>> findByStatus(@Param("status") int status);


}

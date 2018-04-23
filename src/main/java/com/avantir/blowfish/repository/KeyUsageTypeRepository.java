package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.KeyCryptographicType;
import com.avantir.blowfish.entity.KeyUsageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface KeyUsageTypeRepository extends JpaRepository<KeyUsageType, String> {

    Optional<KeyUsageType> findByKeyUsageTypeId(@Param("keyUsageTypeId") Long keyUsageTypeId);
    Optional<KeyUsageType> findByCodeAllIgnoringCase(@Param("code") String code);


}

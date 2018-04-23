package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.KeyCryptographicType;
import com.avantir.blowfish.entity.TranType;
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
public interface KeyCryptographicTypeRepository extends JpaRepository<KeyCryptographicType, String> {

    Optional<KeyCryptographicType> findByKeyCryptographicTypeId(@Param("keyCryptographicTypeId") Long keyCryptographicTypeId);
    Optional<KeyCryptographicType> findByCodeAllIgnoringCase(@Param("code") String code);


}

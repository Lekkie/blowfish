package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.Key;
import com.avantir.blowfish.entity.KeyMap;
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
public interface KeyMapRepository extends JpaRepository<KeyMap, Long> {

    Optional<KeyMap> findByKeyMapId(@Param("keyMapId") Long keyMapId);
    Optional<KeyMap> findByCode(@Param("code") String code);


}

package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.ActiveHsm;
import com.avantir.blowfish.entity.KeyMap;
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
public interface ActiveHsmRepository extends JpaRepository<ActiveHsm, Long> {

    Optional<ActiveHsm> findByActiveHsmId(@Param("activeHsmId") Long activeHsmId);
    Optional<ActiveHsm> findByCode(@Param("code") String code);

}

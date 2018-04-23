package com.avantir.blowfish.repository;

import com.avantir.blowfish.entity.TMSKeyOrigDataElem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface TMSKeyOrigDataElemRepository extends JpaRepository<TMSKeyOrigDataElem, String> {

    @Query("FROM TMSKeyOrigDataElem t WHERE t.tmsKeyOrigDataElemId = :tmsKeyOrigDataElemId")
    Optional<TMSKeyOrigDataElem> findByTmsKeyOrigDataElemId(@Param("tmsKeyOrigDataElemId") Long tmsKeyOrigDataElemId);
    Optional<TMSKeyOrigDataElem> findByOriginalDataElementAllIgnoringCase(@Param("originalDataElement") String originalDataElement);

}

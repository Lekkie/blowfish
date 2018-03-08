package com.avantir.blowfish.repository;

import com.avantir.blowfish.model.TMSKeyOrigDataElem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Repository
@Transactional
public interface TMSKeyOrigDataElemRepository extends JpaRepository<TMSKeyOrigDataElem, String> {

    @Query("FROM TMSKeyOrigDataElem t WHERE t.tmsKeyOrigDataElemId = :tmsKeyOrigDataElemId")
    TMSKeyOrigDataElem findByTmsKeyOrigDataElemId(@Param("tmsKeyOrigDataElemId") Long tmsKeyOrigDataElemId);
    //@Cacheable(value = "endpointByName")
    TMSKeyOrigDataElem findByOriginalDataElementAllIgnoringCase(@Param("originalDataElement") String originalDataElement);

}

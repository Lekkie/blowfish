package com.avantir.blowfish.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 18/02/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_acquirers_terminal_parameters")
@SuppressWarnings("serial")
public class AcquirerTermParam extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long acquirerTermParamId;
    @Column(name = "acquirer_id", nullable = false, unique = true)
    Long acquirerId;
    @Column(name = "term_param_id", nullable = false)
    Long termParamId;

    public Long getAcquirerTermParamId() {
        return acquirerTermParamId;
    }

    public void setAcquirerTermParamId(Long acquirerTermParamId) {
        this.acquirerTermParamId = acquirerTermParamId;
    }

    public Long getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(Long acquirerId) {
        this.acquirerId = acquirerId;
    }

    public Long getTermParamId() {
        return termParamId;
    }

    public void setTermParamId(Long termParamId) {
        this.termParamId = termParamId;
    }
}

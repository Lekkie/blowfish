package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_acquirers_tran_types")
@SuppressWarnings("serial")
public class AcquirerTranType extends BaseModel{

    private static final long serialVersionUID = 1L;

    /*
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    */

    @Column(name = "acquirer_id", nullable = false)
    Long acquirerId;
    @Column(name = "tran_type_id", nullable = false)
    Long tranTypeId;


    public Long getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(Long acquirerId) {
        this.acquirerId = acquirerId;
    }

    public Long getTranTypeId() {
        return tranTypeId;
    }

    public void setTranTypeId(Long tranTypeId) {
        this.tranTypeId = tranTypeId;
    }
}

package com.avantir.blowfish.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_acquirers_bins")
@SuppressWarnings("serial")
public class AcquirerBin extends BaseModel{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long acquirerBinId;
    @Column(name = "acquirer_id", nullable = false)
    Long acquirerId;
    @Column(name = "bin_id", nullable = false, unique = true)
    Long binId;


    public Long getAcquirerBinId() {
        return acquirerBinId;
    }

    public void setAcquirerBinId(Long acquirerBinId) {
        this.acquirerBinId = acquirerBinId;
    }

    public Long getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(Long acquirerId) {
        this.acquirerId = acquirerId;
    }

    public Long getBinId() {
        return binId;
    }

    public void setBinId(Long binId) {
        this.binId = binId;
    }
}

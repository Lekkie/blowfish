package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_acquirers_merchants_tran_types_bins")
@SuppressWarnings("serial")
public class AcquirerMerchantTranTypeBin extends BaseModel{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "acquirer_id", nullable = false)
    Long acquirerId;
    @Column(name = "merchant_id", nullable = false)
    Long merchantId;
    @Column(name = "tran_type_id", nullable = false)
    Long tranTypeId;
    @Column(name = "bin_id", nullable = false)
    Long binId;
    // merchant id & tran_type are unique together

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(Long acquirerId) {
        this.acquirerId = acquirerId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getTranTypeId() {
        return tranTypeId;
    }

    public void setTranTypeId(Long tranTypeId) {
        this.tranTypeId = tranTypeId;
    }

    public Long getBinId() {
        return binId;
    }

    public void setBinId(Long binId) {
        this.binId = binId;
    }
}

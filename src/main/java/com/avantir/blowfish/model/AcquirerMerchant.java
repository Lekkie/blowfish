package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_acquirers_merchants")
@SuppressWarnings("serial")
public class AcquirerMerchant extends BaseModel{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long acquirerMerchantId;
    @Column(name = "acquirer_id", nullable = false)
    Long acquirerId;
    @Column(name = "merchant_id", nullable = false, unique = true)
    Long merchantId;


    public Long getAcquirerMerchantId() {
        return acquirerMerchantId;
    }

    public void setAcquirerMerchantId(Long acquirerMerchantId) {
        this.acquirerMerchantId = acquirerMerchantId;
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
}

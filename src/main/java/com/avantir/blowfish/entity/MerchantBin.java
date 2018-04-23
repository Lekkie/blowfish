package com.avantir.blowfish.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_merchants_bins")
@SuppressWarnings("serial")
public class MerchantBin extends BaseModel{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long merchantBinId;
    @Column(name = "merchant_id", nullable = false)
    Long merchantId;
    @Column(name = "bin_id", nullable = false)
    Long binId;

    public Long getMerchantBinId() {
        return merchantBinId;
    }

    public void setMerchantBinId(Long merchantBinId) {
        this.merchantBinId = merchantBinId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getBinId() {
        return binId;
    }

    public void setBinId(Long binId) {
        this.binId = binId;
    }
}

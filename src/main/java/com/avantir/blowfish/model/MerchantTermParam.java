package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 18/02/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_merchants_terminal_parameters")
@SuppressWarnings("serial")
public class MerchantTermParam extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long merchantTermParamId;
    @Column(name = "merchant_id", nullable = false, unique = true)
    Long merchantId;
    @Column(name = "term_param_id", nullable = false)
    Long termParamId;


    public Long getMerchantTermParamId() {
        return merchantTermParamId;
    }

    public void setMerchantTermParamId(Long merchantTermParamId) {
        this.merchantTermParamId = merchantTermParamId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getTermParamId() {
        return termParamId;
    }

    public void setTermParamId(Long termParamId) {
        this.termParamId = termParamId;
    }
}

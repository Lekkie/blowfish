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
public class MerchantTerminalParameter extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "merchant_id", nullable = false)
    Long merchantId;
    @Column(name = "terminal_parameter_id", nullable = false)
    Long terminalParameterId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getTerminalParameterId() {
        return terminalParameterId;
    }

    public void setTerminalParameterId(Long terminalParameterId) {
        this.terminalParameterId = terminalParameterId;
    }
}

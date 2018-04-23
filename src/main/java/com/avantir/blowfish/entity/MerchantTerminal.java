package com.avantir.blowfish.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

/**
 * Created by lekanomotayo on 07/01/2018.
 */
@Entity
@DynamicInsert
@Table(name = "tbl_merchants_terminals")
@SuppressWarnings("serial")
public class MerchantTerminal extends BaseModel{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long merchantTerminalId;
    @Column(name = "merchant_id", nullable = false)
    Long merchantId;
    @Column(name = "terminal_id", nullable = false, unique = true)
    Long terminalId;

    public Long getMerchantTerminalId() {
        return merchantTerminalId;
    }

    public void setMerchantTerminalId(Long merchantTerminalId) {
        this.merchantTerminalId = merchantTerminalId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }
}

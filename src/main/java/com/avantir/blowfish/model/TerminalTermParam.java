package com.avantir.blowfish.model;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 18/02/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_terminals_terminal_parameters")
@SuppressWarnings("serial")
public class TerminalTermParam extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long terminalTermParamId;
    @Column(name = "terminal_id", nullable = false)
    Long terminalId;
    @Column(name = "term_param_id", nullable = false)
    Long termParamId;

    public Long getTerminalTermParamId() {
        return terminalTermParamId;
    }

    public void setTerminalTermParamId(Long terminalTermParamId) {
        this.terminalTermParamId = terminalTermParamId;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public Long getTermParamId() {
        return termParamId;
    }

    public void setTermParamId(Long termParamId) {
        this.termParamId = termParamId;
    }
}

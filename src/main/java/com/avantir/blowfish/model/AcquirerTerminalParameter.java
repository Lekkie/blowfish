package com.avantir.blowfish.model;

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
public class AcquirerTerminalParameter extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "acquirer_id", nullable = false)
    Long acquirerId;
    @Column(name = "terminal_parameter_id", nullable = false)
    Long terminalParameterId;


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

    public Long getTerminalParameterId() {
        return terminalParameterId;
    }

    public void setTerminalParameterId(Long terminalParameterId) {
        this.terminalParameterId = terminalParameterId;
    }
}

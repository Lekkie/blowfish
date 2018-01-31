package com.avantir.blowfish.messaging.model;

import com.avantir.blowfish.model.BaseModel;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 14/01/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_nodes")
@SuppressWarnings("serial")
public class DefaultSinkNode extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name = "DEFAULT";
    @Column(name = "sink_node_id", nullable = false, unique = true)
    private Long sinkNodeId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSinkNodeId() {
        return sinkNodeId;
    }

    public void setSinkNodeId(Long sinkNodeId) {
        this.sinkNodeId = sinkNodeId;
    }
}

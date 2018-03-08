package com.avantir.blowfish.messaging.model;

import com.avantir.blowfish.model.BaseModel;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_route_by_receiving_inst_id")
@SuppressWarnings("serial")
public class RouteByReceivingInst extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long routeByReceivingInstId;
    @Column(name = "receiving_inst_id", nullable = false, unique = true)
    private String receivingInstId;
    @Column(name = "sink_node_id", nullable = false)
    private Long sinkNodeId;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;

    public RouteByReceivingInst(){

    }


    public Long getRouteByReceivingInstId() {
        return routeByReceivingInstId;
    }

    public void setRouteByReceivingInstId(Long routeByReceivingInstId) {
        this.routeByReceivingInstId = routeByReceivingInstId;
    }

    public String getReceivingInstId() {
        return receivingInstId;
    }

    public void setReceivingInstId(String receivingInstId) {
        this.receivingInstId = receivingInstId;
    }

    public Long getSinkNodeId() {
        return sinkNodeId;
    }

    public void setSinkNodeId(Long sinkNodeId) {
        this.sinkNodeId = sinkNodeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

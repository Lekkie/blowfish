package com.avantir.blowfish.messaging.entity;

import com.avantir.blowfish.entity.BaseModel;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lekanomotayo on 01/01/2018.
 */

@Entity
@DynamicInsert
@Table(name = "tbl_sap_endpoints")
@SuppressWarnings("serial")
public class SAPEndpoint extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long sapEndpointId;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "src_node_id", nullable = true)
    private Long srcNodeId = 0L;
    @Column(name = "snk_node_id", nullable = true)
    private Long snkNodeId = 0L;
    @Column(name = "tcp_endpoint_id", nullable = false, unique = true)
    private Long tcpEndpointId = 0L;

    @Column(name = "iso_bridge_id", nullable = false)
    private Long isoBridgeId = 1L; // 0 - GenericBridge, 2 - PostBridge

    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1; // 0 - inactive, 1- active


    public Long getSapEndpointId() {
        return sapEndpointId;
    }

    public void setSapEndpointId(Long sapEndpointId) {
        this.sapEndpointId = sapEndpointId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSrcNodeId() {
        return srcNodeId;
    }

    public void setSrcNodeId(Long srcNodeId) {
        this.srcNodeId = srcNodeId;
    }

    public Long getSnkNodeId() {
        return snkNodeId;
    }

    public void setSnkNodeId(Long snkNodeId) {
        this.snkNodeId = snkNodeId;
    }

    public Long getTcpEndpointId() {
        return tcpEndpointId;
    }

    public void setTcpEndpointId(Long tcpEndpointId) {
        this.tcpEndpointId = tcpEndpointId;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIsoBridgeId() {
        return isoBridgeId;
    }

    public void setIsoBridgeId(Long isoBridgeId) {
        this.isoBridgeId = isoBridgeId;
    }
}

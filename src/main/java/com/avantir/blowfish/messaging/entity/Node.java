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
@Table(name = "tbl_nodes")
@SuppressWarnings("serial")
public class Node extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long nodeId;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "type", nullable = false)
    private int type; // 0 = src, 1 = sink
    @Column(name = "validate_exp_date", nullable = false)
    private boolean validateExpDate;
    @Column(name = "routing_group_id", nullable = false)
    private Long routingGroupId = 1L; // Only src nodes need routing group
    @Column(name = "key_id", nullable = true)
    private Long keyId;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "status", nullable = false)
    private int status = 1;

    public Node(){

    }


    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
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

    public Long getRoutingGroupId() {
        return routingGroupId;
    }

    public void setRoutingGroupId(Long routingGroupId) {
        this.routingGroupId = routingGroupId;
    }

    public boolean isValidateExpDate() {
        return validateExpDate;
    }

    public void setValidateExpDate(boolean validateExpDate) {
        this.validateExpDate = validateExpDate;
    }
}

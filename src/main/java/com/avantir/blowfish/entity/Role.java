package com.avantir.blowfish.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tbl_roles")
public class Role  extends BaseModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long roleId;
    @Column
    private String name;
    @Column
    @ElementCollection(targetClass=User.class)
    private Set<User> users;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles")
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}

package com.intergamma.inventory.entity;

import javax.persistence.*;

@Entity
@Table(name = "store", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Store {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

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
}

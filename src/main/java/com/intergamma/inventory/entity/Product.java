package com.intergamma.inventory.entity;

import javax.persistence.*;

@Entity
@Table(name = "product", uniqueConstraints = {@UniqueConstraint(columnNames = "productCode")})
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String productCode;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

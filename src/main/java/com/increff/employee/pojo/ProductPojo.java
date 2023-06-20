package com.increff.employee.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProductPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String barcode;

    private Integer brand_category;

    private String name;

    private Double mrp;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getBrand_category() {
        return brand_category;
    }

    public void setBrand_category(Integer brand_category) {
        this.brand_category = brand_category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

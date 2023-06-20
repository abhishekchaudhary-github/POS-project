package com.increff.employee.model;

public class ProductForm {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBrand_category() {
        return brand_category;
    }

    public void setBrand_category(Integer brand_category) {
        this.brand_category = brand_category;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }
}


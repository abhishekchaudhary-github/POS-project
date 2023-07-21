package com.increff.employee.model;

public class ErrorsBrand extends Errors{
    private String Brand;
    private String category;

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

package com.increff.employee.model;

public class OrderItem {
    private Double sellingPrice;
    private Integer quantity;
    private String productName;
    private Double amt;
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    //    private Integer revenue;
    private Integer orderItemId;

//    public String getBarcode() {
//        return barcode;
//    }
//
//    public void setBarcode(String barcode) {
//        this.barcode = barcode;
//    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

//    public Integer getRevenue() {
//        return revenue;
//    }
//
//    public void setRevenue(Integer revenue) {
//        this.revenue = revenue;
//    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

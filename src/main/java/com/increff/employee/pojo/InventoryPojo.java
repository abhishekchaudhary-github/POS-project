package com.increff.employee.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InventoryPojo {
    @Id
    private int id;

    private Integer quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}


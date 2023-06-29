package com.increff.employee.model;

import java.time.LocalDateTime;

public class SchedulerForm {
    private LocalDateTime date;
    private Integer invoiced_orders_count;
    private Integer invoiced_items_count;
    private Double total_revenue;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getInvoiced_items_count() {
        return invoiced_items_count;
    }

    public void setInvoiced_items_count(Integer invoiced_items_count) {
        this.invoiced_items_count = invoiced_items_count;
    }

    public Integer getInvoiced_orders_count() {
        return invoiced_orders_count;
    }

    public void setInvoiced_orders_count(Integer invoiced_orders_count) {
        this.invoiced_orders_count = invoiced_orders_count;
    }

    public Double getTotal_revenue() {
        return total_revenue;
    }

    public void setTotal_revenue(Double total_revenue) {
        this.total_revenue = total_revenue;
    }
}

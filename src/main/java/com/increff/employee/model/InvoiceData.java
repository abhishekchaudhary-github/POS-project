package com.increff.employee.model;
import org.springframework.stereotype.Service;

import java.util.List;


public class InvoiceData {
private Integer orderId;
private String placedDate;
private List<OrderItem> orderItemDataList;
private Double amount;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public List<OrderItem> getOrderItemDataList() {
		return orderItemDataList;
	}

	public void setOrderItemDataList(List<OrderItem> orderItemDataList) {
		this.orderItemDataList = orderItemDataList;
	}

	public String getPlacedDate() {
		return placedDate;
	}

	public void setPlacedDate(String placedDate) {
		this.placedDate = placedDate;
	}
}

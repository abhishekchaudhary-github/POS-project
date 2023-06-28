package com.increff.employee.service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public Integer add() throws ApiException {
        LocalDateTime timeOfOrder = timeOfOrderCreation();
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setTime(timeOfOrder);
        Integer orderId = dao.insert(orderPojo);
        return orderId;
    }

    @Transactional
    public List<OrderPojo> getAll() {
        return dao.selectAll();
    }

    protected static LocalDateTime timeOfOrderCreation() {
        LocalDateTime dateTime = LocalDateTime.now();
//        SimpleDateFormat createdTime = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//        Date currentDate = new Date();
//        String formattedDateTime = createdTime.format(currentDate);
        return dateTime;
    }
}
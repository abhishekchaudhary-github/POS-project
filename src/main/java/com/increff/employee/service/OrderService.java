package com.increff.employee.service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public Integer add() throws ApiException {
        String timeOfOrder = timeOfOrderCreation();
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setTime(timeOfOrder);
        Integer orderId = dao.insert(orderPojo);
        return orderId;
    }

    protected static String timeOfOrderCreation() {
        SimpleDateFormat createdTime = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date currentDate = new Date();
        String formattedDateTime = createdTime.format(currentDate);
        return formattedDateTime;
    }
}

package com.increff.employee.dao;

import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderService;
import helper.pojoHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReportDaoTest extends AbstractUnitTest {

    @Autowired
    ReportDao dao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderItemDao orderItemDao;

    @Test
    public void testSelectAllOrder() throws ApiException {
        LocalDateTime time1 = LocalDateTime.parse("2019-03-27T10:15:30");
        LocalDateTime time2 = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.parse("2021-03-27T10:15:30");
        LocalDateTime endTime = LocalDateTime.parse("2024-03-27T10:15:30");


        OrderPojo orderPojo = pojoHelper.makeOrderPojo(time1);
        OrderPojo orderPojo1 = pojoHelper.makeOrderPojo(time2);

        orderDao.insert(orderPojo);
        orderDao.insert(orderPojo1);

        List<OrderPojo> orderPojoList = dao.selectAllOrder(startTime,endTime);

        assertEquals(1,orderPojoList.size());
        assertEquals(time2,orderPojoList.get(0).getTime());
    }

    @Test
    public void testSelectOrderItem() throws ApiException {
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(1, 2, 3, 4.5);
        orderItemDao.insert(orderItemPojo);
        OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(2, 7, 2, 13.7);
        orderItemDao.insert(orderItemPojo1);
        OrderItemPojo orderItemPojo2 = pojoHelper.makeOrderItemPojo(1, 5, 23, 42.5);
        orderItemDao.insert(orderItemPojo2);

        List<OrderItemPojo> orderItemPojoList = dao.selectOrderItem(1);

        assertEquals(2,orderItemPojoList.size());
        assertEquals(new Integer(2),orderItemPojoList.get(0).getProductId());
        assertEquals(new Integer(3),orderItemPojoList.get(0).getQuantity());
        assertEquals(new Double(4.5),orderItemPojoList.get(0).getSellingPrice());
        assertEquals(new Integer(5),orderItemPojoList.get(1).getProductId());
        assertEquals(new Integer(23),orderItemPojoList.get(1).getQuantity());
        assertEquals(new Double(42.5),orderItemPojoList.get(1).getSellingPrice());
    }
}

package com.increff.employee.dao;

import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import helper.pojoHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OrderDaoTest extends AbstractUnitTest {

    @Autowired
    OrderDao dao;

    @Test
    public void testInsert() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        OrderPojo orderPojo = pojoHelper.makeOrderPojo(time);
        Integer id = dao.insert(orderPojo);
        OrderPojo orderPojo1 = dao.orderById(id);
        assertEquals(time,orderPojo1.getTime());
    }

    @Test
    public void testDelete() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        OrderPojo orderPojo = pojoHelper.makeOrderPojo(time);
        Integer id = dao.insert(orderPojo);
        dao.delete(id);
        assertNull(dao.orderById(id));
    }

    @Test
    public void testSelectAll() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        OrderPojo orderPojo = pojoHelper.makeOrderPojo(time);
        Integer id = dao.insert(orderPojo);
        List<OrderPojo> orderPojoList = dao.selectAll();
        assertEquals(1,orderPojoList.size());
        assertEquals(time,orderPojoList.get(0).getTime());
    }

    @Test
    public void testOrderById() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        OrderPojo orderPojo = pojoHelper.makeOrderPojo(time);
        Integer id = dao.insert(orderPojo);
        OrderPojo orderPojo1 = dao.orderById(id);
        assertEquals(time,orderPojo1.getTime());
    }

}

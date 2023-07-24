package com.increff.employee.dao;

import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import helper.pojoHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderItemDaoTest extends AbstractUnitTest {
    @Autowired
    OrderItemDao dao;

    @Test
    public void testInsert() throws ApiException {
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(1,2,3,4.5);
        dao.insert(orderItemPojo);
        List<OrderItemPojo> orderItemPojoList = dao.selectAll();
        assertEquals(new Integer(1),orderItemPojoList.get(0).getOrderId());
        assertEquals(new Integer(2),orderItemPojoList.get(0).getProductId());
        assertEquals(new Integer(3),orderItemPojoList.get(0).getQuantity());
        assertEquals(new Double(4.5),orderItemPojoList.get(0).getSellingPrice());
    }

    @Test
    public void testSelect() throws ApiException {
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(1,2,3,4.5);
        dao.insert(orderItemPojo);
        List<OrderItemPojo> orderItemPojoList = dao.selectAll();
        Integer id = orderItemPojoList.get(0).getId();
        OrderItemPojo orderItemPojo1 = dao.select(id);

        assertEquals(new Integer(1),orderItemPojo1.getOrderId());
        assertEquals(new Integer(2),orderItemPojo1.getProductId());
        assertEquals(new Integer(3),orderItemPojo1.getQuantity());
        assertEquals(new Double(4.5),orderItemPojo1.getSellingPrice());
    }

    @Test
    public void testSelectFromId() throws ApiException {
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(1,2,3,4.5);
        dao.insert(orderItemPojo);
        List<OrderItemPojo> orderItemPojoList = dao.selectFromId(1);
        assertEquals(new Integer(1),orderItemPojoList.get(0).getId());
        assertEquals(new Integer(2),orderItemPojoList.get(0).getProductId());
        assertEquals(new Integer(3),orderItemPojoList.get(0).getQuantity());
        assertEquals(new Double(4.5),orderItemPojoList.get(0).getSellingPrice());
    }

    @Test
    public void testSelectAll() throws ApiException {
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(1, 2, 3, 4.5);
        dao.insert(orderItemPojo);
        OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(2, 7, 2, 13.7);
        dao.insert(orderItemPojo1);
        List<OrderItemPojo> orderItemPojoList = dao.selectAll();
  //      if(orderItemPojoList.get(0).getOrderId()==1) {
            assertEquals(new Integer(1), orderItemPojoList.get(0).getOrderId());
            assertEquals(new Integer(2), orderItemPojoList.get(0).getProductId());
            assertEquals(new Integer(3), orderItemPojoList.get(0).getQuantity());
            assertEquals(new Double(4.5), orderItemPojoList.get(0).getSellingPrice());

            assertEquals(new Integer(2), orderItemPojoList.get(1).getOrderId());
            assertEquals(new Integer(7), orderItemPojoList.get(1).getProductId());
            assertEquals(new Integer(2), orderItemPojoList.get(1).getQuantity());
            assertEquals(new Double(13.7), orderItemPojoList.get(1).getSellingPrice());
   //     }
//        else {
//            assertEquals(new Integer(1), orderItemPojoList.get(1).getId());
//            assertEquals(new Integer(2), orderItemPojoList.get(1).getProductId());
//            assertEquals(new Integer(3), orderItemPojoList.get(1).getQuantity());
//            assertEquals(new Double(4.5), orderItemPojoList.get(1).getSellingPrice());
//
//            assertEquals(new Integer(2), orderItemPojoList.get(0).getId());
//            assertEquals(new Integer(7), orderItemPojoList.get(0).getProductId());
//            assertEquals(new Integer(2), orderItemPojoList.get(0).getQuantity());
//            assertEquals(new Double(13.7), orderItemPojoList.get(0).getSellingPrice());
//        }
    }

}

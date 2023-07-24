package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import helper.pojoHelper;
import io.swagger.models.auth.In;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryDaoTest extends AbstractUnitTest {
    @Autowired
    InventoryDao dao;

    @Test
    public void testInsert() throws ApiException {
        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(1,2);
        Integer id = dao.insert(inventoryPojo);
        InventoryPojo inventoryPojo1 = dao.select(id);
        assertEquals(new Integer(1),inventoryPojo1.getProductId());
        assertEquals(new Integer(2),inventoryPojo1.getQuantity());
    }

    @Test
    public void testGetFromProductId() throws ApiException {
        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(1,2);
        Integer id = dao.insert(inventoryPojo);
        InventoryPojo inventoryPojo1 = dao.getFromProductId(1);
        assertEquals(new Integer(1),inventoryPojo1.getProductId());
        assertEquals(new Integer(2),inventoryPojo1.getQuantity());
    }

    @Test
    public void testSelect() throws ApiException {
        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(1,2);
        Integer id = dao.insert(inventoryPojo);
        InventoryPojo inventoryPojo1 = dao.select(id);
        assertEquals(new Integer(1),inventoryPojo1.getProductId());
        assertEquals(new Integer(2),inventoryPojo1.getQuantity());
    }

    @Test
    public void testSelectAll() throws ApiException {
        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(1,2);
        Integer id = dao.insert(inventoryPojo);
        List<InventoryPojo> inventoryPojoList = dao.selectAll();
        assertEquals(1,inventoryPojoList.size());
        assertEquals(new Integer(1),inventoryPojoList.get(0).getProductId());
        assertEquals(new Integer(2),inventoryPojoList.get(0).getQuantity());
    }
}

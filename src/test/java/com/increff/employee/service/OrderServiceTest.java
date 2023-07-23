package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import helper.pojoHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest extends AbstractUnitTest {
//what test case for add time creation??
@Autowired
InventoryService inventoryService;
    @Autowired
    BrandService brandService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    OrderService orderService;

    private Integer brandId ;
    private Integer brandIdSecondPojo;
    private Integer productId;
    private Integer productId2;

    private Integer inventoryId;
    private Integer inventoryId2;
    BrandPojo brandPojo;

    private Integer orderId;

    private Integer orderId2;

    @Before
    public void preTest() throws ApiException {
        orderId2 = orderItemService.addInOrder();
        brandPojo = pojoHelper.makeBrandPojo("brand","category");
        brandService.add(brandPojo);
        BrandPojo brandPojo1 = brandService.getId("brand","category");
        brandId = brandPojo1.getId();
        BrandPojo brandPojo2 = pojoHelper.makeBrandPojo("brand2","category2");
        brandService.add(brandPojo2);
        BrandPojo brandPojo3 = brandService.getId("brand2","category2");
        brandIdSecondPojo = brandPojo3.getId();
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
        productId = productService.add(productPojo);
        ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode1",brandIdSecondPojo,1.10,"name1");
        productId2 = productService.add(productPojo1);
        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,1000);
        inventoryId = inventoryService.add(inventoryPojo);
        InventoryPojo inventoryPojo2 = pojoHelper.makeInventoryPojo(productId2,2000);
        inventoryId2 = inventoryService.add(inventoryPojo2);
        orderId = orderItemService.addInOrder();
    }


//    @Test
//    public void testAdd() throws ApiException {
//        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 7, 21, 12, 34, 56);
//
//        try (MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class)) {
//            localDateTimeMock.when(LocalDateTime::now).thenReturn(fixedDateTime);
//
//            Integer orderId3 = orderService.add();
//            //Integer order = orderService.getOrderById(orderId3);
//
//            assertEquals(fixedDateTime, orderService.getOrderById(1).getTime());
//        }
//    }



    //check delete function
    @Test
    public void testDelete() throws ApiException {
        assertNotNull(orderService.getOrderById(orderId));
        orderService.delete(orderId);
        assertNull(orderService.getOrderById(orderId));
    }

    //check getAll function
    @Test
    public void testGetAll() throws ApiException {
        List<OrderPojo> orderPojoList = orderService.getAll();
        assertEquals(2,orderPojoList.size());
        assertEquals(orderPojoList.get(0).getId(),orderId2);
        assertEquals(orderPojoList.get(1).getId(),orderId);
    }

    //test timeOfOrderCreation
    @Test
    public void testTimeOfOrderCreation() {
        orderService.timeOfOrderCreation();
    }



}

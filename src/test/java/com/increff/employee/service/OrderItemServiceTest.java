package com.increff.employee.service;

import com.increff.employee.model.CategoryDetailForm;
import com.increff.employee.pojo.*;
import helper.pojoHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InterfaceMaker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderItemServiceTest extends AbstractUnitTest {
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

    Integer orderId;
    @Before
    public void preTest() throws ApiException {
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

    //adding a list of orderItems
    @Test
    public void testAddNew() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        for(int i=1;i<=4;i++) {
            OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
            OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId2, 1, 1.0);
            orderItemPojoList.add(orderItemPojo);
            orderItemPojoList.add(orderItemPojo1);
        }
        orderItemService.add(orderItemPojoList);

        List<OrderItemPojo> orderItemPojoList1 = orderItemService.selectByOrderId(orderId);

        assertTrue(orderItemPojoList1.size() == orderItemPojoList.size() && orderItemPojoList1.containsAll(orderItemPojoList) && orderItemPojoList.containsAll(orderItemPojoList1));
    }

    @Test
    public void testAddExceedingQuantity() throws ApiException {
        try {
            List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
            OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 10000, 1.0);
            orderItemPojoList.add(orderItemPojo);
            orderItemService.add(orderItemPojoList);

            List<OrderItemPojo> orderItemPojoList1 = orderItemService.selectByOrderId(orderId);

            assertTrue(orderItemPojoList1.size() == orderItemPojoList.size() && orderItemPojoList1.containsAll(orderItemPojoList) && orderItemPojoList.containsAll(orderItemPojoList1));
        }
        catch (ApiException err) {
            assertEquals("quantity is more than what is present in the inventory",err.getMessage());
        }
    }

    //initialize order
    @Test
    public void testAddInOrder() throws ApiException {
        Integer orderId2 = orderItemService.addInOrder();
        OrderPojo orderPojo = orderService.getOrderById(orderId2);
        //time???
        assertNotNull(orderPojo);
    }

    //order by id
    @Test
    public void testSelectOrderById() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        for(int i=1;i<=4;i++) {
            OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
            OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId2, 1, 1.0);
            orderItemPojoList.add(orderItemPojo);
            orderItemPojoList.add(orderItemPojo1);
        }
        orderItemService.add(orderItemPojoList);

        List<OrderItemPojo> orderItemPojoList1 = orderItemService.selectByOrderId(orderId);

        assertTrue(orderItemPojoList1.size() == orderItemPojoList.size() && orderItemPojoList1.containsAll(orderItemPojoList) && orderItemPojoList.containsAll(orderItemPojoList1));
    }

    //given barcode exists ??no assertion
    @Test
    public void testProductBarcodeMustExistExists() throws ApiException {
        CategoryDetailForm categoryDetailForm = new CategoryDetailForm();
        categoryDetailForm.setBarcode("barcode");
        categoryDetailForm.setMrp(1.1);
        categoryDetailForm.setQuantity(1);
        orderItemService.productBarcodeMustExist(categoryDetailForm);
    }

    //barcode does not exist
    @Test
    public void testProductBarcodeMustExistNotExists() throws ApiException {
        try {
            CategoryDetailForm categoryDetailForm = new CategoryDetailForm();
            categoryDetailForm.setBarcode("barcode7");
            categoryDetailForm.setMrp(1.1);
            categoryDetailForm.setQuantity(1);
            orderItemService.productBarcodeMustExist(categoryDetailForm);
        }
        catch (ApiException err) {
            assertEquals("this barcode does not exists",err.getMessage());
        }
    }
    //sellingprice>mrp
    @Test
    public void testProductBarcodeMustExistSellingPriceHigh() throws ApiException {
        try {
            CategoryDetailForm categoryDetailForm = new CategoryDetailForm();
            categoryDetailForm.setBarcode("barcode");
            categoryDetailForm.setMrp(1.1);
            categoryDetailForm.setQuantity(10000);
            orderItemService.productBarcodeMustExist(categoryDetailForm);
        }
        catch (ApiException err) {
            assertEquals("selling price cannot be greater than mrp",err.getMessage());
        }
    }

}

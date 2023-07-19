package com.increff.employee.service;

import com.increff.employee.model.CategoryDetailForm;
import com.increff.employee.pojo.*;
import helper.pojoHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InterfaceMaker;

import javax.transaction.Transactional;
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

    //adding a list of orderItems
    @Test
    public void testAddNew() throws ApiException {
        ArrayList<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
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

    //delete order
    @Test
    public void testDeleteOrder() throws ApiException {
        ArrayList<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
            OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
            OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId2, 1, 1.0);
            orderItemPojoList.add(orderItemPojo);
            orderItemPojoList.add(orderItemPojo1);

            orderItemService.deleteOrder(orderId);

        assertTrue(orderItemService.getAll().isEmpty());
    }

    //delete orderItem deletes the first element
    @Test
    public void testDelete() throws ApiException {
        ArrayList<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
        OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId2, 1, 1.0);
        orderItemPojoList.add(orderItemPojo);
        orderItemPojoList.add(orderItemPojo1);
        orderItemService.add(orderItemPojoList);

        List<OrderItemPojo> orderItemPojoList1 = orderItemService.getAll();
        Integer orderItemId = 1;
        for(int i=0;i<orderItemPojoList1.size();i++) {
            if(orderItemPojoList1.get(i).getOrderId()==orderId && orderItemPojoList1.get(i).getProductId()==productId) {
                orderItemId = orderItemPojoList1.get(i).getId();
                break;
            }
        }
        OrderItemPojo orderItemPojo2 = orderItemService.get(orderItemId);
        Integer checkId = orderItemPojo2.getId();
        orderItemService.delete(orderItemId);
        assertNull(orderItemService.get(checkId));
    }

    //delete order if all items of order are deleted
    @Test
    public void testDeleteOrderIfAllOrderItemsDeleted() throws ApiException {
        ArrayList<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
        OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId2, 1, 1.0);
        orderItemPojoList.add(orderItemPojo);
        orderItemPojoList.add(orderItemPojo1);
        orderItemService.add(orderItemPojoList);

        List<OrderItemPojo> orderItemPojoList1 = orderItemService.getAll();
        Integer orderItemId = 1;
        Integer orderItemId2 = 1;
        for(int i=0;i<orderItemPojoList1.size();i++) {
            if(orderItemPojoList1.get(i).getOrderId()==orderId && orderItemPojoList1.get(i).getProductId()==productId) {
                orderItemId = orderItemPojoList1.get(i).getId();
            }
            if(orderItemPojoList1.get(i).getOrderId()==orderId && orderItemPojoList1.get(i).getProductId()==productId2) {
                orderItemId2 = orderItemPojoList1.get(i).getId();
            }
        }
        OrderItemPojo orderItemPojo2 = orderItemService.get(orderItemId);
        Integer checkId = orderItemPojo2.getId();
        orderItemService.delete(orderItemId);
        orderItemService.delete(orderItemId2);
        assertNull(orderService.getOrderById(orderId));
    }


    //is testDeleteOrderJust works fine
    @Test
    public void testDeleteOrderJust() throws ApiException {
        orderItemService.deleteOrderJust(orderId);
        assertNull(orderService.getOrderById(orderId));
    }

    //is deleteOrderJust works fine
//    @Test
//    public void testDeleteOrderJustNotEditable() throws ApiException {
//        OrderPojo orderPojo = orderService.getOrderById(orderId);
//        orderPojo.setEditable(false);
//        orderItemService.deleteOrderJust(orderId);
//        assertNull(orderService.getOrderById(orderId));
//    }
//
    //is deleteOrderItemJust works fine
    @Test
    public void testDeleteOrderItemJust() throws ApiException {
        ArrayList<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
        orderItemPojoList.add(orderItemPojo);
        orderItemService.add(orderItemPojoList);
        List<OrderItemPojo> orderItemPojoList1 = orderItemService.getAll();
        Integer id = orderItemPojoList1.get(0).getId();
        orderItemService.deleteOrderItemJust(id);
        assertNull(orderItemService.get(id));
    }

    @Test
    public void testEditOrderItemJust() throws ApiException {
        ArrayList<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 0.9);
        orderItemPojoList.add(orderItemPojo);
        orderItemService.add(orderItemPojoList);
        List<OrderItemPojo> orderItemPojoList1 = orderItemService.getAll();
        Integer id = 1;
        for(int i=0;i<orderItemPojoList1.size();i++){
            if(orderId==orderItemPojoList1.get(i).getOrderId()&&productId==orderItemPojoList1.get(i).getProductId()) {
                id = orderItemPojoList1.get(i).getId();
                break;
            }
        }
        OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId, 2, 0.8);
        orderItemService.editOrder(id,orderItemPojo1);
        OrderItemPojo orderItemPojo2 = orderItemService.get(id);
        assertEquals(new Integer(2),orderItemPojo2.getQuantity());
        assertEquals(new Double(0.8),orderItemPojo2.getSellingPrice());
    }

    /////delete 2 FUNCTIONS + EDIT ORDER

//    //order by id
//    @Test
//    public void testSelectOrderById() throws ApiException {
//        List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
//        for(int i=1;i<=4;i++) {
//            OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
//            OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId2, 1, 1.0);
//            orderItemPojoList.add(orderItemPojo);
//            orderItemPojoList.add(orderItemPojo1);
//        }
//        orderItemService.add(orderItemPojoList);
//
//        List<OrderItemPojo> orderItemPojoList1 = orderItemService.selectByOrderId(orderId);
//
//        assertTrue(orderItemPojoList1.size() == orderItemPojoList.size() && orderItemPojoList1.containsAll(orderItemPojoList) && orderItemPojoList.containsAll(orderItemPojoList1));
//    }

    //given barcode exists ??no assertion
    //test get
    @Test
    public void testGet() throws ApiException {
        ArrayList<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
            OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
            orderItemPojoList.add(orderItemPojo);

        //nothing before
        assertTrue(orderItemService.getAll().isEmpty());

        orderItemService.add(orderItemPojoList);

        List<OrderItemPojo> orderItemPojoList1 = orderItemService.getAll();
        Integer id = orderItemPojoList1.get(0).getId();

        assertNotNull(orderItemService.get(id));
    }

    //test get
    @Test
    public void testGetAll() throws ApiException {
        ArrayList<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
        orderItemPojoList.add(orderItemPojo);

        OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId, 2, 3.0);
        orderItemPojoList.add(orderItemPojo1);

        orderItemService.add(orderItemPojoList);

        List<OrderItemPojo> orderItemPojoList1 = orderItemService.getAll();

        assertTrue(orderItemPojoList1.size() == orderItemPojoList.size() && orderItemPojoList1.containsAll(orderItemPojoList) && orderItemPojoList.containsAll(orderItemPojoList1));
    }

    //test get
    @Test
    public void testSelectByOrderId() throws ApiException {
        ArrayList<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
        orderItemPojoList.add(orderItemPojo);

        OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId, 2, 3.0);
        orderItemPojoList.add(orderItemPojo1);

        //item from other orderId
        OrderItemPojo orderItemPojo2 = pojoHelper.makeOrderItemPojo(orderId2, productId, 3, 3.0);
        orderItemPojoList.add(orderItemPojo2);

        orderItemService.add(orderItemPojoList);

        //remove other orderId element
        orderItemPojoList.remove(2);

        List<OrderItemPojo> orderItemPojoList1 = orderItemService.selectByOrderId(orderId);

        assertTrue(orderItemPojoList1.size() == orderItemPojoList.size() && orderItemPojoList1.containsAll(orderItemPojoList) && orderItemPojoList.containsAll(orderItemPojoList1));
    }



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

    //getInventoryFromProductId test product not in inventory
     @Test
     public void testGetInventoryFromProductIdProdIdNotPresent() throws ApiException {
         try {
             ProductPojo productPojo = pojoHelper.makeProductPojo("barcode100",brandId,1.12,"name3");
             Integer productId3 = productService.add(productPojo);
             orderItemService.getInventoryFromProductId(productId3,1);
         }
         catch (ApiException err) {
             assertEquals("this product is not present in the inventory",err.getMessage());
         }
     }

    //getInventoryFromProductId test quantity lesser than in inventory
    @Test
    public void testGetInventoryFromProductIdQuantityHigh() throws ApiException {
        try {
            orderItemService.getInventoryFromProductId(productId,9000);
        }
        catch (ApiException err) {
            assertEquals("quantity of item more than the quantity in inventory",err.getMessage());
        }
    }

    //getInventoryFromProductId test quantity is negative
    @Test
    public void testGetInventoryFromProductIdQuantityLessThan1() throws ApiException {
        try {
            orderItemService.getInventoryFromProductId(productId,0);
        }
        catch (ApiException err) {
            assertEquals("quantity must be at least 1",err.getMessage());
        }
    }
    //test for getInventoryFromProductId second
    //getInventoryFromProductId test product not in inventory
    @Test
    public void testGetInventoryFromProductId2ProdIdNotPresent() throws ApiException {
        try {
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode100",brandId,1.12,"name3");
            Integer productId3 = productService.add(productPojo);
            OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId3, 1, 1.0);
//            List<OrderItemPojo> OrderItemPojoList = new ArrayList<>();
//            OrderItemPojoList.add(orderItemPojo);
//            orderItemService.add(OrderItemPojoList);
//            Integer orderItemId = 1;
//            List<OrderItemPojo> OrderItemPojoList1 = orderItemService.getAll();
//            for(int i=0;i<OrderItemPojoList1.size();i++) {
//                if(OrderItemPojoList1.get(i).getOrderId()==orderId && OrderItemPojoList1.get(i).getProductId()==productId){
//                    orderItemId = OrderItemPojoList1.get(i).getId();
//                    break;
//                }
//            }
            OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId+productId2+1, 1, 1.0);
            orderItemService.getInventoryFromProductId(orderItemPojo,1);
        }
        catch (ApiException err) {
            assertEquals("this product is not present in the inventory",err.getMessage());
        }
    }

    //getInventoryFromProductId test quantity lesser than in inventory
    @Test
    public void testGetInventoryFromProductId2QuantityHigh() throws ApiException {
        try {
//            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
//            Integer productId3 = productService.add(productPojo);
            OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
            List<OrderItemPojo> OrderItemPojoList = new ArrayList<>();
            OrderItemPojoList.add(orderItemPojo);
            orderItemService.add(OrderItemPojoList);


            OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId, 10000, 1.0);
            Integer orderItemId = 1;
            List<OrderItemPojo> OrderItemPojoList1 = orderItemService.getAll();
            for(int i=0;i<OrderItemPojoList1.size();i++) {
                if(OrderItemPojoList1.get(i).getOrderId()==orderItemPojo.getOrderId() && OrderItemPojoList1.get(i).getProductId()==orderItemPojo.getProductId()){
                    orderItemId = OrderItemPojoList1.get(i).getId();
                    break;
                }
            }
            orderItemService.getInventoryFromProductId(orderItemPojo1,orderItemId);
        }
        catch (ApiException err) {
            assertEquals("quantity of item more than the quantity in inventory",err.getMessage());
        }
    }

    //getInventoryFromProductId test selling price<mrp always
    @Test
    public void testGetInventoryFromProduct2MRP() throws ApiException {
        try {
            OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
            List<OrderItemPojo> OrderItemPojoList = new ArrayList<>();
            OrderItemPojoList.add(orderItemPojo);
            orderItemService.add(OrderItemPojoList);


            OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 10000.0);
            Integer orderItemId = 1;
            List<OrderItemPojo> OrderItemPojoList1 = orderItemService.getAll();
            for(int i=0;i<OrderItemPojoList1.size();i++) {
                if(OrderItemPojoList1.get(i).getOrderId()==orderItemPojo.getOrderId() && OrderItemPojoList1.get(i).getProductId()==orderItemPojo.getProductId()){
                    orderItemId = OrderItemPojoList1.get(i).getId();
                    break;
                }
            }
            orderItemService.getInventoryFromProductId(orderItemPojo1,orderItemId);
        }
        catch (ApiException err) {
            assertEquals("selling price of item can not be more than MRP",err.getMessage());
        }
    }

    //getInventoryFromProduct function works fine
    @Test
    public void testGetInventoryFromProduct2() throws ApiException {
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
        List<OrderItemPojo> OrderItemPojoList = new ArrayList<>();
        OrderItemPojoList.add(orderItemPojo);
        orderItemService.add(OrderItemPojoList);


        OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId, 4, 1.0);
        Integer orderItemId = 1;
        List<OrderItemPojo> OrderItemPojoList1 = orderItemService.getAll();
        for(int i=0;i<OrderItemPojoList1.size();i++) {
            if(OrderItemPojoList1.get(i).getOrderId()==orderItemPojo.getOrderId() && OrderItemPojoList1.get(i).getProductId()==orderItemPojo.getProductId()){
                orderItemId = OrderItemPojoList1.get(i).getId();
                break;
            }
        }
        orderItemService.getInventoryFromProductId(orderItemPojo1,orderItemId);
        InventoryPojo inventoryPojo = inventoryService.get(inventoryId);

        assertEquals(new Integer(996),inventoryPojo.getQuantity());
    }

    @Test
    public void testGetInventoryIdOfProduct() throws ApiException {
        assertEquals(productId,orderItemService.getInventoryIdOfProduct("barCode"));
    }

    @Test
    public void testGetFromProductName() throws ApiException {
        OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId, productId, 4, 1.0);
        assertEquals("name",orderItemService.getFromProductName(orderItemPojo1));
    }

    @Test
    public void testNormalize() throws ApiException {
        assertEquals("barcode",orderItemService.normalize("BarcOde"));
    }

    //***tests for get check function****
    //functionality of getCheck function works fine
    @Test
    public void testGetCheckNoError() throws ApiException {
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        orderItemPojoList.add(orderItemPojo);
        orderItemService.add(orderItemPojoList);

        List<OrderItemPojo> orderItemPojoList1 = orderItemService.getAll();
        Integer id = orderItemPojoList1.get(0).getId();

        OrderItemPojo orderItemPojo1 = orderItemService.getCheck(id);

        assertEquals(orderId,orderItemPojo1.getOrderId());
        assertEquals(productId,orderItemPojo1.getProductId());
    }

    //getCheck function if given pojo is not in the database
    @Test
    public void testGetCheckError() throws ApiException {
        try {
            OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId, productId, 1, 1.0);
            List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
            orderItemPojoList.add(orderItemPojo);
            orderItemService.add(orderItemPojoList);

            List<OrderItemPojo> orderItemPojoList1 = orderItemService.getAll();
            Integer id = orderItemPojoList1.get(0).getId();
            orderItemService.getCheck(id);
        }
        catch(ApiException err) {
            assertEquals("Order with given ID does not exit, id: 1",err.getMessage());
        }
    }

}

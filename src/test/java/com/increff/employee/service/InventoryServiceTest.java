package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import helper.pojoHelper;
import io.swagger.annotations.Api;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InventoryServiceTest extends AbstractUnitTest {
    @Autowired
    InventoryService inventoryService;
    @Autowired
    BrandService brandService;

    @Autowired
    ProductService productService;

    private Integer brandId ;
    private Integer brandIdSecondPojo;
    private Integer productId;
    private Integer productId2;
    BrandPojo brandPojo;
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
    }

    //adding quantity less than 1
    @Test
    public void testAddQuantityLess() throws ApiException {
        try {
            InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,0);
            inventoryService.add(inventoryPojo);
        }
        catch (ApiException err) {
            assertEquals("quantity can not be less than 1",err.getMessage());
        }
    }

    //adding already existing product
    @Test
    public void testAddAlreadyExisting() throws ApiException {

        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,2);
        inventoryService.add(inventoryPojo);


        InventoryPojo inventoryPojo1 = pojoHelper.makeInventoryPojo(productId,2);
        Integer inventoryId = inventoryService.add(inventoryPojo1);

        InventoryPojo inventoryPojo2 = inventoryService.get(inventoryId);
        assertEquals(productId,inventoryPojo2.getProductId());
        assertEquals(new Integer(4),inventoryPojo2.getQuantity());
    }

    //adding a product not existing in the inventory
    @Test
    public void testAddNew() throws ApiException {

        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,2);
        Integer inventoryId = inventoryService.add(inventoryPojo);

        assertEquals(new Integer(2),inventoryService.get(inventoryId).getQuantity());
        assertEquals(productId,inventoryService.get(inventoryId).getProductId());
    }

    //test deduceQuantity function
    @Test
    public void testDeductQuantity() throws ApiException {

        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,2);
        Integer inventoryId = inventoryService.add(inventoryPojo);
        inventoryService.deductQuantity(productId,1);
        Integer quantity = inventoryService.get(inventoryId).getQuantity();
        assertEquals(new Integer(1),quantity);
    }

    @Test
    public void testDeductExceedQuantity() throws ApiException {

        try {
            InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId, 2);
            Integer inventoryId = inventoryService.add(inventoryPojo);
            inventoryService.deductQuantity(productId, 100);
            Integer quantity = inventoryService.get(inventoryId).getQuantity();
            assertEquals(new Integer(1), quantity);
        }
        catch (ApiException err) {
            assertEquals("quantity is more than what is present in the inventory",err.getMessage());
        }
    }

    //test get() function
    @Test
    public void testGet() throws ApiException {
        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId2,2);
        Integer inventoryId = inventoryService.add(inventoryPojo);
        InventoryPojo inventoryPojo1 = inventoryService.get(inventoryId);
        assertEquals(new Integer(2),inventoryPojo1.getQuantity());
        assertEquals(productId2,inventoryPojo1.getProductId());
    }

    //test getAll function
    @Test
    public void testGetAll() throws ApiException {
        ArrayList<InventoryPojo> inventoryPojoList = new ArrayList<InventoryPojo>();
        for( int i = 1 ; i <= 3 ; i ++ ) {
            InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,i);
            inventoryService.add(inventoryPojo);
            inventoryPojoList.add(inventoryPojo);
            InventoryPojo inventoryPojo1 = pojoHelper.makeInventoryPojo(productId2,i);
            inventoryService.add(inventoryPojo1);
            inventoryPojoList.add(inventoryPojo1);
        }
        List<InventoryPojo> inventoryPojoList1 = inventoryService.getAll();
        assertTrue(inventoryPojoList1.size() == inventoryPojoList.size() && inventoryPojoList1.containsAll(inventoryPojoList) && inventoryPojoList.containsAll(inventoryPojoList1));
    }

    //test getIdOfProduct function
    @Test
    public void testGetIdOfProduct() throws ApiException {
        Integer productId3 = inventoryService.getIdOfProduct("barcode1");
        assertEquals(productId2,productId3);
    }

    //test getForBarcode function
    @Test
    public void testGetForBarcode() throws ApiException {
        String barcode1 = inventoryService.getForBarcode(productId);
        assertEquals("barcode",barcode1);
    }

    //test getFromProductId function
    @Test
    public void testGetFromProductId() throws ApiException {
        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,7);
        inventoryService.add(inventoryPojo);
        InventoryPojo inventoryPojo1 = inventoryService.getFromProductId(productId);

        assertEquals(new Integer(7),inventoryPojo1.getQuantity());
        assertEquals(productId,inventoryPojo1.getProductId());
    }

    //update tests
    //no empty quantity
    @Test
    public void testUpdateNoEmptyQuantity() throws ApiException {
        try {
            InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,1);
            Integer inventoryId = inventoryService.add(inventoryPojo);

            InventoryPojo inventoryPojo1 = new InventoryPojo();
            inventoryPojo1.setProductId(productId);
            inventoryService.update(inventoryId, inventoryPojo1);
        }
        catch (ApiException err) {
            assertEquals("quantity of given product cannot be empty",err.getMessage());
        }
    }

    //no negative quantity
    @Test
    public void testUpdateNoNegativeQuantity() throws ApiException {
        try {
            InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,1);
            Integer inventoryId = inventoryService.add(inventoryPojo);

            InventoryPojo inventoryPojo1 = pojoHelper.makeInventoryPojo(productId,-1);
            inventoryService.update(inventoryId, inventoryPojo1);
        }
        catch (ApiException err) {
            assertEquals("quantity of given product cannot be less than 1",err.getMessage());
        }
    }

    // testBarcodeIsNotInInventory
    @Test
    public void testUpdateBarcodeIsNotInInventory() throws ApiException {
        try {
            InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId, 1);
            Integer inventoryId = inventoryService.add(inventoryPojo);

            InventoryPojo inventoryPojo1 = pojoHelper.makeInventoryPojo(productId2, 2);
            inventoryService.update(inventoryId, inventoryPojo1);
        }
        catch(ApiException err) {
            assertEquals("inventory of this productId does  not exist",err.getMessage());
        }
    }

    @Test
    public void testUpdateBarcodeAlreadyExists() {
        try {
            InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,1);
            Integer inventoryId = inventoryService.add(inventoryPojo);

            InventoryPojo inventoryPojo1 = pojoHelper.makeInventoryPojo(productId2,1);
            inventoryService.add(inventoryPojo1);
        }
        catch(ApiException err) {
            assertEquals("inventory can not be updated because of alter in productId",err.getMessage());
        }
    }

    //also in PRODUCT
    @Test
    public void testUpdateIfNothingUpdatesReturn0() throws ApiException {
        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,1);
            Integer inventoryId = inventoryService.add(inventoryPojo);

        InventoryPojo inventoryPojo2 = pojoHelper.makeInventoryPojo(productId,1);
            Integer status = inventoryService.update(inventoryId, inventoryPojo2);

            assertEquals(new Integer(0),status);
    }

    @Test
    public void testUpdateIfUpdatesReturn1() throws ApiException {
        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,1);
        Integer inventoryId = inventoryService.add(inventoryPojo);

        InventoryPojo inventoryPojo2 = pojoHelper.makeInventoryPojo(productId,2);
        Integer status = inventoryService.update(inventoryId, inventoryPojo2);

        assertEquals(new Integer(1),status);
    }

    //getcheck works fine
    @Test
    public void testGetCheckWorksFine() throws ApiException {

        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,1);
        Integer inventoryId = inventoryService.add(inventoryPojo);

        InventoryPojo inventoryPojo1 = inventoryService.getCheck(inventoryId);

        assertEquals(inventoryPojo1.getQuantity(),inventoryPojo1.getQuantity());
        assertEquals(inventoryPojo1.getProductId(),inventoryPojo1.getProductId());
    }

    //getCheck function if given pojo is not in the database
    @Test
    public void testGetCheckError() throws ApiException {
        try {
            inventoryService.getCheck(1);
        }
        catch(ApiException err) {
            assertEquals("Inventory with given ID does not exit, id: 1",err.getMessage());
        }
    }

}

package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
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
        brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandService.add(brandPojo);
        BrandPojo brandPojo1 = brandService.getId("brand","category");
        brandId = brandPojo1.getId();
        BrandPojo brandPojo2 = new BrandPojo();
        brandPojo2.setBrand("brand2");
        brandPojo2.setCategory("category2");
        brandService.add(brandPojo2);
        BrandPojo brandPojo3 = brandService.getId("brand2","category2");
        brandIdSecondPojo = brandPojo3.getId();
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(brandId);
        productPojo.setMrp(1.12);
        productPojo.setName("name");
        productId = productService.add(productPojo);
        ProductPojo productPojo1 = new ProductPojo();
        productPojo1.setBarcode("barcode1");
        productPojo1.setBrand_category(brandIdSecondPojo);
        productPojo1.setMrp(1.10);
        productPojo1.setName("name1");
        productId2 = productService.add(productPojo1);
    }

    //adding quantity less than 1
    @Test
    public void testAddQuantityLess() throws ApiException {
        try {
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setQuantity(0);
            inventoryPojo.setProductId(productId);
            inventoryService.add(inventoryPojo);
        }
        catch (ApiException err) {
            assertEquals("quantity can not be less than 1",err.getMessage());
        }
    }

    //adding already existing product
    @Test
    public void testAddAlreadyExisting() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(2);
        inventoryPojo.setProductId(productId);
        Integer inventoryId = inventoryService.add(inventoryPojo);

        InventoryPojo inventoryPojo1 = new InventoryPojo();
        inventoryPojo1.setQuantity(2);
        inventoryPojo1.setProductId(productId);
        inventoryService.add(inventoryPojo1);

        InventoryPojo inventoryPojo2 = inventoryService.get(inventoryId);
        assertEquals(new Integer(4),inventoryPojo2.getQuantity());
        assertEquals(productId,inventoryPojo2.getProductId());
    }

    //adding a product not existing in the inventory
    @Test
    public void testAddNew() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(2);
        inventoryPojo.setProductId(productId);
        Integer inventoryId = inventoryService.add(inventoryPojo);

        assertEquals(new Integer(2),inventoryService.get(inventoryId).getQuantity());
        assertEquals(productId,inventoryService.get(inventoryId).getProductId());
    }

    //test deduceQuantity function
    @Test
    public void testDeductQuantity() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(2);
        inventoryPojo.setProductId(productId);
        Integer inventoryId = inventoryService.add(inventoryPojo);
        inventoryService.deductQuantity(inventoryId,1);
        Integer quantity = inventoryService.get(inventoryId).getQuantity();
        assertEquals(new Integer(1),quantity);
    }

    //test get() function
    @Test
    public void testGet() throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(2);
        inventoryPojo.setProductId(productId2);
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
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setQuantity(i);
            inventoryPojo.setProductId(productId);
            inventoryService.add(inventoryPojo);
            inventoryPojoList.add(inventoryPojo);
            InventoryPojo inventoryPojo1 = new InventoryPojo();
            inventoryPojo1.setQuantity(i);
            inventoryPojo1.setProductId(productId2);
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
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(7);
        inventoryPojo.setProductId(productId);
        inventoryService.add(inventoryPojo);
        InventoryPojo inventoryPojo1 = inventoryService.getFromProductId(productId);

        assertEquals(new Integer(7),inventoryPojo1.getQuantity());
        assertEquals(productId,inventoryPojo1.getProductId());
    }

}

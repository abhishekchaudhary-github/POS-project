package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

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
        inventoryService.add(inventoryPojo);

        InventoryPojo inventoryPojo1 = new InventoryPojo();
        inventoryPojo1.setQuantity(2);
        inventoryPojo1.setProductId(productId);
        Integer inventoryId = inventoryService.add(inventoryPojo1);

        assertEquals(new Integer(4),inventoryService.get(inventoryId).getQuantity());
        assertEquals(productId,inventoryService.get(inventoryId).getProductId());
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

}

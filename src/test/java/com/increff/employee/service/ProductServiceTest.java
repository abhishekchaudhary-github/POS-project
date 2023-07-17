package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Before
    public void preTest() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandService.add(brandPojo);
    }

    //add tests
    @Test
    public void testAdd() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(1);
        productPojo.setMrp(1.12);
        productPojo.setName("name");
        productService.add(productPojo);
        ProductPojo productPojo1 = productService.get(1);
        assertEquals("barcode",productPojo1.getBarcode());
        assertEquals(new Integer(1) ,productPojo1.getBrand_category());
        assertEquals(new Double(1.12) ,productPojo1.getMrp());
        assertEquals("name",productPojo1.getName());
    }

    //empty fields tests
    @Test
    public void testEmptyBarcode() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBrand_category(1);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            productService.add(productPojo);
        } catch (ApiException err) {
            assertEquals("barcode cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testEmptyBrand_category() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            productService.add(productPojo);
        } catch (ApiException err) {
            assertEquals("brand_category cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testEmptyMrp() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(1);
            productPojo.setName("name");
            productService.add(productPojo);
        } catch (ApiException err) {
            assertEquals("mrp cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testEmptyName() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(1);
            productPojo.setMrp(1.12);
            productService.add(productPojo);
        } catch (ApiException err) {
            assertEquals("name cannot be empty",err.getMessage());
        }
    }

}

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

    private Integer id ;

    @Before
    public void preTest() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandService.add(brandPojo);
        BrandPojo brandPojo1 = brandService.getId("brand","category");
        id = brandPojo1.getId();
    }
    //*******add tests******
    //adding values in product
    @Test
    public void testAdd() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(id);
        productPojo.setMrp(1.12);
        productPojo.setName("name");
        Integer productId = productService.add(productPojo);
        ProductPojo productPojo1 = productService.get(productId);
        assertEquals("barcode",productPojo1.getBarcode());
        assertEquals(new Integer(id) ,productPojo1.getBrand_category());
        assertEquals(new Double(1.12) ,productPojo1.getMrp());
        assertEquals("name",productPojo1.getName());
    }

    //*******empty fields tests********
    //empty barcode field
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

    //empty brand_category field
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

    //empty mrp field
    @Test
    public void testEmptyMrp() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(1);
            productPojo.setName("name");
            productService.add(productPojo);
        } catch (ApiException err) {
            assertEquals("MRP cannot be empty",err.getMessage());
        }
    }

    //empty name field
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

    //*******adding unwanted values tests******
    //duplicate barcode
    @Test
    public void testDuplicateBarcode() throws ApiException {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(id);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            productService.add(productPojo);

            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBarcode("barcode");
            productPojo1.setBrand_category(id);
            productPojo1.setMrp(1.13);
            productPojo1.setName("name2");
            productService.add(productPojo);
        }
        catch(ApiException err) {
            assertEquals("same barcode already exists",err.getMessage());
        }
    }

    //test for brand_category not existing
    @Test
    public void testBrand_categoryDoesNotExist() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(2);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            productService.add(productPojo);
        }
        catch(ApiException err) {
            assertEquals("product with this brand category combination does not exist",err.getMessage());
        }
    }

    //check negative mrp
    @Test
    public void testMRPCanNotBeNegative() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(1);
            productPojo.setMrp(-1.12);
            productPojo.setName("name");
            productService.add(productPojo);
        }
        catch(ApiException err) {
            assertEquals("MRP can not be a negative value",err.getMessage());
        }
    }

    //barcode must not have special characters
    @Test
    public void testBarcodeMustNotHaveSpecialCharacters() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barc$ode");
            productPojo.setBrand_category(1);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            productService.add(productPojo);
        }
        catch(ApiException err) {
            assertEquals("barcode must only contain alphanumeric characters",err.getMessage());
        }
    }

    //duplicate brand and category and name
    @Test
    public void testDuplicateBrand_categoryAndName() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(id);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            productService.add(productPojo);
            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBarcode("barcode2");
            productPojo1.setBrand_category(id);
            productPojo1.setMrp(1.12);
            productPojo1.setName("name");
            productService.add(productPojo1);
        }
        catch(ApiException err) {
            assertEquals("this product already exists",err.getMessage());
        }
    }

    //brand_category combination not existing
    @Test
    public void testBrand_categoryNotExisting() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(id+1);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            productService.add(productPojo);
        }
        catch(ApiException err) {
            assertEquals("product with this brand category combination does not exist",err.getMessage());
        }
    }

    @Test
    public void testTruncateMrp() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(id);
        productPojo.setMrp(1.1353);
        productPojo.setName("name");
        Integer productId = productService.add(productPojo);
        ProductPojo productPojo1 = productService.get(productId);
        assertEquals(new Double(1.14) , productPojo1.getMrp());
    }
}

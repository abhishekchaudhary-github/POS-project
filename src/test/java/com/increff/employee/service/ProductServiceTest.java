package com.increff.employee.service;

import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    private Integer brandId ;
    private Integer brandIdSecondPojo;

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
    }
    //*******add tests******
    //adding values in product
    @Test
    public void testAdd() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(brandId);
        productPojo.setMrp(1.12);
        productPojo.setName("name");
        Integer productId = productService.add(productPojo);
        ProductPojo productPojo1 = productService.get(productId);
        assertEquals("barcode",productPojo1.getBarcode());
        assertEquals(new Integer(brandId) ,productPojo1.getBrand_category());
        assertEquals(new Double(1.12) ,productPojo1.getMrp());
        assertEquals("name",productPojo1.getName());
    }

    //*******empty fields tests********
    //empty barcode field
    @Test
    public void testEmptyBarcode() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBrand_category(brandId);
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
            productPojo.setBrand_category(brandId);
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
            productPojo.setBrand_category(brandId);
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
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            productService.add(productPojo);

            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBarcode("barcode");
            productPojo1.setBrand_category(brandId);
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
    public void testBrand_categoryDoesNotExist(){
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
            productPojo.setBrand_category(brandId);
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
            productPojo.setBrand_category(brandId);
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
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            productService.add(productPojo);
            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBarcode("barcode2");
            productPojo1.setBrand_category(brandId);
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
            productPojo.setBrand_category(brandId+brandIdSecondPojo+1);
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
        productPojo.setBrand_category(brandId);
        productPojo.setMrp(1.1353);
        productPojo.setName("name");
        Integer productId = productService.add(productPojo);
        ProductPojo productPojo1 = productService.get(productId);
        assertEquals(new Double(1.14) , productPojo1.getMrp());
    }

    //***barcode must exist
    @Test
    public void testBarcodeMustExist() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("existingBarcode");
        productPojo.setBrand_category(brandId);
        productPojo.setMrp(1.13);
        productPojo.setName("name");
        productService.add(productPojo);
        ProductPojo productPojo1 = productService.barcodeMustExist("existingbarcode");
        ProductPojo productPojo2 = productService.barcodeMustExist("notExistingBarcode");
        System.out.println(productPojo1);
        assertNotNull(productPojo1);
        assertNull(productPojo2);
    }

    //list of same brand_category
    @Test
    public void testBrandCategory() throws ApiException {
        ArrayList<ProductPojo> productPojoList = new ArrayList<ProductPojo>();
        for( int i = 1 ; i <= 7 ; i ++ ) {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode" + i);
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.13);
            productPojo.setName("name"+i);
            productPojoList.add(productPojo);
            productService.add(productPojo);
        }
        List<ProductPojo> productPojoList1 = productService.brandCategory(brandId);
        assertTrue(productPojoList1.size() == productPojoList.size() && productPojoList1.containsAll(productPojoList) && productPojoList.containsAll(productPojoList1));
    }

    //getId function check
    @Test
    public void testGetId() throws ApiException {
            Integer id2;
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode" + 1);
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.13);
            productPojo.setName("name"+1);
            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBarcode("barcode" + 1);
            productPojo1.setBrand_category(brandId);
            productPojo1.setMrp(1.13);
            productPojo1.setName("name"+1);
            id2 = productService.add(productPojo1);

            ProductPojo productPojo2 = productService.get(id2);
            assertEquals(productPojo1,productPojo2);
    }

    //getBrandPojoFromBrandCategory function
    @Test
    public void testGetBrandPojoFromBrandCategory() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(brandId);
        productPojo.setMrp(1.13);
        productPojo.setName("name");
        productService.add(productPojo);
        BrandPojo brandPojo1 = productService.getBrandPojoFromBrandCategory(productPojo);
        assertEquals(brandPojo,brandPojo1);
    }

    //getBrandPojoFromForm function
    @Test
    public void testGetBrandPojoFromForm() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(brandId);
        productPojo.setMrp(1.1);
        productPojo.setName("name");

        ProductForm f = new ProductForm();
        f.setBarcode("barcode");
        f.setBrand("brand");
        f.setCategory("category");
        f.setMrp(1.1);
        f.setName("name");

        BrandPojo brandPojo1 = productService.getBrandPojoFromForm(f);
        assertEquals(brandPojo,brandPojo1);
    }

    //getBrandPojoFromForm function barcode does not exist
    @Test
    public void testGetBrandPojoFromFormBarcodeNotExist() throws ApiException {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.1);
            productPojo.setName("name");

            ProductForm f = new ProductForm();
            f.setBarcode("barcode1");
            f.setBrand("brand");
            f.setCategory("category");
            f.setMrp(1.1);
            f.setName("name");

            BrandPojo brandPojo1 = productService.getBrandPojoFromForm(f);
        }
        catch (ApiException err) {
            assertEquals("this barcode does not exist",err.getMessage());
        }
    }

    //test getAll() function
    @Test
    public void testGetAll() throws ApiException {
        ArrayList<ProductPojo> productPojoList = new ArrayList<ProductPojo>();
        for( int i = 1 ; i <= 7 ; i ++ ) {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode" + i);
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.13);
            productPojo.setName("name"+i);
            productPojoList.add(productPojo);
            productService.add(productPojo);
        }
        List<ProductPojo> productPojoList1 = productService.getAll();
        assertTrue(productPojoList1.size() == productPojoList.size() && productPojoList1.containsAll(productPojoList) && productPojoList.containsAll(productPojoList1));
    }

    //*****test getProductId function*******
    //barcode exists
    public void testGetProductId() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(brandId);
        productPojo.setMrp(1.1);
        productPojo.setName("name");
        Integer productId = productService.add(productPojo);
        Integer productId1 = productService.getProductId("barcode");
        assertEquals(productId,productId1);
    }

    //*****test getProductId function*******
    //barcode does not exists
    public void testGetProductIdNotExist() throws ApiException {
       try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.1);
            productPojo.setName("name");
            Integer productId = productService.add(productPojo);
            Integer productId1 = productService.getProductId("wrongbarcode");
        }
       catch(ApiException err ) {
           assertEquals("this barcode does not exist",err.getMessage());
       }
    }


    //****update function test****
    //barcode already exists
    @Test
    public void testDuplicateBarcodeUpdate() throws ApiException {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            productService.add(productPojo);

            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBarcode("barcode1");
            productPojo1.setBrand_category(brandId);
            productPojo1.setMrp(1.13);
            productPojo1.setName("name2");
            Integer productId = productService.add(productPojo);

            ProductPojo productPojo2 = new ProductPojo();
            productPojo2.setBarcode("barcode");
            productPojo2.setBrand_category(brandId);
            productPojo2.setMrp(1.13);
            productPojo2.setName("name2");

            productService.update(productId,productPojo2);
        }
        catch(ApiException err) {
            assertEquals("same barcode already exists",err.getMessage());
        }
    }
    //*******empty fields tests********
    //empty barcode field in update
    @Test
    public void testEmptyBarcodeUpdate() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBrand_category(brandId);
            productPojo1.setMrp(1.12);
            productPojo1.setName("name");
            productService.update(productId,productPojo1);
        } catch (ApiException err) {
            assertEquals("barcode cannot be empty",err.getMessage());
        }
    }

    //empty brand_category field in update
    @Test
    public void testEmptyBrand_categoryUpdate() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBarcode("barcode");
            productPojo1.setMrp(1.12);
            productPojo1.setName("name");
            productService.update(productId,productPojo1);
        } catch (ApiException err) {
            assertEquals("brand_category cannot be empty",err.getMessage());
        }
    }

    //empty mrp field in update
    @Test
    public void testEmptyMrpUpdate() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBarcode("barcode");
            productPojo1.setBrand_category(brandId);
            productPojo1.setName("name");
            productService.update(productId,productPojo1);
        } catch (ApiException err) {
            assertEquals("MRP cannot be empty",err.getMessage());
        }
    }

    //empty name field in update
    @Test
    public void testEmptyNameUpdate() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBarcode("barcode");
            productPojo1.setBrand_category(brandId);
            productPojo1.setMrp(1.12);
            productService.update(productId,productPojo1);
        } catch (ApiException err) {
            assertEquals("name cannot be empty",err.getMessage());
        }
    }

    //check negative mrp in update
    @Test
    public void testMRPCanNotBeNegativeUpdate() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBarcode("barcode");
            productPojo1.setBrand_category(brandId);
            productPojo1.setMrp(1.12);
            productPojo1.setName("name");
            productService.update(productId,productPojo1);
        }
        catch(ApiException err) {
            assertEquals("MRP can not be a negative value",err.getMessage());
        }
    }

    //barcode must not have special characters
    @Test
    public void testBarcodeMustNotHaveSpecialCharactersUpdate() {
        try {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode");
            productPojo.setBrand_category(brandId);
            productPojo.setMrp(1.12);
            productPojo.setName("name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = new ProductPojo();
            productPojo1.setBarcode("ba$rcode");
            productPojo1.setBrand_category(brandId);
            productPojo1.setMrp(1.12);
            productPojo1.setName("name");
            productService.update(productId,productPojo1);
        }
        catch(ApiException err) {
            assertEquals("barcode must only contain alphanumeric characters",err.getMessage());
        }
    }

    //no values changed in update function
    @Test
    public void testNoValuesChangesInUpdate() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(brandId);
        productPojo.setMrp(1.12);
        productPojo.setName("name");
        Integer productId = productService.add(productPojo);
        ProductPojo productPojo1 = new ProductPojo();
        productPojo1.setBarcode("barcode");
        productPojo1.setBrand_category(brandId);
        productPojo1.setMrp(1.12);
        productPojo1.setName("name");
        productService.update(productId,productPojo1);
        ProductPojo productpojo = productService.get(productId);
        assertEquals("barcode",productpojo.getBarcode());
        assertEquals(brandId,productpojo.getBrand_category());
        assertEquals(new Double(1.12),productpojo.getMrp());
        assertEquals("name",productpojo.getName());
    }

    //updating values
    @Test
    public void testUpdate() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(brandId);
        productPojo.setMrp(1.12);
        productPojo.setName("name");
        Integer productId = productService.add(productPojo);
        ProductPojo productPojo1 = new ProductPojo();
        productPojo1.setBarcode("barcode1");
        productPojo1.setBrand_category(brandIdSecondPojo);
        productPojo1.setMrp(1.10);
        productPojo1.setName("name1");
        productService.update(productId,productPojo1);
        ProductPojo productpojo = productService.get(productId);
        assertEquals("barcode1",productpojo.getBarcode());
        assertEquals(brandIdSecondPojo,productpojo.getBrand_category());
        assertEquals(new Double(1.10),productpojo.getMrp());
        assertEquals("name1",productpojo.getName());
    }

    //***tests for get check function****
    //functionality of getCheck function works fine
    @Test
    public void testGetCheckNoError() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode");
        productPojo.setBrand_category(brandId);
        productPojo.setMrp(1.12);
        productPojo.setName("name");
        Integer productId = productService.add(productPojo);
        assertNotNull(productService.getCheck(productId));
    }

    //getCheck function if given pojo is not in the database
    @Test
    public void testGetCheckError() throws ApiException {
        try {
            productService.getCheck(1);
        }
        catch(ApiException err) {
            assertEquals("Product with given ID does not exit, id: 1",err.getMessage());
        }
    }

    @Test
    public void testNormalize() throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("bArCodE");
        productPojo.setName("naME");
        productService.normalize(productPojo);
        assertEquals("barcode",productPojo.getBarcode());
        assertEquals("name",productPojo.getName());
    }

    @Test
    public void testNormalizeBarcode() throws ApiException {
       String barcode = "BarCoDE";
        String barcode1 = productService.normalizeBarcode(barcode);
        assertEquals("barcode",barcode1);
    }
}

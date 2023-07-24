package com.increff.employee.service;

import com.increff.employee.model.*;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import helper.formHelper;
import helper.pojoHelper;
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
        brandPojo = pojoHelper.makeBrandPojo("brand","category");
        brandService.add(brandPojo);
        BrandPojo brandPojo1 = brandService.getId("brand","category");
        brandId = brandPojo1.getId();
        BrandPojo brandPojo2 = pojoHelper.makeBrandPojo("brand2","category2");
        brandService.add(brandPojo2);
        BrandPojo brandPojo3 = brandService.getId("brand2","category2");
        brandIdSecondPojo = brandPojo3.getId();
    }
    //*******add tests******
    //adding values in product
    @Test
    public void testAdd() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
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
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = pojoHelper.makeProductPojo("",brandId,1.12,"name");
            productService.update(productId,productPojo1);
        } catch (ApiException err) {
            assertEquals("barcode cannot be empty",err.getMessage());
        }
    }

    //empty brand_category field
    @Test
    public void testEmptyBrand_category() {
        try {
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",null,1.12,"name");
            productService.add(productPojo);
        } catch (ApiException err) {
            assertEquals("brand_category cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testBrandCategoryNotExist() throws ApiException {
        try {
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode", brandId+brandIdSecondPojo+1, 1.12, "name");
            productService.add(productPojo);
        } catch (ApiException err) {
            assertEquals("product with this brand category combination does not exist",err.getMessage());
        }
    }

    //empty mrp field
    @Test
    public void testEmptyMrp() {
        try {
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,null,"name");
            productService.add(productPojo);
        } catch (ApiException err) {
            assertEquals("MRP cannot be empty",err.getMessage());
        }
    }

    //empty name field
    @Test
    public void testEmptyName() {
        try {
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"");
            productService.add(productPojo);
        } catch (ApiException err) {
            assertEquals("name cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testBrand_CategoryInvalid() {
        try {
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",0,1.12,"name");
            productService.add(productPojo);
        } catch (ApiException err) {
            assertEquals("product with this brand category combination does not exist",err.getMessage());
        }
    }

    //*******adding unwanted values tests******
    //duplicate barcode
    @Test
    public void testDuplicateBarcode() throws ApiException {
        try {
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
            productService.add(productPojo);

            ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode",brandId,1.13,"name2");
            productService.add(productPojo1);
        }
        catch(ApiException err) {
            assertEquals("same barcode already exists",err.getMessage());
        }
    }

    //test for brand_category not existing
    @Test
    public void testBrand_categoryDoesNotExist(){
        try {
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId+brandIdSecondPojo+1,1.12,"name");
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
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,-1.12,"name");
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
            ProductPojo productPojo = pojoHelper.makeProductPojo("barc$ode",brandId,1.12,"name");
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
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
            productService.add(productPojo);
            ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode2",brandId,1.12,"name");
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
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId+brandIdSecondPojo+1,1.12,"name");
            productService.add(productPojo);
        }
        catch(ApiException err) {
            assertEquals("product with this brand category combination does not exist",err.getMessage());
        }
    }

    @Test
    public void testTruncateMrp() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.1353,"name");
        Integer productId = productService.add(productPojo);
        ProductPojo productPojo1 = productService.get(productId);
        assertEquals(new Double(1.14) , productPojo1.getMrp());
    }

    //***barcode must exist
    @Test
    public void testBarcodeMustExist() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("existingBarcode",brandId,1.13,"name");
        productService.add(productPojo);
        ProductPojo productPojo1 = productService.barcodeMustExist("existingbarcode");
        ProductPojo productPojo2 = productService.barcodeMustExist("notExistingBarcode");
        assertNotNull(productPojo1);
        assertNull(productPojo2);
    }

    //list of same brand_category
    @Test
    public void testBrandCategory() throws ApiException {
        ArrayList<ProductPojo> productPojoList = new ArrayList<ProductPojo>();
        for( int i = 1 ; i <= 7 ; i ++ ) {
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode" + i,brandId,1.13,"name"+i);
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
            ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode" + 1,brandId,1.13,"name"+1);
            Integer id2 = productService.add(productPojo1);
            ProductPojo productPojo2 = productService.get(id2);
            assertEquals(productPojo1,productPojo2);
    }

    @Test(expected = ApiException.class)
    public void testGetIdApiException() throws ApiException {
        productService.get(9999);
    }

    //getBrandPojoFromBrandCategory function
    @Test
    public void testGetBrandPojoFromBrandCategory() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.13,"name");
        productService.add(productPojo);
        BrandPojo brandPojo1 = productService.getBrandPojoFromBrandCategory(productPojo);
        assertEquals(brandPojo,brandPojo1);
    }

    //getBrandPojoFromForm function
    @Test
    public void testGetBrandPojoFromForm() throws ApiException {

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

            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.1,"name");

            ProductForm f = new ProductForm();
            f.setBarcode("barcode");
            f.setBrand("brand1");
            f.setCategory("category4");
            f.setMrp(1.1);
            f.setName("name");

            productService.getBrandPojoFromForm(f);
        }
        catch (ApiException err) {
            assertEquals("this product does not exist",err.getMessage());
        }
    }

    //test getAll() function
    @Test
    public void testGetAll() throws ApiException {
        ArrayList<ProductPojo> productPojoList = new ArrayList<ProductPojo>();
        for( int i = 1 ; i <= 7 ; i ++ ) {

            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode"+i,brandId,1.13,"name"+i);
            productPojoList.add(productPojo);
            productService.add(productPojo);
        }
        List<ProductPojo> productPojoList1 = productService.getAll();
        assertTrue(productPojoList1.size() == productPojoList.size() && productPojoList1.containsAll(productPojoList) && productPojoList.containsAll(productPojoList1));
    }

    //getProductId
    @Test
    public void testGetProductIdNoError() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode"+1,brandId,1.13,"name"+1);
        productService.add(productPojo);
        Integer id = productService.getProductId("barcode1");
        assertEquals("barcode"+1,productService.get(id).getBarcode());
        assertEquals(brandId,productService.get(id).getBrand_category());
        assertEquals("name"+1,productService.get(id).getName());
    }

    @Test
    public void testGetProductIdError() throws ApiException {
        try {
            productService.getProductId("barcode1");
        }
        catch (ApiException err) {
            assertEquals("this barcode does not exist",err.getMessage());
        }
    }

    //getPojoFromBarcode
    @Test
    public void testGetPojoFromBarcode() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode"+1,brandId,1.13,"name"+1);
        productService.add(productPojo);
        ProductPojo productPojo1 = productService.getPojoFromBarcode("barcode1");
        assertEquals("barcode"+1,productPojo1.getBarcode());
        assertEquals(brandId,productPojo1.getBrand_category());
        assertEquals("name"+1,productPojo1.getName());
        assertEquals(new Double(1.13),productPojo1.getMrp());
    }

    //*****test getProductId function*******
    //barcode exists
    public void testGetProductId() throws ApiException {

        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.1,"name");
        Integer productId = productService.add(productPojo);
        Integer productId1 = productService.getProductId("barcode");
        assertEquals(productId,productId1);
    }

    //*****test getProductId function*******
    //barcode does not exists
    public void testGetProductIdNotExist() throws ApiException {
       try {

           ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.1,"name");
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

            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
            productService.add(productPojo);


            ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode1",brandId,1.13,"name2");
            Integer productId = productService.add(productPojo);


            ProductPojo productPojo2 = pojoHelper.makeProductPojo("barcode",brandId,1.13,"name4");
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

            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
            Integer productId = productService.add(productPojo);

            ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
            productService.update(productId,productPojo1);
        } catch (ApiException err) {
            assertEquals("barcode cannot be empty",err.getMessage());
        }
    }

    //empty brand_category field in update
    @Test
    public void testEmptyBrand_categoryUpdate() {
        try {

            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode",null,1.12,"name");
            productService.update(productId,productPojo1);
        } catch (ApiException err) {
            assertEquals("brand_category cannot be empty",err.getMessage());
        }
    }

    //empty mrp field in update
    @Test
    public void testEmptyMrpUpdate() {
        try {

            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode",brandId,null,"name");
            productService.update(productId,productPojo1);
        } catch (ApiException err) {
            assertEquals("MRP cannot be empty",err.getMessage());
        }
    }

    //empty name field in update
    @Test
    public void testEmptyNameUpdate() {
        try {

            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode",brandId,1.13,null);
            productService.update(productId,productPojo1);
        } catch (ApiException err) {
            assertEquals("name cannot be empty",err.getMessage());
        }
    }

    //check negative mrp in update
    @Test
    public void testMRPCanNotBeNegativeUpdate() {
        try {

            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode",brandId,-1.12,"name");
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
            ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
            Integer productId = productService.add(productPojo);
            ProductPojo productPojo1 = pojoHelper.makeProductPojo("ba$rcode",brandId,1.12,"name");
            productService.update(productId,productPojo1);
        }
        catch(ApiException err) {
            assertEquals("barcode must only contain alphanumeric characters",err.getMessage());
        }
    }

    //no values changed in update function
    @Test
    public void testNoValuesChangesInUpdate() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
        Integer productId = productService.add(productPojo);
        ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
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
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
        Integer productId = productService.add(productPojo);
        ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode1",brandIdSecondPojo,1.10,"name1");
        productService.update(productId,productPojo1);
        ProductPojo productpojo = productService.get(productId);
        assertEquals("barcode1",productpojo.getBarcode());
        assertEquals(brandIdSecondPojo,productpojo.getBrand_category());
        assertEquals(new Double(1.10),productpojo.getMrp());
        assertEquals("name1",productpojo.getName());
    }

    //test nothing updates returns 0
    @Test
    public void testUpdateIfNothingUpdatesReturn0() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
        Integer productId = productService.add(productPojo);
        ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
        Integer status = productService.update(productId, productPojo1);

        assertEquals(new Integer(0),status);
    }


    //test is updates returns 1
    @Test
    public void testUpdateIfUpdatesReturn1() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
        Integer productId = productService.add(productPojo);

        ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode1",brandId,1.12,"name1");
        Integer status = productService.update(productId, productPojo1);

        assertEquals(new Integer(1),status);
    }




    //***tests for get check function****
    //functionality of getCheck function works fine
    @Test
    public void testGetCheckNoError() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
        Integer productId = productService.add(productPojo);
        ProductPojo productPojo1 = productService.getCheck(productId);

        assertEquals("barcode",productPojo1.getBarcode());
        assertEquals(brandId,productPojo1.getBrand_category());
        assertEquals(new Double(1.12),productPojo1.getMrp());
        assertEquals("name",productPojo1.getName());
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
        ProductPojo productPojo = pojoHelper.makeProductPojo("bArCodE",brandId,1.12,"naME");
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

    //filecheck tests
    //5000+ size
    @Test
    public void testSizeHigherThan5000() throws ApiException {
        try {
            List<ProductFormString> formList =new ArrayList<>();
            for(int i=0;i<5001;i++){
                ProductFormString tem = new ProductFormString();
                formList.add(tem);
            }
            productService.fileCheck(formList);
        }
        catch(ApiException err) {
            assertEquals("maximum number of rows can be only 5000",err.getMessage());
        }
    }

    //empty brand
    @Test
    public void testFileCheckBrandEmpty() throws ApiException {
        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("ksa","","category","1","name");
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("brand field is empty",errorList.get(0).getMessage());
    }

    //empty category
    @Test
    public void testFileCheckCategoryEmpty() throws ApiException {
        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("ksa","brand","","1","name");
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("category field is empty",errorList.get(0).getMessage());
    }

    //empty barcode
    @Test
    public void testFileCheckBarcodeEmpty() throws ApiException {
        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("","brand","category","1","name");
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("barcode field is empty",errorList.get(0).getMessage());
    }

    //empty mrp
    @Test
    public void testFileCheckMrpEmpty() throws ApiException {
        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("sdf","brand","category","","name");
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("mrp field is empty",errorList.get(0).getMessage());
    }

    //empty name
    @Test
    public void testFileCheckNameEmpty() throws ApiException {
        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("sdf","brand","category","8",null);
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("name field is empty",errorList.get(0).getMessage());
    }

    //empty mrp negative
    @Test
    public void testFileCheckMrpNegative() throws ApiException {
        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("sdf","brand","category","-1","name");
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("mrp is negative",errorList.get(0).getMessage());
    }

    //mrp invalid
    @Test
    public void testFileCheckMrpInvalid() throws ApiException {
        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("sdf","brand","category","nsmd","name");
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("mrp format is invalid",errorList.get(0).getMessage());
    }

    //duplicate barcode
    @Test
    public void testFileCheckBarcodeDuplicate() throws ApiException {
//        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
//        productService.add(productPojo);

        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("barcode","brand","category","1","name4");
        formList.add(productFormString);
        ProductFormString productFormString1 = formHelper.makeProductFormString("barcode","brand","category","1","name3");
        formList.add(productFormString1);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("duplicate barcode",errorList.get(1).getMessage());
    }

    //barcode must only contain alphanumeric characters
    @Test
    public void testFileCheckBarcodeAlphanumericOnly() throws ApiException {
        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("ba$rcode","brand","category","1","name");
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("barcode must only contain alphanumeric characters",errorList.get(0).getMessage());
    }


    //brand category combination not exists
    @Test
    public void testFileCheckBrandCategoryNotExist() throws ApiException {
        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("barcode","brand","category2","1","name3");
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("no such brand category combination exists",errorList.get(0).getMessage());
    }

    //duplicate product
    @Test
    public void testFileCheckDuplicateProduct2() throws ApiException {
        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("barcode","brand","category","1","name");
        ProductFormString productFormString1 = formHelper.makeProductFormString("barcdfode","brand","category","1.5","name");
        formList.add(productFormString);
        formList.add(productFormString1);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("duplicate product",errorList.get(1).getMessage());
    }


    //duplicate product
    @Test
    public void testFileCheckDuplicateBarcode() throws ApiException {
        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("barcode","brand","category","1","name");
        ProductFormString productFormString1 = formHelper.makeProductFormString("barcode","brand","category","1.5","name11");
        formList.add(productFormString);
        formList.add(productFormString1);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("duplicate barcode",errorList.get(1).getMessage());
    }

    //duplicate product
    @Test
    public void testFileCheckDuplicateProduct() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
        productService.add(productPojo);

        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("barcode","brand","category","1","name");
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("duplicate product",errorList.get(0).getMessage());
    }

    //duplicate barcode
    @Test
    public void testFileCheckDuplicateBarcode2() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
        productService.add(productPojo);

        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("barcode","brand","category","1","name2");
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("duplicate barcode",errorList.get(0).getMessage());
    }



    //no error in this line
    @Test
    public void testFileCheckNoError() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode3",brandId,1.12,"name2");
        productService.add(productPojo);

        List<ProductFormString> formList =new ArrayList<>();
        ProductFormString productFormString = formHelper.makeProductFormString("barcode","brand","category","1","name");
        formList.add(productFormString);
        ArrayList<ErrorsProduct> errorList = productService.fileCheck(formList);
        assertEquals("no error in this line",errorList.get(0).getMessage());
    }


}

package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
import helper.pojoHelper;
import io.swagger.annotations.Api;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BrandServiceTest extends AbstractUnitTest {
    @Autowired
    private BrandService brandService;

    //add function tests
    @Test
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand","category");
        brandService.add(brandPojo);
        BrandPojo brandIdPojo = brandService.getId("brand","category");
        BrandPojo brandPojo1 = brandService.get(brandIdPojo.getId());
        assertEquals("brand",brandPojo1.getBrand());
        assertEquals("category",brandPojo1.getCategory());
    }

    @Test
    public void testNoBrandGiven() throws ApiException {
        try{
            BrandPojo brandPojo = pojoHelper.makeBrandPojo(null,"category");
            brandService.add(brandPojo);
        }
        catch(ApiException err) {
            assertEquals("brand cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testNoCategoryGiven() throws ApiException {
        try{
            BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand",null);
            brandService.add(brandPojo);
        }
        catch(ApiException err) {
            assertEquals("category cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testEmptyStringInBrandGiven() throws ApiException {
        try{
            BrandPojo brandPojo = pojoHelper.makeBrandPojo("","category");
            brandService.add(brandPojo);
        }
        catch(ApiException err) {
            assertEquals("brand cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testEmptyStringInCategoryGiven() throws ApiException {
        try{
            BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"," ");
            brandService.add(brandPojo);
        }
        catch(ApiException err) {
            assertEquals("category cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testCategoryAlreadyExists() throws ApiException {
        try {
                BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand","category");
                brandService.add(brandPojo);
                BrandPojo brandPojo1 = pojoHelper.makeBrandPojo("brand","category");
                brandService.add(brandPojo1);
        } catch(ApiException err) {
            assertEquals("category already exists",err.getMessage());
        }
    }

    @Test
    public void testGet() throws ApiException {
        for(int i = 1; i <= 4 ; i ++ ) {
            BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"+i,"category"+i);
            brandService.add(brandPojo);
        }
        Integer id=null;
        BrandPojo brandPojo1 = brandService.getId("brand4","category4");
        if(brandPojo1!=null)
            id = brandPojo1.getId();
        assertEquals("brand4" , brandService.get(id).getBrand());
        assertEquals("category4" , brandService.get(id).getCategory());
    }

    @Test
    public void testGetAll() throws ApiException {
        for(int i = 1; i <= 4 ; i ++ ) {
            BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"+i,"category"+i);
            brandService.add(brandPojo);
        }
        List<BrandPojo> brandPojoList = brandService.getAll();
        for(int i = 1; i <= 4 ; i ++ ) {
            assertEquals("brand"+i ,brandPojoList.get(i-1).getBrand());
            assertEquals("category"+i ,brandPojoList.get(i-1).getCategory());
        }
    }

    @Test
    public void testUpdateEmptyBrand() throws ApiException {
        try {
            for (int i = 1; i <= 2; i++) {
                BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"+i,"category"+i);
                brandService.add(brandPojo);
            }
                BrandPojo brandPojo = new BrandPojo();
                brandPojo.setCategory("category");
                brandService.update(2, brandPojo);
    } catch(ApiException err){
            assertEquals("brand cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testUpdateEmptyCategory() throws ApiException {
            try {
                    for(int i = 1 ; i <= 2; i++) {
                        BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"+i,"category"+i);
                        brandService.add(brandPojo);
                    }
                    BrandPojo brandPojo = new BrandPojo();
                    brandPojo.setBrand("brand");
                    brandService.update(2,brandPojo);
            }
            catch (ApiException err) {
                assertEquals("category cannot be empty",err.getMessage());
            }
    }

//no update in values
    @Test(expected = ApiException.class)
    public void testUpdateNoChangeInInputs() throws ApiException {
            for (int i = 1; i <= 2; i++) {
                BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"+i,"category"+i);
                brandService.add(brandPojo);
            }
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand" + 1);
            brandPojo.setCategory("category" + 1);
            Integer status = brandService.update(2, brandPojo);
            assertEquals(new Integer(0),status);
    }

    @Test(expected = ApiException.class)
    public void testUpdateOnChangeInInputs() throws ApiException {
        for (int i = 1; i <= 2; i++) {
            BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"+i,"category"+i);
            brandService.add(brandPojo);
        }
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand" + 2);
        brandPojo.setCategory("category" + 2);
        Integer status = brandService.update(2, brandPojo);
        assertEquals(new Integer(1),status);
    }
    @Test
    public void testUpdateCheckIfUpdatesDuplicateValue() throws ApiException {
        try{
                for(int i = 1;i<=2;i++) {
                BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"+i,"category"+i);
                brandService.add(brandPojo);
            }

            BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"+1,"category"+1);
                BrandPojo brandPojo1 = brandService.getId("brand2","category2");
                brandService.update(brandPojo1.getId(),brandPojo);
            }
        catch(ApiException err){
            assertEquals("category already exists",err.getMessage());
        }
    }

    @Test
    public void testUpdateValues() throws ApiException {
        for(int i = 1 ; i <= 2; i++) {
            BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"+i,"category"+i);
            brandService.add(brandPojo);
        }
        BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"+3,"category"+3);
        Integer id=null;
        BrandPojo brandPojo1 = brandService.getId("brand2","category2");
        if(brandPojo1!=null)
            id = brandPojo1.getId();
        brandService.update(id,brandPojo);
        BrandPojo brandPojo2 = brandService.get(id);
        assertEquals("brand"+3 ,brandPojo2.getBrand());
        assertEquals("category"+3 ,brandPojo2.getCategory());
    }

    @Test
    public void testGetId() throws ApiException {
        for(int i = 1 ; i <= 2; i++) {
            BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand"+i,"category"+i);
            brandService.add(brandPojo);
        }
        BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand2","category2");
        BrandPojo brandPojo1 = brandService.getId(brandPojo.getBrand(),brandPojo.getCategory());
        assertEquals("brand2",brandPojo1.getBrand());
        assertEquals("category2",brandPojo1.getCategory());
    }

    @Test
    public void testNormalize() throws ApiException {
        BrandPojo brandPojo = pojoHelper.makeBrandPojo("nIkE","ShOE");
        brandService.normalize(brandPojo);
        assertEquals("shoe",brandPojo.getCategory());
        assertEquals("nike",brandPojo.getBrand());
    }
}

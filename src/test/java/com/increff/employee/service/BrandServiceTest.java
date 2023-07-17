package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
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
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandService.add(brandPojo);
        BrandPojo brandPojo1 = brandService.get(1);
        assertEquals("brand",brandPojo1.getBrand());
        assertEquals("category",brandPojo1.getCategory());
    }

    @Test
    public void testNoBrandGiven() throws ApiException {
        try{
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setCategory("category");
            brandService.add(brandPojo);
        }
        catch(ApiException err) {
            assertEquals("brand cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testNoCategoryGiven() throws ApiException {
        try{
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand");
            brandService.add(brandPojo);
        }
        catch(ApiException err) {
            assertEquals("category cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testEmptyStringInBrandGiven() throws ApiException {
        try{
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("");
            brandPojo.setCategory("category");
            brandService.add(brandPojo);
        }
        catch(ApiException err) {
            assertEquals("brand cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testEmptyStringInCategoryGiven() throws ApiException {
        try{
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand");
            brandPojo.setCategory(" ");
            brandService.add(brandPojo);
        }
        catch(ApiException err) {
            assertEquals("category cannot be empty",err.getMessage());
        }
    }

    @Test
    public void testCategoryAlreadyExists() throws ApiException {
        try {
                BrandPojo brandPojo = new BrandPojo();
                brandPojo.setBrand("brand");
                brandPojo.setCategory("category");
                brandService.add(brandPojo);
                BrandPojo brandPojo1 = new BrandPojo();
                brandPojo1.setBrand("brand");
                brandPojo1.setCategory("category");
                brandService.add(brandPojo1);
        } catch(ApiException err) {
            assertEquals("category already exists",err.getMessage());
        }
    }

    @Test
    public void testGet() throws ApiException {
        for(int i = 1; i <= 4 ; i ++ ) {
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand"+i);
            brandPojo.setCategory("category"+i);
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
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand"+i);
            brandPojo.setCategory("category"+i);
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
                BrandPojo brandPojo = new BrandPojo();
                brandPojo.setBrand("brand" + i);
                brandPojo.setCategory("category" + i);
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
                        BrandPojo brandPojo = new BrandPojo();
                        brandPojo.setBrand("brand"+i);
                        brandPojo.setCategory("category"+i);
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

//    @Test(expected = ApiException.class)
//    public void testUpdateNoChangeInInputs() throws ApiException {
//        try{
//            for (int i = 1; i <= 2; i++) {
//                BrandPojo brandPojo = new BrandPojo();
//                brandPojo.setBrand("brand" + i);
//                brandPojo.setCategory("category" + i);
//                brandService.add(brandPojo);
//            }
//            BrandPojo brandPojo = new BrandPojo();
//            brandPojo.setBrand("brand" + 1);
//            brandPojo.setCategory("category" + 1);
//            brandService.update(2, brandPojo);
//        }
//        catch(ApiException err) {
//
//        }
//    }
    @Test
    public void testUpdateCheckIfUpdatesDuplicateValue() throws ApiException {
        try{
                for(int i = 1;i<=2;i++) {
                BrandPojo brandPojo = new BrandPojo();
                brandPojo.setBrand("brand" + 1);
                brandPojo.setCategory("category" + i);
                brandService.add(brandPojo);
            }

                BrandPojo brandPojo = new BrandPojo();
                brandPojo.setBrand("brand"+1);
                brandPojo.setCategory("category"+1);
                BrandPojo brandPojo1 = brandService.getId("brand1","category2");
                brandService.update(brandPojo1.getId(),brandPojo);
            }
        catch(ApiException err){
            assertEquals("category already exists",err.getMessage());
        }
    }

    @Test
    public void testUpdateValues() throws ApiException {
        for(int i = 1 ; i <= 2; i++) {
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand"+i);
            brandPojo.setCategory("category"+i);
            brandService.add(brandPojo);
        }
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand"+3);
        brandPojo.setCategory("category"+3);
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
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand"+i);
            brandPojo.setCategory("category"+i);
            brandService.add(brandPojo);
        }
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand2");
        brandPojo.setCategory("category2");
        BrandPojo brandPojo1 = brandService.getId(brandPojo.getBrand(),brandPojo.getCategory());
        assertEquals("brand2",brandPojo1.getBrand());
        assertEquals("category2",brandPojo1.getCategory());
    }

    @Test
    public void testNormalize() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("nIkE");
        brandPojo.setCategory("ShOE");
        brandService.normalize(brandPojo);
        assertEquals("shoe",brandPojo.getCategory());
    }
}

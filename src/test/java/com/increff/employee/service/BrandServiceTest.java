package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    }

    @Test(expected = ApiException.class)
    public void testNoBrandGiven() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setCategory("category");
        brandService.add(brandPojo);
    }

    @Test(expected = ApiException.class)
    public void testNoCategoryGiven() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandService.add(brandPojo);
    }

    @Test(expected = ApiException.class)
    public void testCategoryAlreadyExists() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandService.add(brandPojo);
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandService.add(brandPojo);
    }

    @Test
    public void testGetId() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        for(int i = 0; i < 4 ; i ++ ) {
            brandPojo.setBrand("brand"+i);
            brandPojo.setCategory("category"+i);
            brandService.add(brandPojo);
        }
        brandService.get(4);
    }

}

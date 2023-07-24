package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import helper.pojoHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BrandDaoTest extends AbstractUnitTest {

    @Autowired
    private BrandDao dao;

    @Test
    public void testInsert() throws ApiException {
        BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand","category");
        dao.insert(brandPojo);
        List<BrandPojo> brandPojoList = dao.selectAll();
        assertEquals("brand",brandPojoList.get(0).getBrand());
        assertEquals("category",brandPojoList.get(0).getCategory());
    }

    @Test
    public void testSelect() throws ApiException {
        BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand","category");
        dao.insert(brandPojo);
        List<BrandPojo> brandPojoList = dao.selectAll();
        Integer id = brandPojoList.get(0).getId();
        BrandPojo brandPojo1 = dao.select(id);
        assertEquals("brand",brandPojo1.getBrand());
        assertEquals("category",brandPojo1.getCategory());
    }

    @Test
    public void testCheckerForDuplicate() throws ApiException {
        BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand","category");
        dao.insert(brandPojo);
        assertNotNull(dao.checkerForDuplicate("brand","category"));
    }

    @Test
    public void testBrandMustExist(){
        BrandPojo brandPojo = pojoHelper.makeBrandPojo("brand","category");
        dao.insert(brandPojo);
        List<BrandPojo> brandPojoList = dao.selectAll();
        Integer id = brandPojoList.get(0).getId();
        assertNotNull(dao.brandMustExist(id));
    }

}

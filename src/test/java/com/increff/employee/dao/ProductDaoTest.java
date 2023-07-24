package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import helper.pojoHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProductDaoTest extends AbstractUnitTest {
    @Autowired
    private ProductDao dao;

    @Test
    public void testInsert() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",1,1.1,"name");
        dao.insert(productPojo);
        List<ProductPojo> productPojoList = dao.selectAll();
        assertEquals("barcode",productPojoList.get(0).getBarcode());
        assertEquals(new Integer(1),productPojoList.get(0).getBrand_category());
        assertEquals(new Double(1.1) ,productPojoList.get(0).getMrp());
        assertEquals("name",productPojoList.get(0).getName());
    }

    @Test
    public void testBarcodeMustExist() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",1,1.1,"name");
        dao.insert(productPojo);
        assertNotNull(dao.barcodeMustExist("barcode"));
    }

    @Test
    public void testBarcodeMustExistList() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",1,1.1,"name");
        dao.insert(productPojo);
        assertNotNull(dao.barcodeMustExistList("barcode"));
    }

    @Test
    public void testBrandCategory() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",1,1.1,"name");
        dao.insert(productPojo);
        assertNotNull(dao.brandCategory(2));
    }

    @Test
    public void testCheckerForDuplicate() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",1,1.1,"name");
        dao.insert(productPojo);
        assertNotNull(dao.checkerForDuplicate(1,"name"));
    }

    @Test
    public void testSelect() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",1,1.1,"name");
        Integer id = dao.insert(productPojo);
        ProductPojo productPojo1 = dao.select(id);
        assertEquals("barcode",productPojo1.getBarcode());
        assertEquals(new Integer(1),productPojo1.getBrand_category());
        assertEquals(new Double(1.1),productPojo1.getMrp());
        assertEquals("name",productPojo1.getName());
    }

    @Test
    public void testSelectBarcode() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",1,1.1,"name");
        Integer id = dao.insert(productPojo);
        ProductPojo productPojo1 = dao.selectBarcode("barcode");
        assertEquals("barcode",productPojo1.getBarcode());
        assertEquals(new Integer(1),productPojo1.getBrand_category());
        assertEquals(new Double(1.1),productPojo1.getMrp());
        assertEquals("name",productPojo1.getName());
    }

    @Test
    public void testSelectAll() throws ApiException {
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",1,1.1,"name");
        dao.insert(productPojo);
        List<ProductPojo> productPojoList = dao.selectAll();
        assertEquals(1,productPojoList.size());
        assertEquals("barcode",productPojoList.get(0).getBarcode());
        assertEquals(new Integer(1),productPojoList.get(0).getBrand_category());
        assertEquals(new Double(1.1),productPojoList.get(0).getMrp());
        assertEquals("name",productPojoList.get(0).getName());
    }

}

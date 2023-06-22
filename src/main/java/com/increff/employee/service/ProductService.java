package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.employee.dao.BrandDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class ProductService {

    @Autowired
    private ProductDao dao;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo p) throws ApiException {
        normalize(p);
        if(StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException(" cannot be empty");
        }
        if(p.getBrand_category()==null) {
            throw new ApiException(" cannot be empty");
        }
        if(StringUtil.isEmpty(p.getName())) {
            throw new ApiException(" cannot be empty");
        }
        if(p.getMrp()==null){
            throw new ApiException(" cannot be empty");
        }
        if(p.getMrp()<0) {
            throw new ApiException("MRP can't be a negative value");
        }

        //barcode originality
        if(brandDao.brandMustExist(p.getBrand_category())==null)
        throw new ApiException(" brand of such brand category does not exist ");

        if(dao.barcodeMustExist(p.getBarcode())!=null)
            throw new ApiException(" put another barcode ");

            if(dao.checkerForDuplicate(p.getBrand_category(), p.getName())==null)
            dao.insert(p);
            else throw new ApiException("this entry already exists");


        inventoryService.add(p.getId());

    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, ProductPojo p) throws ApiException {
        normalize(p);
        ProductPojo tempPojo = getCheck(id);
        if(StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("brand cannot be empty");
        }
        if(p.getBrand_category()==null) {
            throw new ApiException("category cannot be empty");
        }
        if(StringUtil.isEmpty(p.getName())) {
            throw new ApiException("name cannot be empty");
        }
        if(p.getMrp()==null) {
            throw new ApiException("MRP cannot be empty");
        }
        if(p.getMrp()<0) {
            throw new ApiException("MRP can't be a negative value");
        }
        //

        //NOTHING INSERTED
        if(tempPojo.getBarcode().equals(p.getBarcode()) && tempPojo.getName().equals(p.getName()) && tempPojo.getMrp()==p.getMrp() && tempPojo.getBrand_category()==p.getBrand_category()){
            return;
        }
        tempPojo.setBarcode(p.getBarcode());
        tempPojo.setBrand_category(p.getBrand_category());
        tempPojo.setName(p.getName());
        tempPojo.setMrp(p.getMrp());

        if(dao.checkerForDuplicate(p.getBrand_category(), p.getName())==null) {
            inventoryService.add(tempPojo.getId());
        }
    }

    @Transactional
    public ProductPojo getCheck(Integer id) throws ApiException {
        ProductPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Employee with given ID does not exit, id: " + id);
        }
        return p;
    }

    protected static void normalize(ProductPojo p) {
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
        p.setName(StringUtil.toLowerCase(p.getName()));
    }


}


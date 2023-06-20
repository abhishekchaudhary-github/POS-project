package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;
import com.increff.employee.dao.BrandDao;

@Service
public class ProductService {

    @Autowired
    private ProductDao dao;

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
            throw new ApiException(" can't be a negative value");
        }
        brandCategoryExistanceCheck(p);
        dao.insert(p);

    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, ProductPojo p) throws ApiException {
        normalize(p);
        ProductPojo tempPojo = getCheck(id);
        tempPojo.setBarcode(p.getBarcode());
        tempPojo.setBrand_category(p.getBrand_category());
        tempPojo.setName(p.getName());
        tempPojo.setMrp(p.getMrp());
    }

    @Transactional
    public ProductPojo getCheck(int id) throws ApiException {
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


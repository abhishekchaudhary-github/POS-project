package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.util.StringUtil;

@Service
public class BrandService {

    @Autowired
    private BrandDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(BrandPojo p) throws ApiException {
        normalize(p);
        if(StringUtil.isEmpty(p.getBrand())) {
            throw new ApiException("brand cannot be empty");
        }
        if(StringUtil.isEmpty(p.getCategory())) {
            throw new ApiException("category cannot be empty");
        }
        if(dao.checkerForDuplicate(p.getBrand(), p.getCategory())==null)
        dao.insert(p);
        else throw new ApiException("category already exists");

    }



    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public List<BrandPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, BrandPojo p) throws ApiException {
        if(StringUtil.isEmpty(p.getBrand())) {
            throw new ApiException("brand cannot be empty");
        }
        if(StringUtil.isEmpty(p.getCategory())) {
            throw new ApiException("category cannot be empty");
        }
        normalize(p);
        BrandPojo tempPojo = getCheck(id);
        if(dao.select(id).getBrand().equals(p.getBrand()) && dao.select(id).getCategory().equals(p.getCategory())){
            return;
        }
        if(dao.checkerForDuplicate(p.getBrand(), p.getCategory())==null){
        tempPojo.setBrand(p.getBrand());
        tempPojo.setCategory(p.getCategory());}
        else throw new ApiException("category already exists");
    }

    @Transactional
    public BrandPojo getId(String productBrand,String productCategory){
        return dao.checkerForDuplicate(productBrand,productCategory);
    }
    @Transactional
    public BrandPojo getCheck(Integer id) throws ApiException {
        BrandPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Employee with given ID does not exit, id: " + id);
        }
        return p;
    }

    protected static void normalize(BrandPojo p) {
        p.setBrand(StringUtil.toLowerCase(p.getBrand()));
        p.setCategory(StringUtil.toLowerCase(p.getCategory()));
    }

}
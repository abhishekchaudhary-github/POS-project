package com.increff.employee.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.employee.model.BrandForm;
import com.increff.employee.model.Errors;
import com.increff.employee.model.ErrorsBrand;
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

    public BrandPojo brandMustExist (Integer brand_category) {
        return dao.brandMustExist(brand_category);
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
    public Integer update(Integer id, BrandPojo p) throws ApiException {
        if(StringUtil.isEmpty(p.getBrand())) {
            throw new ApiException("brand cannot be empty");
        }
        if(StringUtil.isEmpty(p.getCategory())) {
            throw new ApiException("category cannot be empty");
        }
        normalize(p);
        BrandPojo tempPojo = getCheck(id);
        if(dao.select(id).getBrand().equals(p.getBrand()) && dao.select(id).getCategory().equals(p.getCategory())){
            return 0;
        }
        if(dao.checkerForDuplicate(p.getBrand(), p.getCategory())==null){
        tempPojo.setBrand(p.getBrand());
        tempPojo.setCategory(p.getCategory());
        return 1;
        }
        else throw new ApiException("this category already exists");
    }

    @Transactional
    public BrandPojo getId(String productBrand,String productCategory){
        return dao.checkerForDuplicate(productBrand,productCategory);
    }
    @Transactional
    public BrandPojo getCheck(Integer id) throws ApiException {
        BrandPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Brand with given ID does not exit, id: " + id);
        }
        return p;
    }

    protected static void normalize(BrandPojo p) {
        p.setBrand(StringUtil.toLowerCase(p.getBrand()));
        p.setCategory(StringUtil.toLowerCase(p.getCategory()));
    }

    @Transactional
    public ErrorsBrand checkError(BrandForm brandForm, List<BrandForm> list) throws ApiException {
        ErrorsBrand errors = new ErrorsBrand();
        boolean checks1=false;
        String brand = brandForm.getBrand();
        String category = brandForm.getCategory();
        if(brand==null||brand==""){
            checks1=true;
            errors.setMessage("brand field is empty");
            errors.setErrorCount(1);
        }
        else if(category==null||category==""){
            checks1=true;
            errors.setMessage("category field is empty");
            errors.setErrorCount(1);
        }
        else if(dao.checkerForDuplicate(brandForm.getBrand(), brandForm.getCategory())!=null) {
            checks1=true;
            errors.setMessage("this category already exists");
            errors.setErrorCount(1);
        }
            for (BrandForm brandForm1 : list) {
                if (brandForm1.getBrand().equals(brand) && brandForm1.getCategory().equals(category)) {
                    errors.setMessage("this category already exists");
                    errors.setErrorCount(1);
                   checks1=true;
                    //break;
                }
            }
        if(checks1 == false) {
            errors.setMessage("no error in this line");
            errors.setErrorCount(0);
        }
        errors.setBrand(brand);
        errors.setCategory(category);
        return errors;
    }

    @Transactional
    public ArrayList<ErrorsBrand> fileCheck(List<BrandForm> form) throws ApiException {
        if(form.size()>5000) {
            throw new ApiException("maximum rows can be 5000");
        }
        boolean checkError =false;
        ArrayList<ErrorsBrand> data = new ArrayList<ErrorsBrand>();
        ArrayList<BrandForm> brandPojoList = new ArrayList<>();
        for(BrandForm BrandItem : form){
            ErrorsBrand error = checkError(BrandItem,brandPojoList);
            if(checkError==true || error.getErrorCount()>0) {
                error.setCheckError(true);
            }
            brandPojoList.add(BrandItem);
            data.add(error);
        }
        return data;
    }
}
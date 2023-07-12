package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class ProductService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductDao dao;

    @Autowired
    private BrandDao brandDao;


    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductPojo p) throws ApiException {
        ProductPojo productPojo = dao.barcodeMustExist(p.getBarcode());
        if(productPojo!=null) {
            throw new ApiException("Barcode Already exists");
        }
        if(p.getBrand_category()==null||p.getBrand_category()==0){
            throw new ApiException("no such item exists");
        }
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


  //      inventoryService.add(p.getId());

    }




    ///////
    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo barcodeMustExist(String barcode) {
        return dao.barcodeMustExist(barcode);
    }

//    @Transactional(rollbackOn = ApiException.class)
//    public String checkBarcodeThrowErrorIfExists(String barcode) throws ApiException {
//        ProductPojo productPojo = dao.barcodeMustExist(barcode);
//        if(productPojo!=null) {
//            throw new ApiException("Barcode Already exists");
//        }
//        return productPojo.getBarcode();
//    }

    //////

    @Transactional(rollbackOn = ApiException.class)
    public List<ProductPojo> brandCategory(Integer brand_category) {
        return dao.brandCategory(brand_category);
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional
    public BrandPojo getBrandPojoFromBrandCategory(ProductPojo p) throws ApiException {
        return brandService.get(p.getBrand_category());
    }

    @Transactional
    public BrandPojo getBrandPojoFromForm(ProductForm f) throws ApiException {
        BrandPojo brandPojo =  brandService.getId(f.getBrand(),f.getCategory());
        if(brandPojo==null) {
            throw new ApiException("this item does not exist");
        }
        return brandPojo;
    }


    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional
    public Integer getProductId(String barcode) throws ApiException {
        ProductPojo productPojo = dao.selectBarcode(barcode);
        if(productPojo==null) {
            throw new ApiException("barcode does not exist");
        }
        return productPojo.getId();
    }


    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, ProductPojo p) throws ApiException {
        normalize(p);
        List<ProductPojo> productPojoList = dao.barcodeMustExistList(p.getBarcode());
        if(productPojoList.size()==1) {
           if(productPojoList.get(0).getId()!=id){
               System.out.println(p.getId());
               System.out.println(productPojoList.get(0).getId());
           }
        }
        if(productPojoList.size()>1) {
            throw new ApiException("Barcode Already exists");
        }
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

    }





    @Transactional
    public ProductPojo getCheck(Integer id) throws ApiException {
        ProductPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Product with given ID does not exit, id: " + id);
        }
        return p;
    }

    protected static void normalize(ProductPojo p) {
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
        p.setName(StringUtil.toLowerCase(p.getName()));
    }


}
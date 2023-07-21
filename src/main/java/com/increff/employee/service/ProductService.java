package com.increff.employee.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.model.*;
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


    @Transactional(rollbackOn = ApiException.class)
    public Integer add(ProductPojo p) throws ApiException {
        normalize(p);
        ProductPojo productPojo = dao.barcodeMustExist(p.getBarcode());
        if(productPojo!=null) {
            throw new ApiException("same barcode already exists");
        }
        if(StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("barcode cannot be empty");
        }
        if(p.getBrand_category()==null) {
            throw new ApiException("brand_category cannot be empty");
        }

        if(p.getBrand_category()==null||p.getBrand_category()==0){
            throw new ApiException("product with this brand category combination does not exist");
        }
        if(p.getMrp()==null){
            throw new ApiException("MRP cannot be empty");
        }
        if(StringUtil.isEmpty(p.getName())) {
            throw new ApiException("name cannot be empty");
        }
        if(p.getMrp()<0) {
            throw new ApiException("MRP can not be a negative value");
        }


//        if(dao.barcodeMustExist(p.getBarcode())!=null)
//            throw new ApiException(" put another barcode ");

        if (!p.getBarcode().matches("^[a-zA-Z0-9]*$")) {
            throw new ApiException("barcode must only contain alphanumeric characters");
        }
        //barcode originality
        if(brandService.brandMustExist(p.getBrand_category())==null)
            throw new ApiException("product with this brand category combination does not exist");

        //truncate to 2 decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        p.setMrp(Double.parseDouble(decimalFormat.format(p.getMrp())));

        if(dao.checkerForDuplicate(p.getBrand_category(), p.getName())==null)
            return dao.insert(p);
        else throw new ApiException("this product already exists");


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


    @Transactional
    public ErrorsProduct checkError(ProductFormString productForm, List<ProductFormString> list,HashMap<String,Integer> hm) throws ApiException {
        ErrorsProduct errors = new ErrorsProduct();
        boolean checks1=false;
        String brand = productForm.getBrand();
        String category = productForm.getCategory();
        String barcode = productForm.getBarcode();
        String name = productForm.getName();
        String mrp = productForm.getMrp();
        BrandPojo brandPojo2 = brandService.getId(brand,category);
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
        else if(name==null||name==""){
            checks1=true;
            errors.setMessage("name field is empty");
            errors.setErrorCount(1);
        }
        else if(barcode==null||barcode==""){
            checks1=true;
            errors.setMessage("barcode field is empty");
            errors.setErrorCount(1);
        }
        else if(mrp==null||mrp=="") {
            checks1 = true;
            errors.setMessage("mrp field is empty");
            errors.setErrorCount(1);
        }
        else if(checkDouble(mrp)==false){
            checks1 = true;
            errors.setMessage("mrp format is invalid");
            errors.setErrorCount(1);
        }
        else if(Double.parseDouble(mrp)<0) {
            checks1 = true;
            errors.setMessage("mrp is negative");
            errors.setErrorCount(1);
        }
        else if(hm.containsKey(barcode)) {
            checks1 = true;
            errors.setMessage("duplicate barcode");
            errors.setErrorCount(1);
        }
        else if (!barcode.matches("^[a-zA-Z0-9]*$")) {
            checks1 = true;
            errors.setMessage("barcode must only contain alphanumeric characters");
            errors.setErrorCount(1);
        }
        else if(getBrandPojoFromForm2(brand,category) == null) {
            checks1 = true;
            errors.setMessage("no such brand category combination exists");
            errors.setErrorCount(1);
        }
        else if(dao.checkerForDuplicate(brandPojo2.getId(), name)!=null){
            checks1 = true;
            errors.setMessage("duplicate product");
            errors.setErrorCount(1);
        }
        else if(brandPojo2==null){
            checks1 = true;
            errors.setMessage("duplicate barcode");
            errors.setErrorCount(1);
        }

        if(checks1==false)
        for(int i=0;i<list.size();i++) {
            if(brand.equals(list.get(i).getBrand())&&name.equals(list.get(i).getName())&&category.equals(list.get(i).getCategory())){
                checks1 = true;
                errors.setMessage("duplicate product");
                errors.setErrorCount(1);
            }

        }
        if(checks1 == false) {
            errors.setMessage("no error in this line");
            errors.setErrorCount(0);
        }
        errors.setCategory(category);
        errors.setBrand(brand);
        errors.setBarcode(barcode);
        errors.setName(name);
        return errors;
    }

    private boolean checkDouble(String x){
        try {
            Double.parseDouble(x);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Transactional
    public ArrayList<ErrorsProduct> fileCheck(List<ProductFormString> form) throws ApiException {
        if(form.size()>5000) {
            throw new ApiException("maximum rows can be 5000");
        }
        boolean checkError =false;
        ArrayList<ErrorsProduct> data = new ArrayList<ErrorsProduct>();
        ArrayList<ProductFormString> productPojoList = new ArrayList<>();
        HashMap<String,Integer> hm = new HashMap<>();
        for(ProductFormString productItem : form){
            ErrorsProduct error = checkError(productItem,productPojoList,hm);
            if(checkError==true || error.getErrorCount()>0) {
                error.setCheckError(true);
            }
            //logic
            productPojoList.add(productItem);
            data.add(error);
            if(!hm.containsKey(productItem.getBarcode())){
                hm.put(productItem.getBarcode(),1);
            }
        }
        return data;
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
            throw new ApiException("this product does not exist");
        }
        return brandPojo;
    }

    @Transactional
    public BrandPojo getBrandPojoFromForm2(String brand,String category) throws ApiException {
        BrandPojo brandPojo =  brandService.getId(brand,category);
        return brandPojo;
    }


    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional
    public Integer getProductId(String barcode) throws ApiException {
        normalizeBarcode(barcode);
        ProductPojo productPojo = dao.selectBarcode(barcode);
        if(productPojo==null) {
            throw new ApiException("this barcode does not exist");
        }
        return productPojo.getId();
    }

    @Transactional
    public ProductPojo getPojoFromBarcode(String barcode) throws ApiException {
        normalizeBarcode(barcode);
        ProductPojo productPojo = dao.selectBarcode(barcode);
        return productPojo;
    }


    @Transactional(rollbackOn  = ApiException.class)
    public Integer update(Integer id, ProductPojo p) throws ApiException {
        normalize(p);
        List<ProductPojo> productPojoList = dao.barcodeMustExistList(p.getBarcode());
//        if(productPojoList.size()==1) {
//           if(productPojoList.get(0).getId()!=id){
//               throw new ApiException("same barcode already exists");
//           }
//        }
        if(productPojoList.size()>1) {
            throw new ApiException("same barcode already exists");
        }
        ProductPojo tempPojo = getCheck(id);
        if(StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("barcode cannot be empty");
        }
        if(p.getBrand_category()==null) {
            throw new ApiException("brand_category cannot be empty");
        }
        if(p.getMrp()==null) {
            throw new ApiException("MRP cannot be empty");
        }
        if(StringUtil.isEmpty(p.getName())) {
            throw new ApiException("name cannot be empty");
        }
        if(p.getMrp()<0) {
            throw new ApiException("MRP can't be a negative value");
        }
        //
        if (!p.getBarcode().matches("^[a-zA-Z0-9]*$")) {
            throw new ApiException("barcode must only contain alphanumeric characters");
        }


        //NOTHING INSERTED
        if((dao.select(id).getBarcode()+"").equals((p.getBarcode()+"")) && (dao.select(id).getName()+"").equals((p.getName()+"")) && (dao.select(id).getMrp()+"").equals(p.getMrp()+"") && (dao.select(id).getBrand_category()+"").equals(p.getBrand_category()+"")){
            return 0;
        }

        tempPojo.setBarcode(p.getBarcode());
        tempPojo.setBrand_category(p.getBrand_category());
        tempPojo.setName(p.getName());
        tempPojo.setMrp(p.getMrp());
        return 1;
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

    protected static String normalizeBarcode(String barcode) {
        String normalizedBarcode = StringUtil.toLowerCase(barcode);
        return normalizedBarcode;
    }

}
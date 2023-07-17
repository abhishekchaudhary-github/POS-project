package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductApiController {
    @Autowired
    private ProductService service;

    @ApiOperation(value = "Adds a Product")
    @RequestMapping(path = "/api/product", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm form) throws ApiException {
        BrandPojo brandPojo = service.getBrandPojoFromForm(form);
        ProductPojo p = convert(form,brandPojo);
        service.add(p);
    }

    @ApiOperation(value = "Gets a Product by ID")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Integer id) throws ApiException {
        ProductPojo p = service.get(id);
        BrandPojo brandPojo = service.getBrandPojoFromBrandCategory(p);
        return convert(p,brandPojo);
    }

    @ApiOperation(value = "Gets list of all Products")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> list = service.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for (ProductPojo p : list) {
            BrandPojo brandPojo = service.getBrandPojoFromBrandCategory(p);
            list2.add(convert(p,brandPojo));
        }
        return list2;
    }

    @ApiOperation(value = "Updates a Product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody ProductForm f) throws ApiException {
        BrandPojo brandPojo = service.getBrandPojoFromForm(f);
        ProductPojo p = convert(f,brandPojo);
        service.update(id, p);
    }


    private static ProductData convert(ProductPojo p,BrandPojo brandPojo) throws ApiException {
        ProductData d = new ProductData();
        d.setName(p.getName());
        d.setBarcode(p.getBarcode());
        //BrandPojo brandPojo = service.getBrandPojoFromBrandCategory(p);
        d.setBrand(brandPojo.getBrand());
        d.setCategory(brandPojo.getCategory());
        d.setMrp(p.getMrp());
        d.setId(p.getId());

        return d;
    }

    private static ProductPojo convert(ProductForm f,BrandPojo brandPojo) throws ApiException {
        ProductPojo p = new ProductPojo();
        p.setName(f.getName());
        //service.checkBarcodeThrowErrorIfExists(f.getBarcode());
        p.setBarcode(f.getBarcode());
 //       BrandPojo brandPojo = service.getBrandPojoFromForm(f);
//        if(brandPojo==null){
//            throw new ApiException("this item does not exist");
//        }
        p.setBrand_category(brandPojo.getId());
        p.setMrp(f.getMrp());
        return p;
    }

}

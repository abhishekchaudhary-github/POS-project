package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.employee.model.*;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandApiController {

    @Autowired
    private BrandService service;

    @ApiOperation(value = "Adds a Brand")
    @RequestMapping(path = "/api/brand", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm form) throws ApiException {
        BrandPojo p = convert(form);
        service.add(p);
    }

    @ApiOperation(value = "tsv upload")
    @RequestMapping(path = "/api/brand/tsv", method = RequestMethod.POST)
    public List<ErrorsBrand> addBulk(@RequestBody List<BrandForm>  form) throws ApiException {
        ArrayList<ErrorsBrand> data = service.fileCheck(form);
        if(data.get(data.size()-1).isCheckError()==false)
            for(BrandForm BrandItem : form)
                service.add(convert(BrandItem));
        return data;
    }


    @ApiOperation(value = "Gets a Brand by ID")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable Integer id) throws ApiException {
        BrandPojo p = service.get(id);
        return convert(p);
    }

    @ApiOperation(value = "Gets list of all Brands")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        List<BrandPojo> list = service.getAll();
        List<BrandData> list2 = new ArrayList<BrandData>();
        for (BrandPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    @ApiOperation(value = "Updates a Brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
    public Integer update(@PathVariable Integer id, @RequestBody BrandForm f) throws ApiException {
        BrandPojo p = convert(f);
        return service.update(id, p);
    }


    private static BrandData convert(BrandPojo p) {
        BrandData d = new BrandData();
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        d.setId(p.getId());
        return d;
    }

    private static BrandPojo convert(BrandForm f) {
        BrandPojo p = new BrandPojo();
        p.setBrand(f.getBrand());
        p.setCategory(f.getCategory());
        return p;
    }

}

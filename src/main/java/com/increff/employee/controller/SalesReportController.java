package com.increff.employee.controller;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.SalesReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class SalesReportController {

    @Autowired
    private SalesReportService salesReportService;
    @ApiOperation(value = "gives sales report")
    @RequestMapping(path = "/api/salesreport", method = RequestMethod.POST)
    public List<SalesReportData> filterSalesReport(@RequestBody SalesReportForm form) throws ApiException {
        return salesReportService.getAll(form.getStartTime(),form.getEndTime(),form.getBrand(),form.getCategory());
    }



//    @ApiOperation(value = "Gets list of all Products")
//    @RequestMapping(path = "/api/brandreport", method = RequestMethod.GET)
//    public List<ProductData> getBrandReport() throws ApiException {
//
//    }
//
//    @ApiOperation(value = "Gets list of all Products")
//    @RequestMapping(path = "/api/inventoryreport", method = RequestMethod.GET)
//    public List<ProductData> getInventoryReport() throws ApiException {
//
//    }
//


}

package com.increff.employee.controller;

import com.increff.employee.model.*;
import com.increff.employee.service.ApiException;
//import com.increff.employee.service.BrandReportService;
import com.increff.employee.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class ReportController {
    //@Autowired
    //BrandReportService brandReportService;

    @Autowired
    private ReportService salesReportService;
    @ApiOperation(value = "gives sales report")
    @RequestMapping(path = "/api/salesreport", method = RequestMethod.POST)
    public List<SalesReportData> filterSalesReport(@RequestBody SalesReportForm form) throws ApiException {
        return salesReportService.getAll(form.getStartTime(),form.getEndTime(),form.getBrand(),form.getCategory());
    }



//    @ApiOperation(value = "Gets list of all Products")
//    @RequestMapping(path = "/api/brandreport", method = RequestMethod.POST)
//    public List<BrandData> getBrandReport(@RequestBody BrandForm form) throws ApiException {
//        return brandReportService.getAll(form);
//    }






//
//    @ApiOperation(value = "Gets list of all Products")
//    @RequestMapping(path = "/api/inventoryreport", method = RequestMethod.GET)
//    public List<ProductData> getInventoryReport() throws ApiException {
//
//    }
//


}

package com.increff.employee.controller;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.model.DailyReportData;
import com.increff.employee.model.DailyReportForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.DailyReportPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.DailyReportService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
public class DailyReportApiController {
    @Autowired
    DailyReportService service;

    @ApiOperation(value = "Gets list of all schedules")
    @RequestMapping(path = "/api/scheduler", method = RequestMethod.GET)
    public List<DailyReportData> getAll() {
        List<DailyReportPojo> list = service.getAll();
        List<DailyReportData> list2 = new ArrayList<DailyReportData>();
        for (DailyReportPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    @ApiOperation(value = "gives daily report")
    @RequestMapping(path = "/api/daytodaysales", method = RequestMethod.POST)
    public List<DailyReportData> getBrandReport(@RequestBody DailyReportForm form) throws ApiException {
        List<DailyReportPojo> list = service.getAll(form);
        List<DailyReportData> list2 = new ArrayList<DailyReportData>();
        for (DailyReportPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    private static DailyReportData convert(DailyReportPojo p) {
        DailyReportData d = new DailyReportData();
        //convert date to string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = p.getDate().format(formatter);
        d.setDate(formattedDateTime);
        d.setInvoiced_items_count(p.getInvoiced_items_count());
        d.setInvoiced_orders_count(p.getInvoiced_orders_count());
        d.setTotal_revenue(p.getTotal_revenue());
        return d;
    }

}

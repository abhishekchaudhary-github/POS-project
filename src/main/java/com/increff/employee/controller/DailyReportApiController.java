package com.increff.employee.controller;

import com.increff.employee.model.SchedulerData;
import com.increff.employee.pojo.DailyReportPojo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
public class DailyReportApiController {
    @Autowired
    SchedulerService service;

    @ApiOperation(value = "Gets list of all schedules")
    @RequestMapping(path = "/api/scheduler", method = RequestMethod.GET)
    public List<SchedulerData> getAll() {
        List<DailyReportPojo> list = service.getAll();
        List<SchedulerData> list2 = new ArrayList<SchedulerData>();
        for (DailyReportPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    private static SchedulerData convert(DailyReportPojo p) {
        SchedulerData d = new SchedulerData();
        d.setDate(p.getDate());
        d.setInvoiced_items_count(p.getInvoiced_items_count());
        d.setInvoiced_orders_count(p.getInvoiced_orders_count());
        return d;
    }

}

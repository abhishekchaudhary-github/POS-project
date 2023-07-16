package com.increff.employee.scheduler;
import com.increff.employee.pojo.DailyReportPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.DailyReportService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class Scheduler {
    @Autowired
    DailyReportService dailyReportService;

    @Autowired
    ReportService reportService;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional(rollbackOn  = ApiException.class)
    public void Scheduler() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setDate(time);
        Integer lastId = dailyReportService.getLastId();
        //
        DailyReportPojo baseItem = dailyReportService.get(1);
        DailyReportPojo lastItem = dailyReportService.get(lastId);
        if(baseItem==null) {
            throw new ApiException("database altered");
        }
//        if(lastItem.==1){
//            dailyReportPojo.setInvoiced_items_count(baseItem.getInvoiced_items_count());
//            dailyReportPojo.setInvoiced_orders_count(baseItem.getInvoiced_orders_count());
//        }
//
//        else {
////            DailyReportPojo dailyReportPojo1 = dailyReportService.get(max-1);
////            DailyReportPojo dailyReportPojo2 = dailyReportService.get(max);
//            dailyReportPojo.setInvoiced_items_count(baseItem.getInvoiced_items_count()-lastItem.getInvoiced_items_count());
//            dailyReportPojo.setInvoiced_orders_count(baseItem.getInvoiced_orders_count()-lastItem.getInvoiced_orders_count());
//        }
        dailyReportPojo.setInvoiced_items_count(baseItem.getInvoiced_items_count());
        dailyReportPojo.setInvoiced_orders_count(baseItem.getInvoiced_orders_count());
        baseItem.setInvoiced_items_count(0);
        baseItem.setInvoiced_orders_count(0);
        baseItem.setTotal_revenue(0.0);
        //
        dailyReportService.add(dailyReportPojo);
    }
}

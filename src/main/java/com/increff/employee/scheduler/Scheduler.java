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
    public void Scheduler() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setDate(time);
        Integer lastId = dailyReportService.getLastId();
        if(lastId==null||lastId==0) {
            throw new ApiException("database altered");
        }
        if(lastId==1){
            DailyReportPojo dailyReportPojo2 = dailyReportService.get(lastId);
            dailyReportPojo.setInvoiced_items_count(dailyReportPojo2.getTotal_invoice());
            dailyReportPojo.setInvoiced_orders_count(dailyReportPojo2.getInvoiced_orders_count());
        }

        else {
            DailyReportPojo dailyReportPojo1 = dailyReportService.get(lastId-1);
            DailyReportPojo dailyReportPojo2 = dailyReportService.get(lastId);
            dailyReportPojo.setInvoiced_items_count(dailyReportPojo2.getTotal_invoice()-dailyReportPojo1.getTotal_invoice());
            dailyReportPojo.setInvoiced_orders_count(dailyReportPojo2.getInvoiced_orders_count()-dailyReportPojo1.getInvoiced_orders_count());
        }
        dailyReportService.add(dailyReportPojo);
    }
}

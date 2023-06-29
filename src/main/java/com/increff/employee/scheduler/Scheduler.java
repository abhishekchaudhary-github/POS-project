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
        //CHANGE LATER INVOICE
        dailyReportPojo.setInvoiced_items_count(0);
        LocalDateTime timeOfPreviousDay = time.minus(1, ChronoUnit.DAYS);
//        List<OrderPojo> orderPojoList = reportService.getOrdersByTime(timeOfPreviousDay,time);
//
//        List<OrderItemPojo> orderItemPojoBigList = new ArrayList<OrderItemPojo>();
//        for(OrderPojo orderPojo : orderPojoList){
//            List<OrderItemPojo> orderItemPojoList = reportService.getOrderItemByOrderId(orderPojo.getId());
//            orderItemPojoBigList.addAll(orderItemPojoList);
//        }

        dailyReportService.add(dailyReportPojo);
    }
}

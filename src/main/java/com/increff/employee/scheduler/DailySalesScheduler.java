package com.increff.employee.scheduler;
import com.increff.employee.pojo.DailyReportPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.DailyReportService;
import com.increff.employee.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.transaction.Transactional;

@Service
public class DailySalesScheduler {
    @Autowired
    private DailyReportService dailyReportService;

    @Autowired
    private ReportService reportService;

    @Scheduled(cron = "*/2 * * * * *")
    @Transactional(rollbackOn  = ApiException.class)
    public void Scheduler() throws ApiException {
        LocalDateTime time = LocalDateTime.now().with(LocalTime.MIDNIGHT);

        if(dailyReportService.getLastId()==0) {
            DailyReportPojo dailyReportPojo = new DailyReportPojo();
            dailyReportPojo.setDate(time);
            dailyReportPojo.setInvoiced_orders_count(0);
            dailyReportPojo.setInvoiced_items_count(0);
            dailyReportPojo.setTotal_revenue(0.0);
            dailyReportService.add(dailyReportPojo);

            LocalDateTime nextDayDateTime = time.plusDays(1);
            DailyReportPojo dailyReportPojo1 = new DailyReportPojo();
            dailyReportPojo1.setDate(nextDayDateTime);
            dailyReportPojo1.setInvoiced_orders_count(0);
            dailyReportPojo1.setInvoiced_items_count(0);
            dailyReportPojo1.setTotal_revenue(0.0);
            dailyReportService.add(dailyReportPojo1);

            DailyReportPojo dailyReportPojo2 = new DailyReportPojo();
            dailyReportPojo2.setDate(time);
            dailyReportPojo2.setInvoiced_orders_count(0);
            dailyReportPojo2.setInvoiced_items_count(0);
            dailyReportPojo2.setTotal_revenue(0.0);
            dailyReportService.add(dailyReportPojo2);}
        else {  addInDb(dailyReportService.get(dailyReportService.getLastId()),time,1);
                createFieldInDb(dailyReportService.get(1).getDate(),time,1,2);}}

    @Transactional(rollbackOn  = ApiException.class)
    public void addInDb(DailyReportPojo dailyReportPojo1, LocalDateTime time,Integer id1){
        if(dailyReportPojo1.getDate().isEqual(time)){
            DailyReportPojo baseItem = dailyReportService.get(id1);
            Integer initialItems = dailyReportPojo1.getInvoiced_items_count();
            Integer initialOrders = dailyReportPojo1.getInvoiced_orders_count();
            Double initialRevenue = dailyReportPojo1.getTotal_revenue();
            dailyReportPojo1.setInvoiced_items_count(initialItems + baseItem.getInvoiced_items_count());
            dailyReportPojo1.setInvoiced_orders_count(initialOrders + baseItem.getInvoiced_orders_count());
            dailyReportPojo1.setTotal_revenue(initialRevenue + baseItem.getTotal_revenue());
            baseItem.setInvoiced_items_count(0);
            baseItem.setInvoiced_orders_count(0);
            baseItem.setTotal_revenue(0.0);}
        else { DailyReportPojo baseItem = dailyReportService.get(id1);
            DailyReportPojo dailyReportPojo = dailyReportService.get(dailyReportService.getLastId());
            Double lastRevenue = dailyReportPojo.getTotal_revenue();
            Integer lastOrders = dailyReportPojo.getInvoiced_orders_count();
            Integer lastItems = dailyReportPojo.getInvoiced_items_count();
            dailyReportPojo.setInvoiced_orders_count(baseItem.getInvoiced_orders_count() + lastOrders);
            dailyReportPojo.setInvoiced_items_count(baseItem.getInvoiced_items_count() + lastItems);
            dailyReportPojo.setTotal_revenue(baseItem.getTotal_revenue() + lastRevenue);
            baseItem.setInvoiced_items_count(0);
            baseItem.setInvoiced_orders_count(0);
            baseItem.setTotal_revenue(0.0);}
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void createFieldInDb(LocalDateTime date1, LocalDateTime time,Integer id1,Integer id2) throws ApiException {
        if(!date1.isEqual(time)) {
            DailyReportPojo baseItem2 = dailyReportService.get(id2);
            DailyReportPojo dailyReportPojo2 = new DailyReportPojo();
            dailyReportPojo2.setDate(time);
            dailyReportPojo2.setInvoiced_orders_count(baseItem2.getInvoiced_orders_count());
            dailyReportPojo2.setInvoiced_items_count(baseItem2.getInvoiced_items_count());
            dailyReportPojo2.setTotal_revenue(baseItem2.getTotal_revenue());
            dailyReportService.add(dailyReportPojo2);
            baseItem2.setInvoiced_items_count(0);
            baseItem2.setInvoiced_orders_count(0);
            baseItem2.setTotal_revenue(0.0);
            DailyReportPojo baseItem3 = dailyReportService.get(id1);
            baseItem3.setDate(time);
            LocalDateTime nextDayDateTime = time.plusDays(id1);
            baseItem2.setDate(nextDayDateTime);
        }
    }
}

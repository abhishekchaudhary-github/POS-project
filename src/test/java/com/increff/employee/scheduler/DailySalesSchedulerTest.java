package com.increff.employee.scheduler;

import com.increff.employee.model.DailyReportData;
import com.increff.employee.pojo.DailyReportPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.DailyReportService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;
public class DailySalesSchedulerTest extends AbstractUnitTest {
    @Autowired
    private DailyReportService dailyReportService;

    @Autowired
    private DailySalesScheduler dailySalesScheduler;


    @Test
    public void testEmptyDb() throws ApiException {
        dailySalesScheduler.Scheduler();
        assertNotNull(dailyReportService.get(1));
        assertNotNull(dailyReportService.get(2));
        assertNotNull(dailyReportService.get(3));
    }

    //test add on same day
    @Test
    public void testAddForSameDate() throws ApiException {
        LocalDateTime time = LocalDateTime.now().with(LocalTime.MIDNIGHT);

        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setDate(time);
        dailyReportPojo.setInvoiced_orders_count(2);
        dailyReportPojo.setInvoiced_items_count(5);
        dailyReportPojo.setTotal_revenue(101.0);
        Integer id2 = dailyReportService.add(dailyReportPojo);

        LocalDateTime nextDayDateTime = time.plusDays(1);
        DailyReportPojo dailyReportPojo1 = new DailyReportPojo();
        dailyReportPojo1.setDate(nextDayDateTime);
        dailyReportPojo1.setInvoiced_orders_count(0);
        dailyReportPojo1.setInvoiced_items_count(0);
        dailyReportPojo1.setTotal_revenue(0.0);
        Integer id3 = dailyReportService.add(dailyReportPojo1);

        DailyReportPojo dailyReportPojo2 = new DailyReportPojo();
        dailyReportPojo2.setDate(time);
        dailyReportPojo2.setInvoiced_orders_count(3);
        dailyReportPojo2.setInvoiced_items_count(4);
        dailyReportPojo2.setTotal_revenue(100.0);
        Integer id = dailyReportService.add(dailyReportPojo2);

        dailySalesScheduler.addInDb(dailyReportPojo2,time,id2);
        DailyReportPojo dailyReportPojo3 = dailyReportService.get(id);
        assertEquals(new Integer(5),dailyReportPojo3.getInvoiced_orders_count());
        assertEquals(new Integer(9),dailyReportPojo3.getInvoiced_items_count());
        assertEquals(new Double(201.0),dailyReportPojo3.getTotal_revenue());
    }

    //test add on next day
    @Test
    public void testAddForDifferentDate() throws ApiException {
        LocalDateTime time = LocalDateTime.now().with(LocalTime.MIDNIGHT);

        LocalDateTime previousDay = time.minusDays(1);
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setDate(previousDay);
        dailyReportPojo.setInvoiced_orders_count(2);
        dailyReportPojo.setInvoiced_items_count(5);
        dailyReportPojo.setTotal_revenue(101.0);
        Integer id11 = dailyReportService.add(dailyReportPojo);

        DailyReportPojo dailyReportPojo1 = new DailyReportPojo();
        dailyReportPojo1.setDate(time);
        dailyReportPojo1.setInvoiced_orders_count(2);
        dailyReportPojo1.setInvoiced_items_count(10);
        dailyReportPojo1.setTotal_revenue(305.7);
        Integer id3 = dailyReportService.add(dailyReportPojo1);


        DailyReportPojo dailyReportPojo2 = new DailyReportPojo();
        dailyReportPojo2.setDate(previousDay);
        dailyReportPojo2.setInvoiced_orders_count(3);
        dailyReportPojo2.setInvoiced_items_count(4);
        dailyReportPojo2.setTotal_revenue(100.0);
        Integer id = dailyReportService.add(dailyReportPojo2);

        dailySalesScheduler.createFieldInDb(dailyReportService.get(id11).getDate(),time,id11,id3);
        List<DailyReportPojo> dailyReportList = dailyReportService.getAll();
        Integer id22 = 900;
        for(int i=0;i<dailyReportList.size();i++){
            if(dailyReportList.get(i).getDate().isEqual(time)){
                id22 = dailyReportList.get(i).getId();
            }
        }

        assertEquals(2,dailyReportList.size());

        DailyReportPojo dailyReportPojo4 = dailyReportService.get(id22);
        assertEquals(new Integer(2),dailyReportPojo4.getInvoiced_orders_count());
        assertEquals(new Integer(10),dailyReportPojo4.getInvoiced_items_count());
        assertEquals(new Double(305.7),dailyReportPojo4.getTotal_revenue());

    }
}
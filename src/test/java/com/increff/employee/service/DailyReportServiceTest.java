package com.increff.employee.service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.increff.employee.model.DailyReportForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.DailyReportPojo;
import com.increff.employee.pojo.ProductPojo;
import helper.pojoHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DailyReportServiceTest  extends AbstractUnitTest {
    @Autowired
    private DailyReportService dailyReportService;

    private String convertToString(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
         return time.format(formatter);
    }

    //add
    @Test
    public void testAdd() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        DailyReportPojo dailyReportPojo = pojoHelper.makeDailyReportPojo(time,1,1,1.1);
        dailyReportService.add(dailyReportPojo);
        List<DailyReportPojo> dailyReportPojoList = dailyReportService.getAll();
        Integer id = dailyReportPojoList.get(0).getId();
        DailyReportPojo dailyReportPojo1 = dailyReportService.get(id);

        assertEquals(time,dailyReportPojo1.getDate());
        assertEquals(new Integer(1),dailyReportPojo1.getInvoiced_items_count());
        assertEquals(new Integer(1),dailyReportPojo1.getInvoiced_orders_count());
        assertEquals(new Double(1.1),dailyReportPojo1.getTotal_revenue());
    }

    //getall function
    @Test
    public void testGetAll() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        ArrayList<DailyReportPojo> dailyReportPojoList1 = new ArrayList<DailyReportPojo>();

        DailyReportPojo dailyReportPojo = pojoHelper.makeDailyReportPojo(time,1,1,1.1);
        dailyReportService.add(dailyReportPojo);
        dailyReportPojoList1.add(dailyReportPojo);
        DailyReportPojo dailyReportPojo2 = pojoHelper.makeDailyReportPojo(time,1,1,1.1);
        dailyReportService.add(dailyReportPojo2);
        dailyReportPojoList1.add(dailyReportPojo2);
        List<DailyReportPojo> dailyReportPojoList = dailyReportService.getAll();
        assertTrue(dailyReportPojoList1.size() == dailyReportPojoList.size() && dailyReportPojoList1.containsAll(dailyReportPojoList) && dailyReportPojoList.containsAll(dailyReportPojoList1));

    }

    //getall function with times setting an older time
    @Test
    public void testGetAllTime() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        ArrayList<DailyReportPojo> dailyReportPojoList1 = new ArrayList<DailyReportPojo>();

        LocalDateTime timeOlder = LocalDateTime.parse("2019-03-27T10:15:30");

        LocalDateTime startTime = LocalDateTime.parse("2021-03-27T10:15:30");
        LocalDateTime endTime = LocalDateTime.parse("2024-03-27T10:15:30");

        DailyReportPojo dailyReportPojo = pojoHelper.makeDailyReportPojo(timeOlder,1,1,1.1);
        dailyReportService.add(dailyReportPojo);

        DailyReportPojo dailyReportPojo2 = pojoHelper.makeDailyReportPojo(time,1,1,1.1);
        dailyReportService.add(dailyReportPojo2);
        dailyReportPojoList1.add(dailyReportPojo2);

        List<DailyReportPojo> dailyReportPojoList = dailyReportService.getAll(startTime,endTime);

        assertTrue(dailyReportPojoList1.size() == dailyReportPojoList.size() && dailyReportPojoList1.containsAll(dailyReportPojoList) && dailyReportPojoList.containsAll(dailyReportPojoList1));

    }
//test getAll for a form
@Test
public void testGetAllForm() throws ApiException {
    LocalDateTime time = LocalDateTime.now();

    LocalDateTime timeOlder = LocalDateTime.parse("2019-03-27T10:15:30");

    LocalDateTime startTime = LocalDateTime.parse("2021-03-27T10:15:30");
    LocalDateTime endTime = LocalDateTime.parse("2024-03-27T10:15:30");

    ArrayList<DailyReportPojo> dailyReportPojoList1 = new ArrayList<DailyReportPojo>();
    DailyReportPojo dailyReportPojo = pojoHelper.makeDailyReportPojo(timeOlder,1,1,1.1);
    DailyReportPojo dailyReportPojo2 = pojoHelper.makeDailyReportPojo(time,1,1,1.1);
    dailyReportService.add(dailyReportPojo2);
    dailyReportPojoList1.add(dailyReportPojo2);


    DailyReportForm dailyReportForm = new DailyReportForm();
    LocalDateTime now = LocalDateTime.now();

    String startTime1 = convertToString(startTime);
    String endTime1 = convertToString(endTime);

    dailyReportForm.setStartTime(startTime1);
    dailyReportForm.setEndTime(endTime1);

    List<DailyReportPojo> dailyReportPojoList = dailyReportService.getAll(dailyReportForm);

    assertTrue(dailyReportPojoList1.size() == dailyReportPojoList.size() && dailyReportPojoList1.containsAll(dailyReportPojoList) && dailyReportPojoList.containsAll(dailyReportPojoList1));

}

    //test get function ????

    //test get lastId
    @Test
    public void testGetLastId() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        DailyReportPojo dailyReportPojo = pojoHelper.makeDailyReportPojo(time, 1, 1, 1.1);
        dailyReportService.add(dailyReportPojo);
        DailyReportPojo dailyReportPojo1 = pojoHelper.makeDailyReportPojo(time, 2, 2, 1.2);
        dailyReportService.add(dailyReportPojo1);
        Integer lastId = dailyReportService.getLastId();
        DailyReportPojo dailyReportPojo2 = dailyReportService.get(lastId);

        assertEquals(time, dailyReportPojo1.getDate());
        assertEquals(new Integer(2), dailyReportPojo2.getInvoiced_items_count());
        assertEquals(new Integer(2), dailyReportPojo2.getInvoiced_orders_count());
        assertEquals(new Double(1.2), dailyReportPojo2.getTotal_revenue());
    }
}

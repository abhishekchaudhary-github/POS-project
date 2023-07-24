package com.increff.employee.dao;

import com.increff.employee.pojo.DailyReportPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import helper.pojoHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DailyReportDaoTest extends AbstractUnitTest {
    @Autowired
    DailyReportDao dao;

    @Test
    public void testInsert() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        DailyReportPojo dailyReportPojo = pojoHelper.makeDailyReportPojo(time,2,7,10000.5);
        dao.insert(dailyReportPojo);
        List<DailyReportPojo> dailyReportPojoList = dao.selectAll();
        assertEquals(new Integer(2),dailyReportPojoList.get(0).getInvoiced_orders_count());
        assertEquals(new Integer(7),dailyReportPojoList.get(0).getInvoiced_items_count());
        assertEquals(new Double(10000.5),dailyReportPojoList.get(0).getTotal_revenue());
    }

    @Test
    public void testSelect1() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        DailyReportPojo dailyReportPojo = pojoHelper.makeDailyReportPojo(time,2,7,10000.5);
        dao.insert(dailyReportPojo);

        LocalDateTime time1 = LocalDateTime.now();
        DailyReportPojo dailyReportPojo1 = pojoHelper.makeDailyReportPojo(time1,3,10,12000.5);
        dao.insert(dailyReportPojo1);
        List<DailyReportPojo> dailyReportPojoList = dao.selectAll();
        Integer id =dailyReportPojoList.get(0).getId();
        DailyReportPojo dailyReportPojo2 = dao.select(id);
        assertEquals(new Integer(2),dailyReportPojo2.getInvoiced_orders_count());
        assertEquals(new Integer(7),dailyReportPojo2.getInvoiced_items_count());
        assertEquals(new Double(10000.5),dailyReportPojo2.getTotal_revenue());
    }

    @Test
    public void testSelect2() throws ApiException {
        LocalDateTime time = LocalDateTime.parse("2019-03-27T10:15:30");
        DailyReportPojo dailyReportPojo = pojoHelper.makeDailyReportPojo(time,2,7,10000.5);
        dao.insert(dailyReportPojo);
        LocalDateTime time1 = LocalDateTime.now();
        DailyReportPojo dailyReportPojo1 = pojoHelper.makeDailyReportPojo(time1,3,10,12000.5);
        dao.insert(dailyReportPojo1);


        LocalDateTime timeOlder = LocalDateTime.parse("2020-03-27T10:15:30");
        LocalDateTime timeNew = LocalDateTime.parse("2024-03-27T10:15:30");

        List<DailyReportPojo> dailyReportPojoList = dao.select(timeOlder,timeNew);

        assertEquals(1,dailyReportPojoList.size());

        assertEquals(new Integer(3),dailyReportPojoList.get(0).getInvoiced_orders_count());
        assertEquals(new Integer(10),dailyReportPojoList.get(0).getInvoiced_items_count());
        assertEquals(new Double(12000.5),dailyReportPojoList.get(0).getTotal_revenue());
    }

    @Test
    public void testSelectAll() throws ApiException {
        LocalDateTime time = LocalDateTime.now();
        DailyReportPojo dailyReportPojo = pojoHelper.makeDailyReportPojo(time,2,7,10000.5);
        dao.insert(dailyReportPojo);
        DailyReportPojo dailyReportPojo1 = pojoHelper.makeDailyReportPojo(time,3,2,153000.5);
        dao.insert(dailyReportPojo1);
        List<DailyReportPojo> dailyReportPojoList = dao.selectAll();
        assertEquals(2,dailyReportPojoList.size());
        assertEquals(new Integer(2),dailyReportPojoList.get(0).getInvoiced_orders_count());
        assertEquals(new Integer(7),dailyReportPojoList.get(0).getInvoiced_items_count());
        assertEquals(new Double(10000.5),dailyReportPojoList.get(0).getTotal_revenue());
        assertEquals(new Integer(3),dailyReportPojoList.get(1).getInvoiced_orders_count());
        assertEquals(new Integer(2),dailyReportPojoList.get(1).getInvoiced_items_count());
        assertEquals(new Double(153000.5),dailyReportPojoList.get(1).getTotal_revenue());
    }

    @Test
    public void testSelectLast() throws ApiException {
        LocalDateTime time = LocalDateTime.parse("2019-03-27T10:15:30");
        DailyReportPojo dailyReportPojo = pojoHelper.makeDailyReportPojo(time,2,7,10000.5);
        dao.insert(dailyReportPojo);
        LocalDateTime time1 = LocalDateTime.now();
        DailyReportPojo dailyReportPojo1 = pojoHelper.makeDailyReportPojo(time1,3,10,12000.5);
        dao.insert(dailyReportPojo1);

        Integer lastId = dao.selectLastId();
        assertEquals(new Integer(2),lastId);
    }

}

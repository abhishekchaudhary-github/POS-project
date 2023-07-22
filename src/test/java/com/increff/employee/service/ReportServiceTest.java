package com.increff.employee.service;

import com.increff.employee.model.*;
import com.increff.employee.pojo.*;
import helper.formHelper;
import helper.pojoHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ReportServiceTest  extends AbstractUnitTest {

    @Autowired
    BrandService brandService;

    @Autowired
    ReportService reportService;

    @Autowired
    DailyReportService dailyReportService;

    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    OrderItemService orderItemService;

    private Integer brandId ;
    private Integer productId;
    private Integer productId2;

    private Integer inventoryId;
    private Integer inventoryId2;
    BrandPojo brandPojo;

    BrandPojo brandPojo1;

    private Integer orderId;

    private Integer orderId2;

    @Before
    public void preTest() throws ApiException {
        brandPojo = pojoHelper.makeBrandPojo("brand","category");
        brandService.add(brandPojo);
        BrandPojo brandPojo1 = brandService.getId("brand","category");
        brandId = brandPojo1.getId();
        brandPojo1 = pojoHelper.makeBrandPojo("brand2","category2");
        brandService.add(brandPojo1);
        BrandPojo brandPojo3 = brandService.getId("brand2","category2");
        Integer brandIdSecondPojo = brandPojo3.getId();
        ProductPojo productPojo = pojoHelper.makeProductPojo("barcode",brandId,1.12,"name");
        productId = productService.add(productPojo);
        ProductPojo productPojo1 = pojoHelper.makeProductPojo("barcode1",brandIdSecondPojo,1.10,"name1");
        productId2 = productService.add(productPojo1);
        InventoryPojo inventoryPojo = pojoHelper.makeInventoryPojo(productId,1000);
        inventoryId = inventoryService.add(inventoryPojo);
        InventoryPojo inventoryPojo2 = pojoHelper.makeInventoryPojo(productId2,2000);
        inventoryId2 = inventoryService.add(inventoryPojo2);
        orderId = orderItemService.addInOrder();
        orderId2 = orderItemService.addInOrder();
    }

    @Test
    public void testGetBrandReport() throws ApiException {
        BrandForm brandForm = formHelper.makeBrandForm("brand2","category2");

        List<BrandData> brandFormList1 = reportService.getBrandReport(brandForm);

        assertEquals(1,brandFormList1.size());
        assertEquals("brand2",brandFormList1.get(0).getBrand());
        assertEquals("category2",brandFormList1.get(0).getCategory());
    }

    //date????
    @Test
    public void testGetDailySalesReport() throws ApiException {

        postDailyform form1 = new postDailyform();
        String startTime = "2021-03-27";
        String endTime = "2024-03-27";
        form1.setStartTime(startTime);
        form1.setEndTime(endTime);
        LocalDateTime time = LocalDateTime.now();
        DailyReportPojo dailyReportPojo = pojoHelper.makeDailyReportPojo(time,1,1,1.1);
        dailyReportService.add(dailyReportPojo);
        List<DailyReportPojo> dailyReportPojoList = reportService.getDailySalesReport(form1);
        assertEquals(1,dailyReportPojoList.size());
        assertEquals(new Integer(1),dailyReportPojoList.get(0).getInvoiced_orders_count());
        assertEquals(new Integer(1),dailyReportPojoList.get(0).getInvoiced_items_count());
        assertEquals(new Double(1.1),dailyReportPojoList.get(0).getTotal_revenue());
        assertEquals(time,dailyReportPojoList.get(0).getDate());
    }

    @Test
    public void testGetInventoryReport() throws ApiException {
        BrandForm brandForm = formHelper.makeBrandForm("brand2","category2");



        List<InventoryReportData> inventoryReportDataList = reportService.getInventoryReport(brandForm);

        assertEquals(1,inventoryReportDataList.size());
        assertEquals("brand2",inventoryReportDataList.get(0).getBrand());
        assertEquals("category2",inventoryReportDataList.get(0).getCategory());
        assertEquals(new Integer(2000),inventoryReportDataList.get(0).getQuantity());
    }


    //GOOD TRICK
    @Test
    public void testGetOrdersByTime() throws ApiException {

        orderItemService.addInOrder();

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.parse("2024-03-27T10:15:30");

        Integer orderId3 = orderItemService.addInOrder();


        List<OrderPojo> orderPojoList = reportService.getOrdersByTime(startTime,endTime);

        assertEquals(1,orderPojoList.size());
        assertEquals(orderId3,orderPojoList.get(0).getId());
    }

    @Test
    public void testGetOrdersItemByOrderId() throws ApiException {


        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId,productId,500,1.1);
        OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId,productId2,500,1.0);
        List<OrderItemPojo> OrderItemPojoList = new ArrayList<>();
        OrderItemPojoList.add(orderItemPojo);
        OrderItemPojoList.add(orderItemPojo1);

        orderItemService.add(OrderItemPojoList);


        List<OrderItemPojo> orderItemPojoList1 = reportService.getOrderItemByOrderId(orderId);

        assertEquals(2,orderItemPojoList1.size());
        assertEquals(orderId,orderItemPojoList1.get(0).getOrderId());
        assertEquals(orderId,orderItemPojoList1.get(1).getOrderId());
        assertEquals(productId,orderItemPojoList1.get(0).getProductId());
        assertEquals(productId2,orderItemPojoList1.get(1).getProductId());
        assertEquals(new Integer(500),orderItemPojoList1.get(0).getQuantity());
        assertEquals(new Integer(500),orderItemPojoList1.get(1).getQuantity());
        assertEquals(new Double(1.1),orderItemPojoList1.get(0).getSellingPrice());
        assertEquals(new Double(1.0),orderItemPojoList1.get(1).getSellingPrice());

    }

    //test getall()
    @Test
    public void testGetAll() throws ApiException {
        String startTime = "2021-03-27";
        String endTime = "2024-03-27";

        Integer orderId3 = orderItemService.addInOrder();
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId3,productId2,500,0.9);
        List<OrderItemPojo> OrderItemPojoList = new ArrayList<>();
        OrderItemPojoList.add(orderItemPojo);
        orderItemService.add(OrderItemPojoList);

        List<SalesReportData> salesReportDataList = reportService.getAll(startTime,endTime,"","");
        assertEquals(1,salesReportDataList.size());
        assertEquals(new Integer(500),salesReportDataList.get(0).getQuantity());
        assertEquals("brand2",salesReportDataList.get(0).getBrand());
        assertEquals("category2",salesReportDataList.get(0).getCategory());
    }

    //more than one product same brand category
    @Test
    public void testGetAllMoreBrandCategoryProducts() throws ApiException {
        String startTime = "2021-03-27";
        String endTime = "2024-03-27";

        Integer orderId3 = orderItemService.addInOrder();
        OrderItemPojo orderItemPojo1 = pojoHelper.makeOrderItemPojo(orderId2,productId2,500,0.9);
        OrderItemPojo orderItemPojo = pojoHelper.makeOrderItemPojo(orderId3,productId2,500,0.9);
        List<OrderItemPojo> OrderItemPojoList = new ArrayList<>();
        OrderItemPojoList.add(orderItemPojo);
        OrderItemPojoList.add(orderItemPojo1);
        orderItemService.add(OrderItemPojoList);

        List<SalesReportData> salesReportDataList = reportService.getAll(startTime,endTime,"","");
        assertEquals(1,salesReportDataList.size());
        assertEquals(new Integer(1000),salesReportDataList.get(0).getQuantity());
        assertEquals("brand2",salesReportDataList.get(0).getBrand());
        assertEquals("category2",salesReportDataList.get(0).getCategory());
    }

}

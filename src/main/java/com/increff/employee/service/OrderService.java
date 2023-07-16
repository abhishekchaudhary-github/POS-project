//77 100 101
package com.increff.employee.service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.model.InvoiceData;
import com.increff.employee.model.OrderItem;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.DailyReportPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.transaction.Transactional;

@Service
public class OrderService {

    private int orderItemCount;

    private Double initialRevenue;
    @Value("${invoice.url}")
    private String url;

    @Autowired
    private OrderDao dao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemDataList;

    @Autowired
    private ProductService productService;

    @Autowired
    private DailyReportService dailyReportService;

    @Transactional(rollbackOn = ApiException.class)
    public Integer add() throws ApiException {
        LocalDateTime timeOfOrder = timeOfOrderCreation();
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setTime(timeOfOrder);
        Integer orderId = dao.insert(orderPojo);
        return orderId;
    }


    @Transactional
    public void delete(Integer id) {
        dao.delete(id);
    }

    @Transactional
    public List<OrderPojo> getAll() {
        return dao.selectAll();
    }

    protected static LocalDateTime timeOfOrderCreation() {
        LocalDateTime dateTime = LocalDateTime.now();
//        SimpleDateFormat createdTime = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//        Date currentDate = new Date();
//        String formattedDateTime = createdTime.format(currentDate);
        return dateTime;
    }

//    public Integer getLastOrderId() {
//         List<OrderPojo> orderPojoList = dao.selectAll();
//         if(orderPojoList.size()==0) {
//             return 0;
//         }
//         int k=1;
//         for(int i=0;i<orderPojoList.size();i++) {
//             if(orderPojoList.get(i).getId()>k) {
//                 k = orderPojoList.get(i).getId();
//             }
//         }
//         return k;
//    }

    public OrderPojo getOrderById(Integer id) {
        return dao.orderById(id);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public ResponseEntity<byte[]> getInvoicePDF(Integer id) throws Exception {
        orderItemCount=0;
        initialRevenue=0.0;
        InvoiceData invoiceData = generateInvoiceForOrder(id);
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("before sending");
        byte[] contents = Base64.getDecoder().decode(restTemplate.postForEntity(url, invoiceData, byte[].class).getBody());
        System.out.println("after sending");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "invoice.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
//        Integer lastId = dailyReportService.getLastId();
//        DailyReportPojo dailyReportFirstPojo = dailyReportService.get(1);

        //dailyReportPojo.setTotal_invoice(dailyReportPojo.getTotal_invoice()+1);
        //Integer initialTotalInvoicedItems = dailyReportPojo.getTotal_invoiced_items();
        //dailyReportPojo.setTotal_invoiced_items(initialTotalInvoicedItems+orderItemCount);
        //uneditable
        OrderPojo orderPojo = getOrderById(id);
        //
        if(orderPojo.isEditable()==true) {
            DailyReportPojo dailyReportFirstPojo = dailyReportService.get(1);
            if(dailyReportFirstPojo==null) {
                throw new ApiException("database has been altered");
            }
            Integer countOfInvoices = dailyReportFirstPojo.getInvoiced_orders_count();
            Integer countOfOrders = dailyReportFirstPojo.getInvoiced_items_count();
            Double totalRevenue = dailyReportFirstPojo.getTotal_revenue();
            System.out.println(countOfInvoices);
            countOfInvoices+=1;
            countOfOrders+=orderItemCount;
            totalRevenue+=initialRevenue;
            dailyReportFirstPojo.setInvoiced_orders_count(countOfInvoices);
            dailyReportFirstPojo.setInvoiced_items_count(countOfOrders);
            dailyReportFirstPojo.setTotal_revenue(totalRevenue);
        }
        //
        orderPojo.setEditable();
        return response;
    }

    @Transactional(rollbackOn  = ApiException.class)
    public InvoiceData generateInvoiceForOrder(Integer orderId) throws ApiException
    {
        InvoiceData invoiceData = new InvoiceData();
        OrderPojo orderPojo = dao.orderById(orderId);
        invoiceData.setOrderId(orderPojo.getId());
        invoiceData.setPlacedDate(orderPojo.getTime().toString());
        List<OrderItemPojo> orderItemPojoList = orderItemDataList.selectByOrderId(orderPojo.getId());

        List<OrderItem> orderItemList = new ArrayList<>();
        for(OrderItemPojo p: orderItemPojoList) {
            orderItemCount ++;
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(p.getId());
            String productName = productService.getCheck(p.getProductId()).getName();
            orderItem.setProductName(productName);
            orderItem.setQuantity(p.getQuantity());
            orderItem.setSellingPrice(p.getSellingPrice());
            orderItemList.add(orderItem);
            //
            initialRevenue+=p.getSellingPrice()*p.getQuantity();
            //
        }
        invoiceData.setOrderItemDataList(orderItemList);
        return invoiceData;
    }
}
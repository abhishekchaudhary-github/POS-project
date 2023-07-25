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
import java.time.LocalTime;
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

        //dailyReportPojo.setTotal_invoice(dailyReportPojo.getTotal_invoice()+1);
        //Integer initialTotalInvoicedItems = dailyReportPojo.getTotal_invoiced_items();
        //dailyReportPojo.setTotal_invoiced_items(initialTotalInvoicedItems+orderItemCount);
        //uneditable
        OrderPojo orderPojo = getOrderById(id);
        //
        if(orderPojo.isEditable()==true) {
//8            DailyReportPojo dailyReportFirstPojo = dailyReportService.get(1);
//8            if(dailyReportFirstPojo==null) {
//8                throw new ApiException("database has been altered");
//8            }


            LocalDateTime timeChecker = LocalDateTime.now().with(LocalTime.MIDNIGHT);


            Integer countOfInvoices1=0;
            Integer countOfOrders1=0;
            Double totalRevenue1=0.0;
            DailyReportPojo dailyReportFirstPojo1 = dailyReportService.get(1);
            if(!timeChecker.isEqual(dailyReportFirstPojo1.getDate())){
                dailyReportFirstPojo1 = dailyReportService.get(2);
            }

            countOfInvoices1 = dailyReportFirstPojo1.getInvoiced_orders_count();
            countOfOrders1 = dailyReportFirstPojo1.getInvoiced_items_count();
            totalRevenue1 = dailyReportFirstPojo1.getTotal_revenue();
//           System.out.println(countOfInvoices);
            countOfInvoices1+=1;
            countOfOrders1+=orderItemCount;
            totalRevenue1+=initialRevenue;
            dailyReportFirstPojo1.setInvoiced_orders_count(countOfInvoices1);
            dailyReportFirstPojo1.setInvoiced_items_count(countOfOrders1);
            dailyReportFirstPojo1.setTotal_revenue(totalRevenue1);
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
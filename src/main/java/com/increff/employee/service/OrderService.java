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

    public OrderPojo getOrderById(Integer id) {
        return dao.orderById(id);
    }

    @Transactional
    public ResponseEntity<byte[]> getInvoicePDF(Integer id) throws Exception {
        orderItemCount=0;
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
        Integer lastId = dailyReportService.getLastId();
        DailyReportPojo dailyReportPojo = dailyReportService.get(lastId);
        dailyReportPojo.setTotal_invoice(dailyReportPojo.getTotal_invoice()+1);
        Integer initialTotalInvoicedItems = dailyReportPojo.getTotal_invoiced_items();
        dailyReportPojo.setTotal_invoiced_items(initialTotalInvoicedItems+orderItemCount);
        //uneditable
        OrderPojo orderPojo = getOrderById(id);
        orderPojo.setEditable();
        return response;
    }

    @Transactional
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
        }
        invoiceData.setOrderItemDataList(orderItemList);
        return invoiceData;
    }
}
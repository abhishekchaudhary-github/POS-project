package com.increff.employee.service;
import com.increff.employee.dao.EmployeeDao;
import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.dao.SalesReportDao;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SalesReportService {

    //
    @Autowired
    OrderService orderService;

    //
    @Autowired
    private SalesReportDao dao;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Transactional
    public List<SalesReportData> getAll(String startTime, String endTime, String brand, String category) throws ApiException {
        LocalDateTime startTime1 = convertTime(startTime);
        LocalDateTime endTime1 = convertTime(endTime);
        List<OrderPojo> orderPojoList = dao.selectAllOrder(startTime1,endTime1);
        List<OrderItemPojo> orderItemPojo = new ArrayList<OrderItemPojo>();
        for(int i = 0 ; i < orderPojoList.size(); i++) {
            List<OrderItemPojo> tempOrderItemList = dao.selectOrderItem(orderPojoList.get(i).getId());
            for(OrderItemPojo tempOrderItem : tempOrderItemList ){
      //          System.out.println(tempOrderItem.getQuantity());
            orderItemPojo.add(tempOrderItem);
            }
        }
        //System.out.println(orderItemPojo);
        List<SalesReportData> salesReportDataList = new ArrayList<SalesReportData>();
        HashMap<String,SalesReportData> hm = new HashMap<String,SalesReportData>();
        for( OrderItemPojo orderItem : orderItemPojo ) {
            SalesReportData salesReportData = new SalesReportData();
            ProductPojo productPojo = productService.get(orderItem.getProductId());
            BrandPojo brandPojo = brandService.get(productPojo.getBrand_category());

            if(brand.equals(brandPojo.getBrand()) && category.equals(brandPojo.getCategory())){
                String key = brand + category;
                if(!hm.containsKey(key)){
                    salesReportData.setBrand(brand);
                    salesReportData.setCategory(category);
                    salesReportData.setRevenue(orderItem.getSellingPrice());
                    salesReportData.setQuantity(orderItem.getQuantity());
                    hm.put(key,salesReportData);
                }
                else {
                    salesReportData.setBrand(brand);
                    salesReportData.setCategory(category);
                    salesReportData.setRevenue(orderItem.getSellingPrice());
                   Integer quantitySum = hm.get(key).getQuantity();
                    quantitySum += orderItem.getQuantity();
                    salesReportData.setQuantity(quantitySum);
                    hm.put(key,salesReportData);
                }
            }



        }
        //loop on hashmap and setting values in list
        for (String id : hm.keySet()){
            hm.get(id).setRevenue(hm.get(id).getRevenue()*hm.get(id).getQuantity());
            salesReportDataList.add(hm.get(id));
        }
        return salesReportDataList;
    }

    private LocalDateTime convertTime(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parse the string to obtain a LocalDate object
        LocalDate date = LocalDate.parse(dateString, formatter);

        // Define the specific time
        int hour = 00;
        int minute = 00;
        int second = 00;

        // Create a LocalDateTime object with the date and specific time
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, minute, second));
        return dateTime;
    }
}

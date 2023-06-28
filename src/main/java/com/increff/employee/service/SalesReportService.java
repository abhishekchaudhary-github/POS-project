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
import java.util.List;

@Service
public class SalesReportService {
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
            //CAUTION!!!
            List<OrderItemPojo> tempOrderItemList = dao.selectOrderItem(orderPojoList.get(i).getId());
            for(OrderItemPojo tempOrderItem : tempOrderItemList ){
            orderItemPojo.add(tempOrderItem);
            }
        }
        List<SalesReportData> salesReportDataList = new ArrayList<SalesReportData>();
        for( OrderItemPojo orderItem : orderItemPojo ) {
            SalesReportData salesReportData = new SalesReportData();
            ProductPojo productPojo = productService.get(orderItem.getProductId());
            BrandPojo brandPojo = brandService.get(productPojo.getBrand_category());

            if(brand.equals(brandPojo.getBrand()) && category.equals(brandPojo.getCategory())){
            salesReportData.setBrand(brand);
            salesReportData.setCategory(category);
            salesReportData.setQuantity(orderItem.getQuantity());
            salesReportData.setRevenue(orderItem.getQuantity()*orderItem.getSellingPrice());
            }
        }
        return salesReportDataList;
    }

    private LocalDateTime convertTime(String initialTime){
        //convert time string to time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        // Parse the string to obtain a LocalDate object
        LocalDate date = LocalDate.parse(initialTime, formatter);
        // Decide on a specific time to combine with the date
        LocalTime time = LocalTime.of(0, 0); // Example: 00:00
        // Combine the date and time to create a LocalDateTime object
        LocalDateTime ConvertedTime = LocalDateTime.of(date, time);
        return ConvertedTime;
    }
}

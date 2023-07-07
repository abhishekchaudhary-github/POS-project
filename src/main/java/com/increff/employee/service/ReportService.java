package com.increff.employee.service;
import com.increff.employee.dao.ReportDao;
import com.increff.employee.model.*;
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
public class ReportService {

    //
    @Autowired
    OrderService orderService;

    //
    @Autowired
    private ReportDao dao;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private DailyReportService dailyReportService;


    @Transactional
    public List<BrandData> getBrandReport(BrandForm form) throws ApiException {
        String brand = form.getBrand();
        if(brand==null) brand="";
        String category = form.getCategory();
        if(category==null) category="";
        List<BrandPojo> brandPojoList = brandService.getAll();
        List<BrandData> brandDataList = new ArrayList<BrandData>();
        for (BrandPojo brandPojo : brandPojoList) {
            if (brandPojo.getBrand().contains(brand) && brandPojo.getCategory().contains(category)) {
                BrandData brandData = new BrandData();
                brandData.setCategory(brandPojo.getCategory());
                brandData.setBrand(brandPojo.getBrand());
                brandDataList.add(brandData);
            }
        }
        return brandDataList;
    }

    @Transactional
    public List<DailyReportPojo> getDailySalesReport(DailyReportForm form) throws ApiException {
        LocalDateTime startTime1 = convertTime(form.getStartTime());
        LocalDateTime endTime1 = convertTime(form.getEndTime());
        List<DailyReportPojo> dailyReportPojoList = dailyReportService.getAll(startTime1, endTime1);
        return dailyReportPojoList;
    }
    @Transactional
    public List<InventoryReportData> getInventoryReport(BrandForm form) throws ApiException {
        String brand = form.getBrand();
        if(brand==null) brand="";
        String category = form.getCategory();
        if(category==null) category="";
        List<ProductPojo> productPojoList = productService.getAll();
        List<BrandPojo> brandPojoList = brandService.getAll();
        List<InventoryReportData> inventoryDataList = new ArrayList<InventoryReportData>();
        for (BrandPojo brandPojo : brandPojoList) {
            if (brandPojo.getBrand().contains(brand) && brandPojo.getCategory().contains(category)) {
                InventoryReportData inventoryReportData = new InventoryReportData();
                inventoryReportData.setCategory(brandPojo.getCategory());
                inventoryReportData.setBrand(brandPojo.getBrand());
                Integer brandId = brandPojo.getId();
                //call quantity
                Integer quantity = 0 ;
                for(ProductPojo productPojo : productPojoList ) {
                    if (productPojo.getBrand_category() == brandId) {
                        InventoryPojo inventoryPojo = inventoryService.getFromProductId(productPojo.getId());
                        if(inventoryPojo!=null)
                        quantity += inventoryPojo.getQuantity();
                    }
                }
                //why productservice.brandCategory not working !!!!!
//                System.out.println(productService.brandCategory(brandPojo.getId()));
//                InventoryPojo inventoryPojo = inventoryService.getFromProductId(productPojo.getId());
                inventoryReportData.setQuantity(quantity);
                inventoryDataList.add(inventoryReportData);
            }
        }
        return inventoryDataList;
    }

    @Transactional
    public List<OrderPojo> getOrdersByTime(LocalDateTime startTime, LocalDateTime endTime) {
        return dao.selectAllOrder(startTime,endTime);
    }

    @Transactional
    public List<OrderItemPojo> getOrderItemByOrderId(Integer id){
        return dao.selectOrderItem(id);
    }

    @Transactional
    public List<SalesReportData> getAll(String startTime, String endTime, String brand, String category) throws ApiException {
        LocalDateTime startTime1 = convertTime(startTime);
        LocalDateTime endTime1 = convertTime(endTime);
        List<OrderPojo> orderPojoList = dao.selectAllOrder(startTime1, endTime1);
        List<OrderItemPojo> orderItemPojo = new ArrayList<OrderItemPojo>();
        for (int i = 0; i < orderPojoList.size(); i++) {
            List<OrderItemPojo> tempOrderItemList = dao.selectOrderItem(orderPojoList.get(i).getId());
            for (OrderItemPojo tempOrderItem : tempOrderItemList) {
                orderItemPojo.add(tempOrderItem);
            }
        }

        List<SalesReportData> salesReportDataList = new ArrayList<SalesReportData>();
        HashMap<Integer, SalesReportData> hm = new HashMap<Integer, SalesReportData>();

        for (OrderItemPojo orderItem : orderItemPojo) {
            SalesReportData salesReportData = new SalesReportData();
            ProductPojo productPojo = productService.get(orderItem.getProductId());
            BrandPojo brandPojo = brandService.get(productPojo.getBrand_category());

            //"" not needed
            if(brand==null) brand="";
            if(category==null) category="";

            if (brandPojo.getBrand().contains(brand) && brandPojo.getCategory().contains(category)) {
                Integer key = brandPojo.getId();
                AddInHashmap(hm, salesReportData, key, brand, category, orderItem, brandPojo);
            }


        }

//        //loop on hashmap and setting values in list
        for (Integer id : hm.keySet()) {
            hm.get(id).setRevenue(hm.get(id).getRevenue() * hm.get(id).getQuantity());
            salesReportDataList.add(hm.get(id));
        }
            return salesReportDataList;

    }

    private void AddInHashmap(HashMap<Integer,SalesReportData> hm,SalesReportData salesReportData,Integer key,String brand,String category,OrderItemPojo orderItem,BrandPojo brandPojo) {
        if (!hm.containsKey(key)) {

                        salesReportData.setBrand(brandPojo.getBrand());
                        salesReportData.setCategory(brandPojo.getCategory());
                        salesReportData.setCategory(brandPojo.getCategory());
                        salesReportData.setRevenue(orderItem.getSellingPrice());
                        salesReportData.setQuantity(orderItem.getQuantity());
                        hm.put(key, salesReportData);
                    } else {
                        salesReportData.setBrand(brandPojo.getBrand());
                        salesReportData.setCategory(brandPojo.getCategory());
                        salesReportData.setRevenue(orderItem.getSellingPrice());
                        Integer quantitySum = hm.get(key).getQuantity();
                        quantitySum += orderItem.getQuantity();
                        salesReportData.setQuantity(quantitySum);
                        hm.put(key, salesReportData);
                   }
    }
    private LocalDateTime convertTime(String dateString){
        //careful here
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

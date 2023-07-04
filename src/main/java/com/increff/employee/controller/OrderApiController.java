package com.increff.employee.controller;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.increff.employee.model.CategoryDetailForm;
import com.increff.employee.model.OrderData;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class OrderApiController {
    //dto
    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    //
    @Autowired
    private OrderService service;

//    @ApiOperation(value = "Adds a Order")
//    @RequestMapping(path = "/api/orderitem", method = RequestMethod.POST)
//    public void add(@RequestBody List<CategoryDetailForm> form) throws ApiException {
//        List<OrderPojo> p = new ArrayList<OrderPojo>();
//        Integer orderPojoId = orderService.add();
//        for(CategoryDetailForm categoryDetailItem : form)
//            p.add(convert(categoryDetailItem,orderPojoId));
//        service.add(p);
//    }


    @ApiOperation(value = "Gets list of all employees")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OrderData> getAll() {
        List<OrderPojo> list = service.getAll();
        List<OrderData> list2 = new ArrayList<OrderData>();
        for (OrderPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

//    @ApiOperation(value = "Gets a Order by ID")
//    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
//    public OrderData get(@PathVariable Integer id) throws ApiException {
//        OrderPojo p = service.get(id);
//        return convert(p);
//    }
//
//    @ApiOperation(value = "Check for items")
//    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.POST)
//    public void get(@RequestBody CategoryDetailForm form) throws ApiException {
//        service.check(form);
//    }
//
//    @ApiOperation(value = "Updates a Order")
//    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.PUT)
//    public void update(@PathVariable Integer id, @RequestBody OrderForm f) throws ApiException {
//        OrderPojo p = convert(f);
//        service.update(id, p);
//    }


    private static OrderData convert(OrderPojo p) {
        OrderData d = new OrderData();
        //convert date to string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = p.getTime().format(formatter);
        d.setTime(formattedDateTime);
        d.setId(p.getId());
        return d;
    }

//    @ApiOperation(value = "Download Invoice")
//    @GetMapping(path = "/invoice/{id}")
//    public ResponseEntity<byte[]> getInvoicePDF(@PathVariable Integer id) throws Exception{
//        return dto.getInvoicePDF(id);
//    }

//    private OrderPojo convert(CategoryDetailForm f ,Integer orderPojoId) throws ApiException {
//        OrderPojo p = new OrderPojo();
//        ProductPojo productPojo = productService.barcodeMustExist(f.getBarcode());
//        if(productPojo!=null) {
//            if(productPojo.getMrp()<f.getMrp())
//                throw new ApiException("Selling price cannot be greater than mrp");
//            InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());
////            Integer orderPojoId = orderService.add();
//            p.setOrderId(orderPojoId);
//            p.setProductId(productPojo.getId());
//            p.setQuantity(f.getQuantity());
//            p.setSellingPrice(f.getMrp());
//            return p;
//        }
//
//        else throw new ApiException("Barcode does not exist");
//    }

}

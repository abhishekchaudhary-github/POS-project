package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.employee.model.CategoryDetailForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderItemPojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderItemApiController {
    //dto
    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    //
    @Autowired
    private OrderItemService service;

    @ApiOperation(value = "Adds a OrderItem")
    @RequestMapping(path = "/api/orderitem", method = RequestMethod.POST)
    public void add(@RequestBody List<CategoryDetailForm> form) throws ApiException {
        List<OrderItemPojo> p = new ArrayList<OrderItemPojo>();
        for(CategoryDetailForm categoryDetailItem : form)
            p.add(convert(categoryDetailItem));
        service.add(p);
    }

//
//    @ApiOperation(value = "Gets a OrderItem by ID")
//    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
//    public OrderItemData get(@PathVariable Integer id) throws ApiException {
//        OrderItemPojo p = service.get(id);
//        return convert(p);
//    }
//
//    @ApiOperation(value = "Gets list of all OrderItems")
//    @RequestMapping(path = "/api/orderitem", method = RequestMethod.GET)
//    public List<OrderItemData> getAll() {
//        List<OrderItemPojo> list = service.getAll();
//        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
//        for (OrderItemPojo p : list) {
//            list2.add(convert(p));
//        }
//        return list2;
//    }
//
//    @ApiOperation(value = "Updates a OrderItem")
//    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.PUT)
//    public void update(@PathVariable Integer id, @RequestBody OrderItemForm f) throws ApiException {
//        OrderItemPojo p = convert(f);
//        service.update(id, p);
//    }


    private static OrderItemData convert(OrderItemPojo p) {
        OrderItemData d = new OrderItemData();
        d.setOrderId(p.getOrderId());
        d.setProductId(p.getProductId());
        d.setQuantity(p.getQuantity());
        d.setSellingPrice(p.getSellingPrice());
        d.setId(p.getId());
        return d;
    }

    private OrderItemPojo convert(CategoryDetailForm f) throws ApiException {
        OrderItemPojo p = new OrderItemPojo();
        ProductPojo productPojo = productService.barcodeMustExist(f.getBarcode());
        if(productPojo!=null) {
            if(productPojo.getMrp()<f.getMrp())
                throw new ApiException("Selling price cannot be greater than mrp");
            InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());
            Integer orderPojoId = orderService.add();
            p.setOrderId(orderPojoId);
            p.setProductId(productPojo.getId());
            p.setQuantity(f.getQuantity());
            p.setSellingPrice(f.getMrp());
            return p;
        }

        else throw new ApiException("Barcode does not exist");
    }

}

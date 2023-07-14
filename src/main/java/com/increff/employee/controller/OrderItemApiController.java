package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.employee.model.*;
import com.increff.employee.pojo.*;
import com.increff.employee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.transaction.Transactional;

@Api
@RestController
public class OrderItemApiController {
    //
    @Autowired
    private OrderItemService service;

    @ApiOperation(value = "Adds a OrderItem")
    @RequestMapping(path = "/api/orderitem", method = RequestMethod.POST)
    public void add(@RequestBody List<CategoryDetailForm> form) throws ApiException {
        List<OrderItemPojo> p = new ArrayList<OrderItemPojo>();
        Integer orderPojoId = service.getOrderId()+1;
        for(CategoryDetailForm categoryDetailItem : form)
            p.add(convert(categoryDetailItem,orderPojoId));
        service.addInOrder();
        service.add(p);
    }


    @ApiOperation(value = "Deletes an Order so give orderId")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
    public void deleteOrder(@PathVariable Integer id) {
        service.deleteOrder(id);
    }

    @ApiOperation(value = "Deletes an orderitems so give id")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id) throws ApiException {
        service.delete(id);
    }


    @ApiOperation(value = "Gets list of all employees")
    @RequestMapping(path = "/api/orderitem", method = RequestMethod.GET)
    public List<OrderItemData> getAll() throws ApiException {
        List<OrderItemPojo> list = service.getAll();
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for (OrderItemPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    @ApiOperation(value = "Gets a OrderItem by ID")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
    public OrderItemData get(@PathVariable Integer id) throws ApiException {
        OrderItemPojo p = service.get(id);
        return convert(p);
    }
//
//    @ApiOperation(value = "Check for items")
//    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.POST)
//    public void get(@RequestBody CategoryDetailForm form) throws ApiException {
//        service.check(form);
//    }
//
    @ApiOperation(value = "Updates a OrderItem")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody OrderItemEditForm f) throws ApiException {
        OrderItemPojo p = convertEdit(f);
        service.editOrder(p);
    }


    private OrderItemData convert(OrderItemPojo p) throws ApiException {
        String barcode = service.getFromProductBarcode(p);
        String name = service.getFromProductName(p);
        OrderItemData d = new OrderItemData();
        d.setOrderId(p.getOrderId());
        d.setProductId(p.getProductId());
        d.setQuantity(p.getQuantity());
        d.setSellingPrice(p.getSellingPrice());
        d.setBarcode(barcode);
        d.setName(name);
        d.setId(p.getId());
        return d;
    }

    private OrderItemPojo convert(CategoryDetailForm f ,Integer orderPojoId) throws ApiException {
        OrderItemPojo p = new OrderItemPojo();
        service.productBarcodeMustExist(f);
        Integer productId = service.getInventoryIdOfProduct(f.getBarcode());
        service.getInventoryFromProductId(productId,f.getQuantity());
            p.setOrderId(orderPojoId);
            p.setProductId(productId);
            p.setQuantity(f.getQuantity());
            p.setSellingPrice(f.getMrp());
            return p;
    }

    private OrderItemPojo convertEdit(OrderItemEditForm f) throws ApiException {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        service.getInventoryFromProductId(f,f.getId());
        orderItemPojo.setQuantity(f.getQuantity());
        orderItemPojo.setSellingPrice(f.getSelling_price());
        return orderItemPojo;
    }

}

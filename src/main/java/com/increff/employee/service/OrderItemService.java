package com.increff.employee.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.employee.model.CategoryDetailForm;
import com.increff.employee.model.OrderItemEditForm;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.util.StringUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
public class OrderItemService {

    //dto
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    //

    @Autowired
    private OrderItemDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(List<OrderItemPojo> p) throws ApiException {
        for(OrderItemPojo orderItemPojoItem : p){
            //HERE!!!
            inventoryService.deductQuantity(orderItemPojoItem.getProductId(),orderItemPojoItem.getQuantity());
        }
        for(OrderItemPojo orderItemPojoItem : p){
            dao.insert(orderItemPojoItem);
        }

    }
    @Transactional(rollbackOn = ApiException.class)
    public Integer addInOrder() throws ApiException {
        return orderService.add();
    }



    @Transactional(rollbackOn = ApiException.class)
    public void deleteOrder(Integer orderId) throws ApiException {
        deleteOrderJust(orderId);
        List<OrderItemPojo> orderItemPojoList = dao.selectAll();
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        HashMap<Integer, InventoryPojo> hm = new HashMap<Integer, InventoryPojo>();
        for (int i = 0; i < inventoryPojoList.size(); i++) {
            hm.put(inventoryPojoList.get(i).getProductId(), inventoryPojoList.get(i));
        }

        for (int i = 0; i < orderItemPojoList.size(); i++) {
            if (orderItemPojoList.get(i).getOrderId() == orderId) {
                InventoryPojo inventoryPojo = hm.get(orderItemPojoList.get(i).getProductId());
                Integer quantity = inventoryPojo.getQuantity();
                quantity += orderItemPojoList.get(i).getQuantity();
                inventoryPojo.setQuantity(quantity);
                deleteOrderItemJust(orderItemPojoList.get(i).getId());
                //dao.delete(orderItemPojoList.get(i).getId());
            }
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void delete(Integer id) throws ApiException {
        OrderItemPojo orderItemPojo = dao.select(id);
        OrderPojo orderPojo = orderService.getOrderById(orderItemPojo.getOrderId());
        if(orderPojo.isEditable()==true) {
            List<OrderItemPojo> orderItemPojoList = dao.selectAll();
            int count = 0;
            for (int i = 0; i < orderItemPojoList.size(); i++) {
                if (orderItemPojoList.get(i).getOrderId() == orderItemPojo.getOrderId()) {
                    count++;
                }
            }
            //psuedo inventory pojo
            InventoryPojo inventoryPojo = inventoryService.getFromProductId(orderItemPojo.getProductId());
            Integer quantity = inventoryPojo.getQuantity();
            quantity += orderItemPojo.getQuantity();
            inventoryPojo.setQuantity(quantity);
            orderPojo.setTime(LocalDateTime.now());
            if (count < 2) {
                //orderService.delete(orderItemPojo.getOrderId());
                deleteOrder(orderItemPojo.getOrderId());
            }
            else{
            dao.delete(id);}
        }
        else {
            throw new ApiException("this order is not editable");
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void deleteOrderJust(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderService.getOrderById(orderId);
        if(orderPojo.isEditable()==true) {
            orderService.delete(orderId);
        }
        else {
            throw new ApiException("this order is not editable");
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void deleteOrderItemJust(Integer id) {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void editOrder(Integer id, OrderItemPojo orderItemPojo1) throws ApiException {
        OrderPojo orderPojo = orderService.getOrderById(orderItemPojo1.getOrderId());
        //OrderItemPojo orderItemPojo = dao.select(orderItemPojo1.getProductId());
        if(orderPojo.isEditable()==true){
            getInventoryFromProductId(orderItemPojo1,id);
            OrderItemPojo orderItemPojo = getCheck(id);
            orderItemPojo.setQuantity(orderItemPojo1.getQuantity());
            orderItemPojo.setSellingPrice(orderItemPojo1.getSellingPrice());
            orderPojo.setTime(LocalDateTime.now());
            if(orderItemPojo1.getQuantity()==0) {
                delete(orderItemPojo1.getId());
            }
        }
        else throw new ApiException("this order is not editable");
    }


    @Transactional(rollbackOn = ApiException.class)
    public OrderItemPojo get(Integer id) throws ApiException {
        return dao.select(id);
    }
//
    @Transactional
    public List<OrderItemPojo> getAll() {
        return dao.selectAll();
    }

//    @Transactional
//    public Integer getOrderId() {
//        return orderService.getLastOrderId();
//    }

    @Transactional
    public List<OrderItemPojo> selectByOrderId(Integer id) {
        List<OrderItemPojo> orderItemPojoList = dao.selectAll();
        List<OrderItemPojo> orderItemPojoList2 = new ArrayList<OrderItemPojo>();
        for(int i = 0; i< orderItemPojoList.size(); i++) {
             if(orderItemPojoList.get(i).getOrderId()==id){
                 orderItemPojoList2.add(orderItemPojoList.get(i));
             }
         }
        return orderItemPojoList2;
    }
    @Transactional
    public void productBarcodeMustExist(CategoryDetailForm categoryDetailForm) throws ApiException {
        ProductPojo productPojo = productService.barcodeMustExist(categoryDetailForm.getBarcode());
        if(productPojo==null){
            throw new ApiException("this barcode does not exists");
        }
        if(productPojo.getMrp()<categoryDetailForm.getMrp())
                throw new ApiException("selling price cannot be greater than mrp");
    }

    @Transactional
    public void getInventoryFromProductId(Integer productId,Integer quantity) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.getFromProductId(productId);
        if(inventoryPojo==null) {
            throw new ApiException("this product is not present in the inventory");
        }
        if(quantity>inventoryPojo.getQuantity()){
            throw new ApiException("quantity of item more than the quantity in inventory");
        }
        if(quantity<=1) {
            throw new ApiException("quantity must be at least 1");
        }
        //check for it
//        if( quantity % 1 != 0) {
//            throw new ApiException("quantity can't be fractional");
//        }

    }

    @Transactional(rollbackOn = ApiException.class)
    public void getInventoryFromProductId(OrderItemPojo orderItemEditPojo, Integer id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.getFromProductId(orderItemEditPojo.getProductId());
        if(inventoryPojo==null) {
            throw new ApiException("this product is not present in the inventory");
        }
        OrderItemPojo orderItemPojo = dao.select(id);
        if(orderItemEditPojo.getQuantity()>inventoryPojo.getQuantity()+orderItemPojo.getQuantity()){
            throw new ApiException("quantity of item more than the quantity in inventory");
        }
        ProductPojo productPojo = productService.get(inventoryPojo.getProductId());
        if(orderItemEditPojo.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("selling price of item can not be more than MRP");
        }
        inventoryPojo.setQuantity((inventoryPojo.getQuantity()+orderItemPojo.getQuantity()-orderItemEditPojo.getQuantity()));
    }

    @Transactional
    public Integer getInventoryIdOfProduct(String barcode) throws ApiException {
        String barcode1 = normalize(barcode);
        return inventoryService.getIdOfProduct(barcode1);
    }

    @Transactional
    public String getFromProductBarcode(OrderItemPojo orderItemPojo) throws ApiException {
        ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
        return productPojo.getBarcode();
    }

    @Transactional
    public String getFromProductName(OrderItemPojo orderItemPojo) throws ApiException {
        ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
        return productPojo.getName();
    }
//    @Transactional(rollbackOn  = ApiException.class)
//    public void update(Integer id, OrderItemPojo p) throws ApiException {
//        if(StringUtil.isEmpty(p.getOrderItem())) {
//            throw new ApiException("orderItem cannot be empty");
//        }
//        if(StringUtil.isEmpty(p.getCategory())) {
//            throw new ApiException("category cannot be empty");
//        }
//        normalize(p);
//        OrderItemPojo tempPojo = getCheck(id);
//        if(dao.select(id).getOrderItem().equals(p.getOrderItem()) && dao.select(id).getCategory().equals(p.getCategory())){
//            return;
//        }
//        if(dao.checkerForDuplicate(p.getOrderItem(), p.getCategory())==null){
//            tempPojo.setOrderItem(p.getOrderItem());
//            tempPojo.setCategory(p.getCategory());}
//        else throw new ApiException("category already exists");
//    }
//
//    @Transactional
//    public OrderItemPojo getCheck(Integer id) throws ApiException {
//        OrderItemPojo p = dao.select(id);
//        if (p == null) {
//            throw new ApiException("Employee with given ID does not exit, id: " + id);
//        }
//        return p;
//    }
//
    protected static String normalize(String barcode) {
        return StringUtil.toLowerCase(barcode);
    }

    @Transactional
    public OrderItemPojo getCheck(Integer id) throws ApiException {
        OrderItemPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Order with given ID does not exit, id: " + id);
        }
        return p;
    }


}
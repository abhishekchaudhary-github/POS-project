package com.increff.employee.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.employee.model.CategoryDetailForm;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.OrderItemPojo;
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



    @Transactional
    public void deleteOrder(Integer orderId) {
        orderService.delete(orderId);
        List<OrderItemPojo> orderItemPojoList = dao.selectAll();
        System.out.println(orderItemPojoList);
        for(int i=0;i<orderItemPojoList.size();i++) {
            if(orderItemPojoList.get(i).getOrderId()==orderId)
                dao.delete(orderItemPojoList.get(i).getId());
        }
    }

    @Transactional
    public void delete(Integer id) {
        OrderItemPojo orderItemPojo = dao.select(id);
        List<OrderItemPojo> orderItemPojoList = dao.selectAll();
        int count=0;
        for(int i=0;i<orderItemPojoList.size();i++){
            if(orderItemPojoList.get(i).getOrderId()==orderItemPojo.getOrderId()) {
                count ++;
            }
        }
        if(count<2) {
            orderService.delete(orderItemPojo.getOrderId());
        }
        dao.delete(id);
    }

    @Transactional
    public void editOrder(OrderItemPojo orderItemPojo1) throws ApiException {
        OrderPojo orderPojo = orderService.getOrderById(orderItemPojo1.getOrderId());
        OrderItemPojo orderItemPojo = dao.select(orderItemPojo1.getProductId());
        if(orderPojo.isEditable()==true){
            orderItemPojo.setQuantity(orderItemPojo1.getQuantity());
            orderItemPojo.setSellingPrice(orderItemPojo1.getSellingPrice());
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
        if(productPojo.getMrp()<categoryDetailForm.getMrp())
                throw new ApiException("Selling price cannot be greater than mrp");
    }

    @Transactional
    public void getInventoryFromProductId(Integer productId) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.getFromProductId(productId);
        if(inventoryPojo==null) {
            throw new ApiException("this product is not present in the inventory");
        }
    }

    @Transactional
    public Integer getInventoryIdOfProduct(String barcode) throws ApiException {
        return inventoryService.getIdOfProduct(barcode);
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
//    protected static void normalize(OrderItemPojo p) {
//        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
//    }

}
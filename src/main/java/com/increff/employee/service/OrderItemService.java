package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.util.StringUtil;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(List<OrderItemPojo> p) throws ApiException {

      dao.insert(p);

    }


//    @Transactional(rollbackOn = ApiException.class)
//    public OrderItemPojo get(Integer id) throws ApiException {
//        return getCheck(id);
//    }
//
//    @Transactional
//    public List<OrderItemPojo> getAll() {
//        return dao.selectAll();
//    }
//
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
//        p.setOrderItem(StringUtil.toLowerCase(p.getOrderItem()));
//        p.setCategory(StringUtil.toLowerCase(p.getCategory()));
//    }

}
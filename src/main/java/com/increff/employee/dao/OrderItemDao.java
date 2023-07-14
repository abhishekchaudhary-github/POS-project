package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.employee.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao {

    private static String select_id = "select p from OrderItemPojo p where id=:id";
    private static String select_all = "select p from OrderItemPojo p";
    private static String nocommon_case = "select p from OrderItemPojo p where orderItem=:orderItem and category=:category";
    private static String delete_id = "delete from OrderItemPojo p where p.id=:id";

    private static String select_order_id = "select p from OrderItemPojo p where orderId=:orderId";

    @PersistenceContext
    private EntityManager em;


    public void delete(Integer id) {
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
    }

    @Transactional
    public void insert(OrderItemPojo p) {
        em.persist(p);
    }

    public OrderItemPojo select(Integer id) {
        TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }
//
    @Transactional
    public List<OrderItemPojo> selectFromId(Integer id) {
        TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
    public List<OrderItemPojo> selectAll() {
        TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
        return query.getResultList();
    }
//
//    public OrderItemPojo checkerForDuplicate(String orderItem, String category){
//        TypedQuery<OrderItemPojo> query = getQuery(nocommon_case, OrderItemPojo.class);
//
//        query.setParameter("orderItem", orderItem);
//        query.setParameter("category", category);
//        return getSingle(query);
//    }
//    public OrderItemPojo orderItemMustExist(Integer productId){
//        TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
//        query.setParameter("id", productId);
//        return getSingle(query);
//    }
//
//    public void update(OrderItemPojo p) {
//    }



}

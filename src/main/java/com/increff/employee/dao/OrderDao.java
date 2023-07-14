package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {

    private static String delete_id = "delete from OrderPojo p where id=:id";
    private static String select_id = "select p from OrderPojo p where id=:id";
    private static String select_all = "select p from OrderPojo p";

    private static String last_id = "select count(*) from OrderPojo p";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Integer insert(OrderPojo orderPojo) {
        em.persist(orderPojo);
        return orderPojo.getId();
    }

    public Integer delete(Integer id) {
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public Integer getLastOrderId() {
        Query query = em.createQuery(last_id);
        Long count = (Long) query.getSingleResult(); // Execute the query and retrieve the count value
        return count.intValue();
    }

    public List<OrderPojo> selectAll() {
        TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
        return query.getResultList();
    }

    public OrderPojo orderById(Integer id) {
        TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }
}

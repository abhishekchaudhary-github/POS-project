package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.OrderPojo;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class OrderDao extends AbstractDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Integer insert(OrderPojo orderPojo) {
        em.persist(orderPojo);
        return orderPojo.getId();
    }
}

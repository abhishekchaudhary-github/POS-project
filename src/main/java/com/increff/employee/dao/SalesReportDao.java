package com.increff.employee.dao;

import com.increff.employee.pojo.EmployeePojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SalesReportDao  extends AbstractDao {
    private static String select_id_order = "select p from OrderPojo p where time>=:startTime and time<=:endTime";

    private static String select_id_order_item = "select p from OrderItemPojo p where orderId=:orderId";
    @PersistenceContext
    private EntityManager em;

    public List<OrderPojo> selectAllOrder(LocalDateTime startTime, LocalDateTime endTime) {
        TypedQuery<OrderPojo> query = getQuery(select_id_order, OrderPojo.class);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        return query.getResultList();
    }

    public List<OrderItemPojo> selectOrderItem(Integer orderId){
        TypedQuery<OrderItemPojo> query = getQuery(select_id_order_item, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

}

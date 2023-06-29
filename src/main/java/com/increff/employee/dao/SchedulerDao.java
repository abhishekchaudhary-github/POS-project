package com.increff.employee.dao;

import com.increff.employee.pojo.SchedulerPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class SchedulerDao extends AbstractDao{
    private static String select_all = "select p from SchedulerPojo p";
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(SchedulerPojo p) {
        em.persist(p);
    }

    public List<SchedulerPojo> selectAll() {
        TypedQuery<SchedulerPojo> query = getQuery(select_all, SchedulerPojo.class);
        return query.getResultList();
    }

}

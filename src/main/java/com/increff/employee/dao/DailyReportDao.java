package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.DailyReportPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DailyReportDao extends AbstractDao{
    private static String select_all = "select p from DailyReportPojo p";

    private static String select_time = "select p from DailyReportPojo p where date>:startTime and date<:endTime";

    private static String count_of_table = "select count(*) from DailyReportPojo p";

    private static String select_id = "select p from DailyReportPojo p where id=:id";
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Integer insert(DailyReportPojo p) {
        em.persist(p);
        return p.getId();
    }

    public List<DailyReportPojo> selectAll() {
        TypedQuery<DailyReportPojo> query = getQuery(select_all, DailyReportPojo.class);
        return query.getResultList();
    }
    public List<DailyReportPojo> select(LocalDateTime startTime, LocalDateTime endTime) {
        TypedQuery<DailyReportPojo> query = getQuery(select_time, DailyReportPojo.class);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        return query.getResultList();
    }

    public DailyReportPojo select(Integer id) {
        TypedQuery<DailyReportPojo> query = getQuery(select_id, DailyReportPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public Integer selectLastId() {
        TypedQuery<Long> query = getQuery(count_of_table, Long.class);
        Long count = query.getSingleResult();
        return count.intValue();
    }
}

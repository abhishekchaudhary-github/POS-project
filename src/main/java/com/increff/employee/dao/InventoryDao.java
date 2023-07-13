package com.increff.employee.dao;

import com.increff.employee.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class InventoryDao  extends AbstractDao {
    private static String select_id = "select p from InventoryPojo p where id=:id";
    private static String select_all = "select p from InventoryPojo p";

    private static String get_quantity_from_productId = "select p from InventoryPojo p where productId=:productId";


    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(InventoryPojo p) {
        em.persist(p);
    }

    @Transactional
    public InventoryPojo getFromProductId(Integer productId) {
        TypedQuery<InventoryPojo> query = getQuery(get_quantity_from_productId, InventoryPojo.class);
        query.setParameter("productId", productId);
        return getSingle(query);
    }

    public InventoryPojo getIdFromBarcode(Integer productId) {
        TypedQuery<InventoryPojo> query = getQuery(get_quantity_from_productId, InventoryPojo.class);
        query.setParameter("productId", productId);
        return getSingle(query);
    }

    public InventoryPojo select(Integer id) {
        TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = getQuery(select_all, InventoryPojo.class);
        return query.getResultList();
    }
    public void update(InventoryPojo p) {
    }
}

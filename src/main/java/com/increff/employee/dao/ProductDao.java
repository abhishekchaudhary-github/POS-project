package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.employee.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.ProductPojo;


@Repository
public class ProductDao extends AbstractDao{
    private static String select_id = "select p from ProductPojo p where id=:id";
    private static String select_all = "select p from ProductPojo p";

    private static String select_barcode = "select p from ProductPojo p where barcode=:barcode";
    private static String nocommon_case = "select p from ProductPojo p where brand_category=:brand_category and name=:name";

//    private static  String barcode_and_mrp = "select p from ProductPojo p where brand_category=:brand_category and mrp=:mrp";
    private static String brand_category_select = "select p from ProductPojo p where brand_category=:brand_category";
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Integer insert(ProductPojo p) {
        em.persist(p);
        return p.getId();
    }

//    public ProductPojo barcodeAndMrp(String barcode, Double mrp) {
//        TypedQuery<ProductPojo> query = getQuery(barcode_and_mrp, ProductPojo.class);
//        query.setParameter("barcode", barcode);
//        query.setParameter("mrp", mrp);
//        return getSingle(query);
//    }

    public ProductPojo barcodeMustExist(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(select_barcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

    public List<ProductPojo> barcodeMustExistList(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(select_barcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return query.getResultList();
    }



    public List<ProductPojo> brandCategory(Integer brand_category) {
        TypedQuery<ProductPojo> query = getQuery(brand_category_select, ProductPojo.class);
        query.setParameter("brand_category", brand_category);
        return query.getResultList();
    }

    public ProductPojo checkerForDuplicate(Integer brand_category, String name){
        TypedQuery<ProductPojo> query = getQuery(nocommon_case, ProductPojo.class);

        query.setParameter("brand_category", brand_category);
        query.setParameter("name", name);
        return getSingle(query);
    }
    public ProductPojo select(Integer id) {
        TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }
    public ProductPojo selectBarcode(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(select_barcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }


    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
        return query.getResultList();
    }



    public void update(ProductPojo p) {
    }

}

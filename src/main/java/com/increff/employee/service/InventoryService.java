package com.increff.employee.service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao dao;



    @Transactional(rollbackOn = ApiException.class)
    public void add(Integer productId) throws ApiException {
        InventoryPojo p = new InventoryPojo();
        p.setId(productId);
        p.setQuantity(0);
        dao.insert(p);
    }


    @Transactional(rollbackOn = ApiException.class)
    public void deductQuantity(Integer productId, Integer quantity){
        InventoryPojo productToBeChanged = dao.select(productId);
        Integer previousQuantity = productToBeChanged.getQuantity();
        productToBeChanged.setQuantity(previousQuantity - quantity);
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo get(Integer id) throws ApiException {
        return dao.select(id);
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, InventoryPojo p) throws ApiException {
        if(p.getQuantity()==null) {
            throw new ApiException("field cannot be empty");
        }
        if(p.getQuantity()<0){
            throw new ApiException("quantity cannot be negative");
        }

        //InventoryPojo tempPojo = getCheck(id);
            InventoryPojo temp = get(id);
            temp.setQuantity(p.getQuantity());
    }

//    @Transactional
//    public InventoryPojo getCheck(Integer id) throws ApiException {
//        InventoryPojo p = dao.select(id);
//        if (p == null) {
//            throw new ApiException("Employee with given ID does not exit, id: " + id);
//        }
//        return p;
//    }


}

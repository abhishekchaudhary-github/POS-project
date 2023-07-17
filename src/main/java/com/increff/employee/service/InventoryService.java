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

    @Autowired
    private ProductService productService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo inventoryPojo) throws ApiException {
        if(dao.select(inventoryPojo.getProductId())==null) {
            if(inventoryPojo.getQuantity()<1){
                throw new ApiException("quantity can not be less than 1");
            }
            dao.insert(inventoryPojo);
        }
        else {
            Integer previousQuantity = dao.select(inventoryPojo.getProductId()).getQuantity();
            Integer newQuantity = inventoryPojo.getQuantity()+previousQuantity;
            inventoryPojo.setQuantity(newQuantity);
            dao.select(inventoryPojo.getProductId()).setQuantity(newQuantity);
        }
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

    @Transactional
    public Integer getIdOfProduct(String barcode) throws ApiException {
        return productService.getProductId(barcode);
    }
    public String getForBarcode(Integer productId) throws ApiException {
        return productService.get(productId).getBarcode();
    }

    @Transactional
    public InventoryPojo getFromProductId(Integer productId) throws ApiException {
        return dao.getFromProductId(productId);
    }

//    public Integer getIdFromBarcode(String barcode) throws ApiException {
//        Integer productId = productService.getProductId(barcode);
 //       InventoryPojo inventoryPojo = dao.getIdFromBarcode(productId);
//        if(inventoryPojo==null) return -1;
//        else return inventoryPojo.getId();
//    }


    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, InventoryPojo p) throws ApiException {
        if(p.getQuantity()==null) {
            throw new ApiException("field cannot be empty");
        }
        if(p.getQuantity()<1){
            throw new ApiException("quantity cannot be less than 1");
        }
        //check if given product id exist
        if(dao.getFromProductId(p.getProductId())==null){

        }
        else if(!dao.getFromProductId(p.getProductId()).equals(dao.select(id)) && dao.getFromProductId(p.getProductId())!=null) {
            throw new ApiException("given barcode already exists");
        }
        //InventoryPojo tempPojo = getCheck(id);
            InventoryPojo temp = get(id);
            temp.setQuantity(p.getQuantity());
            temp.setProductId(p.getProductId());
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

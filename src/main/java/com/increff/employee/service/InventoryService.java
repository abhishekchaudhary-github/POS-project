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
    public Integer add(InventoryPojo inventoryPojo) throws ApiException {
        if(dao.select(inventoryPojo.getProductId())==null) {
            if(inventoryPojo.getQuantity()<1){
                throw new ApiException("quantity can not be less than 1");
            }
            return dao.insert(inventoryPojo);
        }
        else {
            if(inventoryPojo.getQuantity()<1){
                throw new ApiException("quantity can not be less than 1");
            }
            Integer previousQuantity = dao.select(inventoryPojo.getProductId()).getQuantity();
            Integer newQuantity = inventoryPojo.getQuantity()+previousQuantity;
            inventoryPojo.setQuantity(newQuantity);
            InventoryPojo inventoryPojo1 = dao.select(inventoryPojo.getProductId());
            Integer inventoryId = inventoryPojo1.getId();
            inventoryPojo1.setQuantity(newQuantity);
            return inventoryId;
        }
    }


    @Transactional(rollbackOn = ApiException.class)
    public void deductQuantity(Integer inventoryId, Integer quantity){
        InventoryPojo productToBeChanged = dao.select(inventoryId);
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
    public Integer update(Integer id, InventoryPojo p) throws ApiException {
        if(p.getQuantity()==null) {
            throw new ApiException("quantity of given product cannot be empty");
        }
        if(p.getQuantity()<1){
            throw new ApiException("quantity of given product cannot be less than 1");
        }
        //check if given product id exist in inventory
        if(dao.getFromProductId(p.getProductId())==null){

        }
        else if(!dao.getFromProductId(p.getProductId()).equals(dao.select(id)) && dao.getFromProductId(p.getProductId())!=null) {
            throw new ApiException("given barcode already exists");
        }
        InventoryPojo tempPojo = getCheck(id);
        String s1 = dao.select(id).getQuantity()+"";
        String s2 = p.getQuantity()+"";
        String s3 = dao.select(id).getQuantity()+"";
        String s4 = p.getQuantity()+"";
        if( s1.equals(s2) && s3.equals(s4) ) {
            return 0;
        }
        tempPojo.setQuantity(p.getQuantity());
        tempPojo.setProductId(p.getProductId());
            return 1;
    }

    @Transactional
    public InventoryPojo getCheck(Integer id) throws ApiException {
        InventoryPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Inventory with given ID does not exit, id: " + id);
        }
        return p;
    }


}

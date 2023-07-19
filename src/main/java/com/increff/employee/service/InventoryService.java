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

    @Autowired
    private InventoryService inventoryService;

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
            InventoryPojo inventoryPojo1 = dao.select(inventoryPojo.getProductId());
            inventoryPojo1.setQuantity(newQuantity);
            return inventoryPojo1.getId();
        }
    }


    @Transactional(rollbackOn = ApiException.class)
    public void deductQuantity(Integer productId, Integer quantity) throws ApiException {
        //MAJOR CHANGE HERE
        InventoryPojo productToBeChanged = dao.getFromProductId(productId);
        Integer previousQuantity = productToBeChanged.getQuantity();
        if(previousQuantity - quantity<0){
            throw new ApiException("quantity is more than what is present in the inventory");
        }
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
            throw new ApiException("inventory of this productId does  not exist");
        }
        else if(!dao.getFromProductId(p.getProductId()).equals(dao.select(id)) && dao.getFromProductId(p.getProductId())!=null) {
            throw new ApiException("inventory can not be updated because of alter in productId");
        }
        InventoryPojo tempPojo = getCheck(id);
        String s1 = dao.select(id).getQuantity()+"";
        String s2 = p.getQuantity()+"";
//        String s3 = dao.select(id).getQuantity()+"";
//        String s4 = p.getQuantity()+"";
        if( s1.equals(s2) ) {
            return 0;
        }
        tempPojo.setQuantity(p.getQuantity());
//        tempPojo.setProductId(p.getProductId());
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

package com.increff.employee.controller;

import com.increff.employee.model.*;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class InventoryApiController {
    @Autowired
    private InventoryService service;
    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Adds an Inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
    public void add(@RequestBody InventoryForm form) throws ApiException {
        Integer productId = service.getIdOfProduct(form.getBarcode());
        InventoryPojo p = convert(form,productId);
        service.add(p);
    }

    @ApiOperation(value = "tsv upload")
    @RequestMapping(path = "/api/inventory/tsv", method = RequestMethod.POST)
    public List<ErrorsInventory> addBulk(@RequestBody List<InventoryFormString> form) throws ApiException {
        ArrayList<ErrorsInventory> data = service.fileCheck(form);
        System.out.println(data.get(data.size()-1).isCheckError());
        if(data.get(data.size()-1).isCheckError()==false)
            for(InventoryFormString InventoryItem : form) {
                Integer productId = service.getIdOfProduct(InventoryItem.getBarcode());
                InventoryForm inventoryForm = convertToForm(InventoryItem);
                service.add(convert(inventoryForm, productId));
            }
        return data;
    }


    @ApiOperation(value = "Gets an Inventory by ID")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable Integer id) throws ApiException {
        InventoryPojo p = service.get(id);
        String barcode = service.getForBarcode(p.getProductId());
        return convert(p,barcode);
    }

//    @ApiOperation(value = "Gets an Inventory by barcode")
//    @RequestMapping(path = "/api/inventory/barcode/{barcode}", method = RequestMethod.GET)
//    public Integer getInventoryFromBarcode(@PathVariable String barcode) throws ApiException {
//        return service.getIdFromBarcode(barcode);
//    }

    @ApiOperation(value = "Gets list of all Inventories")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> list = service.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (InventoryPojo p : list) {
            String barcode = service.getForBarcode(p.getProductId());
            list2.add(convert(p,barcode));
        }
        return list2;
    }

    @ApiOperation(value = "Updates an Inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
    public Integer update(@PathVariable Integer id, @RequestBody InventoryForm form) throws ApiException {
        Integer productId = service.getIdOfProduct(form.getBarcode());
        InventoryPojo p = convert(form,productId);
        return service.update(id, p);
    }


    private static InventoryData convert(InventoryPojo p,String barcode) throws ApiException {
        InventoryData d = new InventoryData();
        d.setQuantity(p.getQuantity());
        //d.setBarcode(service.getForBarcode(p.getProductId()));
        d.setBarcode(barcode);
        d.setId(p.getId());
        return d;
    }

    private static InventoryPojo convert(InventoryForm f,Integer productId) throws ApiException {
        InventoryPojo p = new InventoryPojo();
        p.setQuantity(f.getQuantity());
        //Integer productId = service.getIdOfProduct(f.getBarcode());
        p.setProductId(productId);
        return p;
    }

    private static InventoryForm convertToForm(InventoryFormString form){
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setQuantity(Integer.parseInt(form.getQuantity()));
        inventoryForm.setBarcode(form.getBarcode());
        return inventoryForm;
    }

}

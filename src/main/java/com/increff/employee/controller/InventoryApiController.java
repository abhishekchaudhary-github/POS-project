package com.increff.employee.controller;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
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

//    @ApiOperation(value = "Adds an Inventory")
//    @RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
//    public void add(@RequestBody InventoryForm form) throws ApiException {
//        InventoryPojo p = convert(form);
//        service.add(p);
//    }


    @ApiOperation(value = "Gets an Inventory by ID")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable int id) throws ApiException {
        InventoryPojo p = service.get(id);
        return convert(p);
    }

    @ApiOperation(value = "Gets list of all Inventories")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() {
        List<InventoryPojo> list = service.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (InventoryPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    @ApiOperation(value = "Updates an Inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
        InventoryPojo p = convert(f);
        service.update(id, p);
    }


    private static InventoryData convert(InventoryPojo p) {
        InventoryData d = new InventoryData();
        d.setQuantity(p.getQuantity());
        d.setId(p.getId());
        return d;
    }

    private static InventoryPojo convert(InventoryForm f) {
        InventoryPojo p = new InventoryPojo();
        p.setQuantity(f.getQuantity());
        p.setId(f.getId());
        return p;
    }

}

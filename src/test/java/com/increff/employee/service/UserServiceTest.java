package com.increff.employee.service;

import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.UserPojo;
import helper.pojoHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserServiceTest  extends AbstractUnitTest {
    @Autowired
    UserService userService;

    @Test
    public void testAdd() throws ApiException {
        UserPojo userPojo = pojoHelper.makeUserPojo("abc","supervisor","a@com");
        userService.add(userPojo);
        UserPojo userPojo1 = userService.get("a@com");
        assertEquals("abc",userPojo1.getPassword());
        assertEquals("supervisor",userPojo1.getRole());
        assertEquals("a@com",userPojo1.getEmail());
    }

    @Test
    public void testAddUserExists(){
        try{

            UserPojo userPojo = pojoHelper.makeUserPojo("abc","supervisor","a@com");
            userService.add(userPojo);
            UserPojo userPojo1 = pojoHelper.makeUserPojo("abc","supervisor","a@com");
            userService.add(userPojo1);
        }
        catch(ApiException err){
            assertEquals("user with given email already exists",err.getMessage());
        }
    }


    @Test
    public void testGet() throws ApiException {
            UserPojo userPojo = pojoHelper.makeUserPojo("abc","supervisor","a@com");
            userService.add(userPojo);
            UserPojo userPojo1 = pojoHelper.makeUserPojo("abc","supervisor","a1@com");
            userService.add(userPojo1);
            userService.get("a@com");

            assertEquals("abc",userPojo.getPassword());
            assertEquals("supervisor",userPojo.getRole());
            assertEquals("a@com",userPojo.getEmail());
    }

    @Test
    public void testGetById() throws ApiException {
        UserPojo userPojo1 = pojoHelper.makeUserPojo("abc","supervisor","a@com");
        userService.add(userPojo1);
        List<UserPojo> userServiceList =userService.getAll();
        UserPojo userPojo = userService.getById(userServiceList.get(0).getId());
        assertEquals("abc",userPojo.getPassword());
        assertEquals("supervisor",userPojo.getRole());
        assertEquals("a@com",userPojo.getEmail());
    }

    @Test
    public void testGetAll() throws ApiException {
        List<UserPojo> userPojoList1 = new ArrayList<>();
        UserPojo userPojo = pojoHelper.makeUserPojo("abc","supervisor","a@com");
        userService.add(userPojo);
        userPojoList1.add(userPojo);
        UserPojo userPojo1 = pojoHelper.makeUserPojo("abc","supervisor","a1@com");
        userService.add(userPojo1);
        userPojoList1.add(userPojo1);

        List<UserPojo> userPojoList = userService.getAll();

        assertTrue(userPojoList1.size() == userPojoList.size() && userPojoList1.containsAll(userPojoList) && userPojoList.containsAll(userPojoList1));
    }

    @Test
    public void testDelete() throws ApiException {
        List<UserPojo> userPojoList1 = new ArrayList<>();
        UserPojo userPojo = pojoHelper.makeUserPojo("abc","supervisor","a@com");
        userService.add(userPojo);
        userPojoList1.add(userPojo);
        UserPojo userPojo1 = pojoHelper.makeUserPojo("abc","supervisor","a1@com");
        userService.add(userPojo1);

        List<UserPojo> userPojoList = userService.getAll();

        userService.delete(userPojoList.get(1).getId());

        assertTrue(userPojoList1.size() == userPojoList.size() && userPojoList1.containsAll(userPojoList) && userPojoList.containsAll(userPojoList1));
    }

    @Test
    public void normalize() {
        UserPojo userPojo = pojoHelper.makeUserPojo("abc","supervisoR","A@com");
        userService.normalize(userPojo);
        assertEquals("supervisor",userPojo.getRole());
        assertEquals("a@com",userPojo.getEmail());
    }
}

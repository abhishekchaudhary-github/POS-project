package com.increff.employee.controller;

import com.increff.employee.model.InfoData;
import com.increff.employee.model.LoginForm;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SignupController {
    @Autowired
    private UserService service;
    @Autowired
    private InfoData info;
    @ApiOperation(value = "signups a user")
    @RequestMapping(path = "/session/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView signup(HttpServletRequest req, LoginForm f) throws ApiException {
        UserPojo userPojo = convert(f);
        service.add(userPojo);
        return new ModelAndView("redirect:/site/login");
    }

    private UserPojo convert(LoginForm loginForm) throws ApiException {
        UserPojo userPojo = new UserPojo();
        userPojo.setPassword(loginForm.getPassword());
        userPojo.setEmail(loginForm.getEmail());
        if(service.getById(1)==null)
        userPojo.setRole("admin");
        else
            userPojo.setRole("standard");
        return userPojo;
    }
}

//package com.increff.employee.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.increff.employee.model.CategoryDetailForm;
//import com.increff.employee.pojo.OrderItemPojo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.increff.employee.model.EmployeeData;
//import com.increff.employee.model.EmployeeForm;
//import com.increff.employee.pojo.EmployeePojo;
//import com.increff.employee.service.ApiException;
////import com.increff.employee.service.EmployeeService;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//
//@Api
//@RestController
//public class EmployeeApiController {
//
//	@Autowired
//	private EmployeeService service;
//
//	@ApiOperation(value = "Adds an employee")
//	@RequestMapping(path = "/api/employee", method = RequestMethod.POST)
//	public void add(@RequestBody EmployeeForm form) throws ApiException {
//		EmployeePojo p = convert(form);
//		service.add(p);
//	}
//
//
////	@ApiOperation(value = "Adds a OrderItem")
////	@RequestMapping(path = "/api/orderitem", method = RequestMethod.POST)
////	public ArrayList<Errors> add(@RequestBody List<EmployeeForm> formlist) throws ApiException {
////		boolean checkError =false;
////		ArrayList<Errors> data = new ArrayList<Errors>();
////		for(EmployeeForm form1 : formlist) {
////			//			check(form1);
////			Errors erros = new Errors();
////			erros.setMessage("no error");
////			data.add(erros);
////		}
////		if(checkError==false){
////		for(EmployeeForm form : formlist) {
////			EmployeePojo p1 = convert(form);
////			service.add(p1);
////		}
////		}
////		return data;
////	}
//
//
//	@ApiOperation(value = "Deletes an employee")
//	@RequestMapping(path = "/api/employee/{id}", method = RequestMethod.DELETE)
//	public void delete(@PathVariable Integer id) {
//		service.delete(id);
//	}
//
//	@ApiOperation(value = "Gets an employee by ID")
//	@RequestMapping(path = "/api/employee/{id}", method = RequestMethod.GET)
//	public EmployeeData get(@PathVariable Integer id) throws ApiException {
//		EmployeePojo p = service.get(id);
//		return convert(p);
//	}
//
//	@ApiOperation(value = "Gets list of all employees")
//	@RequestMapping(path = "/api/employee", method = RequestMethod.GET)
//	public List<EmployeeData> getAll() {
//		List<EmployeePojo> list = service.getAll();
//		List<EmployeeData> list2 = new ArrayList<EmployeeData>();
//		for (EmployeePojo p : list) {
//			list2.add(convert(p));
//		}
//		return list2;
//	}
//
//	@ApiOperation(value = "Updates an employee")
//	@RequestMapping(path = "/api/employee/{id}", method = RequestMethod.PUT)
//	public void update(@PathVariable Integer id, @RequestBody EmployeeForm f) throws ApiException {
//		EmployeePojo p = convert(f);
//		service.update(id, p);
//	}
//
//
//	private static EmployeeData convert(EmployeePojo p) {
//		EmployeeData d = new EmployeeData();
//		d.setAge(p.getAge());
//		d.setName(p.getName());
//		d.setId(p.getId());
//		return d;
//	}
//
//	private static EmployeePojo convert(EmployeeForm f) {
//		EmployeePojo p = new EmployeePojo();
//		p.setAge(f.getAge());
//		p.setName(f.getName());
//		return p;
//	}
//
//
//}

package com.increff.employee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/employee")
	public ModelAndView employee() {
		return mav("employee.html");
	}

	@RequestMapping(value = "/ui/admin")
	public ModelAndView admin() {
		return mav("user.html");
	}

	@RequestMapping(value = "/ui/brand")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/product")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/ui/customerDetail")
	public ModelAndView customerDetail() {
		return mav("customerDetail.html");
	}

	@RequestMapping(value = "/ui/salesreport")
	public ModelAndView salesReport() {
		return mav("salesReport.html");
	}

	@RequestMapping(value = "/ui/brandreport")
	public ModelAndView brandReport() {
		return mav("brandReport.html");
	}
	@RequestMapping(value = "/ui/inventoryreport")
	public ModelAndView inventoryReport() {
		return mav("inventoryReport.html");
	}

	@RequestMapping(value = "/ui/dailyreport")
	public ModelAndView dailyReport() {
		return mav("dailyReport.html");
	}

	@RequestMapping(value = "/ui/reportsview")
	public ModelAndView reportsview() {
		return mav("reportsview.html");
	}

}

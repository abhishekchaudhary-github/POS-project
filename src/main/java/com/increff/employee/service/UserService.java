package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.employee.model.InfoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.UserDao;
import com.increff.employee.pojo.UserPojo;
import org.springframework.web.servlet.ModelAndView;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Autowired
	private InfoData info;

	@Transactional
	public void add(UserPojo p) throws ApiException {
		normalize(p);
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			info.setMessage("this email has already been registered");
			return;
		}
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo getById(Integer id) throws ApiException {
		return dao.select(id);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Transactional
	public void setMessage() {
		info.setMessage("");
	}

	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}
}

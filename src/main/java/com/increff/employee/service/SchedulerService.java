package com.increff.employee.service;

import com.increff.employee.dao.SchedulerDao;
import com.increff.employee.pojo.SchedulerPojo;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private SchedulerDao dao;
    @Transactional(rollbackOn = ApiException.class)
    public void add(SchedulerPojo p) throws ApiException {
        dao.insert(p);
    }

    @Transactional
    public List<SchedulerPojo> getAll() {
        return dao.selectAll();
    }
}

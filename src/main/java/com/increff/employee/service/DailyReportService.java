package com.increff.employee.service;

import com.increff.employee.dao.DailyReportDao;
import com.increff.employee.pojo.DailyReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DailyReportService {

    @Autowired
    private DailyReportDao dao;
    @Transactional(rollbackOn = ApiException.class)
    public void add(DailyReportPojo p) throws ApiException {
        dao.insert(p);
    }

    @Transactional
    public List<DailyReportPojo> getAll(LocalDateTime startTime,LocalDateTime endTime) {
        return dao.select(startTime,endTime);
    }
}

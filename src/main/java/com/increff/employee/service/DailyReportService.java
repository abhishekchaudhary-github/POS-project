package com.increff.employee.service;

import com.increff.employee.dao.DailyReportDao;
import com.increff.employee.model.DailyReportForm;
import com.increff.employee.pojo.DailyReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DailyReportService {

    @Autowired
    private DailyReportDao dao;
    @Transactional(rollbackOn = ApiException.class)
    public Integer add(DailyReportPojo p) throws ApiException {
        return dao.insert(p);
    }
    @Transactional
    public List<DailyReportPojo> getAll() {
        List<DailyReportPojo> dailyReportPojoList =  dao.selectAll();
        dailyReportPojoList.remove(0);
        dailyReportPojoList.remove(0);
        return dailyReportPojoList;
    }
    @Transactional
    public List<DailyReportPojo> getAll(LocalDateTime startTime,LocalDateTime endTime) {
        List<DailyReportPojo> dailyReportPojoList =  dao.select(startTime,endTime);
        System.out.println(dailyReportPojoList);
        for(int i=0;i<dailyReportPojoList.size();i++) {
            if(dailyReportPojoList.get(i).getId()==1 || dailyReportPojoList.get(i).getId()==2) {
                dailyReportPojoList.remove(0);
                --i;
            }
        }
        return dailyReportPojoList;
    }

    @Transactional
    public List<DailyReportPojo> getAll(DailyReportForm form) {
        LocalDateTime startTime = convertTime(form.getStartTime());
        LocalDateTime endTime = convertTime(form.getEndTime());
        List<DailyReportPojo> dailyReportPojoList = dao.select(startTime,endTime);
        for(int i=0;i<dailyReportPojoList.size();i++) {
            if(dailyReportPojoList.get(i).getId()==1 || dailyReportPojoList.get(i).getId()==2) {
                dailyReportPojoList.remove(0);
                --i;
            }
        }
        return dailyReportPojoList;
    }
    @Transactional
    public DailyReportPojo get(Integer id) {
        return dao.select(id);
    }


    @Transactional
    public Integer getLastId() {
        return dao.selectLastId();
    }

    private LocalDateTime convertTime(String dateString){
        //careful here
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the string to obtain a LocalDate object
        LocalDate date = LocalDate.parse(dateString, formatter);

        // Define the specific time
        int hour = 00;
        int minute = 00;
        int second = 00;

        // Create a LocalDateTime object with the date and specific time
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, minute, second));
        return dateTime;
    }




}

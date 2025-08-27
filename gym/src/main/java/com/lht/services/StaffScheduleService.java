/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.StaffSchedule;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

/**
 *
 * @author admin
 */
public interface StaffScheduleService {

    List<StaffSchedule> getStaffSchedules(Map<String, String> params);

    List<StaffSchedule> getAllStaffSchedules();

    StaffSchedule getStaffScheduleById(Integer id);

    StaffSchedule addOrUpdateStaffSchedule(StaffSchedule ss);

    boolean deleteStaffSchedule(Integer id);

    public Page<StaffSchedule> getAllSort(String sortField, String sortDir, int page, int size);
    
    public double sumDurationByStaffIdAndMonthYear(int staffId, int month, int year);
    
    public List<StaffSchedule> getStaffScheduleByStaffId(Integer id);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.StaffDayOff;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

/**
 *
 * @author admin
 */
public interface StaffDayOffService {

    List<StaffDayOff> getStaffDayOffs(Map<String, String> params);

    List<StaffDayOff> getAllStaffDayOffs();

    StaffDayOff getStaffDayOffById(Integer id);

    StaffDayOff addOrUpdateStaffDayOff(StaffDayOff sdo);

    boolean deleteStaffDayOff(Integer id);
    
    public Page<StaffDayOff> getAllSort(String sortField, String sortDir, int page, int size);
    
    public int countByStaffIdAndMonthYear(int staffId, int month, int year);
    
    public List<StaffDayOff> getStaffDayOffByStaffId(Integer id);
}

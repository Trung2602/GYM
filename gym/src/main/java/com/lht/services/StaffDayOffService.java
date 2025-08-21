/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.StaffDayOff;
import java.util.List;
import java.util.Map;

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
    
    public List<StaffDayOff> getAllSort(String sortField, String sortDir);
    
    public int countByStaffIdAndMonthYear(int staffId, int month, int year);
}

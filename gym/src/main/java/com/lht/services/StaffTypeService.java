/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.StaffType;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface StaffTypeService {

    List<StaffType> getStaffTypes(Map<String, String> params);

    List<StaffType> getAllStaffTypes();

    StaffType getStaffTypeById(Integer id);

    StaffType addOrUpdateStaffType(StaffType st);

    boolean deleteStaffType(Integer id);
    
    public List<StaffType> getAllSort(String sortField, String sortDir);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.Staff;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface StaffService {

    List<Staff> getStaffs(Map<String, String> params);

    List<Staff> getAllStaffs();

    Staff getStaffById(Integer id);

    Staff addOrUpdateStaff(Staff s);

    boolean deleteStaff(Integer id);
}

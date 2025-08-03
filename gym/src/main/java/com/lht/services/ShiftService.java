/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.Shift;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface ShiftService {

    List<Shift> getShifts(Map<String, String> params);

    List<Shift> getAllShifts();

    Shift getShiftById(Integer id);

    Shift addOrUpdateShift(Shift s);

    boolean deleteShift(Integer id);
    
    public List<Shift> getAllSort(String sortField, String sortDir);
}

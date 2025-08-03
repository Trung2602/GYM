/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.CustomerSchedule;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface CustomerScheduleService {

    List<CustomerSchedule> getCustomerSchedules(Map<String, String> params);

    List<CustomerSchedule> getAllCustomerSchedules();

    CustomerSchedule getCustomerScheduleById(Integer id);

    CustomerSchedule addOrUpdateCustomerSchedule(CustomerSchedule cs);

    boolean deleteCustomerSchedule(Integer id);
    
    public List<CustomerSchedule> getAllSort(String sortField, String sortDir);
}

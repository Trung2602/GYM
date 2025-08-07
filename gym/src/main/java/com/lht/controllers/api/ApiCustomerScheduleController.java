/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.CustomerSchedule;
import com.lht.services.CustomerScheduleService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api")
public class ApiCustomerScheduleController {
    @Autowired
    private CustomerScheduleService customerScheduleService;

    @GetMapping("/customer-schedules-all")
    public ResponseEntity<List<CustomerSchedule>> getCustomerSchedulesAll() {
        return ResponseEntity.ok(this.customerScheduleService.getAllCustomerSchedules());
    }
    
    @GetMapping("/customer-schedules-filter")
    public ResponseEntity<List<CustomerSchedule>> getCustomerSchedulesFilter(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(this.customerScheduleService.getCustomerSchedules(params));
    }

    @GetMapping("customer-schedules-sort")
    public ResponseEntity<List<CustomerSchedule>> getCustomerSchedulesSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(this.customerScheduleService.getAllSort(sortField, sortDir));
    }
    
    @GetMapping("customer-schedule/{id}")
    public ResponseEntity<CustomerSchedule> getCustomerScheduleById(@PathVariable("id") Integer id) {   
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.customerScheduleService.getCustomerScheduleById(id));
    }

    @PostMapping("customer-schedule-update")
    public ResponseEntity<CustomerSchedule> addOrUpdateCustomerSchedule(@RequestBody CustomerSchedule customerSchedule) {
        return ResponseEntity.ok(this.customerScheduleService.addOrUpdateCustomerSchedule(customerSchedule));
    }

    @DeleteMapping("customer-schedule-delete/{id}")
    public ResponseEntity<Void> deleteCustomerSchedule(@PathVariable("id") Integer id) {
        if (this.customerScheduleService.deleteCustomerSchedule(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

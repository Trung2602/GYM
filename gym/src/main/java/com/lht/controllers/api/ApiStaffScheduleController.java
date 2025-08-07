/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.StaffSchedule;
import com.lht.services.StaffScheduleService;
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
public class ApiStaffScheduleController {
    @Autowired
    private StaffScheduleService staffScheduleService;

    @GetMapping("/staff-schedules-all")
    public ResponseEntity<List<StaffSchedule>> getStaffSchedulesAll() {
        return ResponseEntity.ok(this.staffScheduleService.getAllStaffSchedules());
    }
    
    @GetMapping("/staff-schedules-filter")
    public ResponseEntity<List<StaffSchedule>> getStaffSchedulesFilter(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(this.staffScheduleService.getStaffSchedules(params));
    }

    @GetMapping("staff-schedules-sort")
    public ResponseEntity<List<StaffSchedule>> getStaffSchedulesSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(this.staffScheduleService.getAllSort(sortField, sortDir));
    }
    
    @GetMapping("staff-schedule/{id}")
    public ResponseEntity<StaffSchedule> getStaffScheduleById(@PathVariable("id") Integer id) {   
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.staffScheduleService.getStaffScheduleById(id));
    }

    @PostMapping("staff-schedule-update")
    public ResponseEntity<StaffSchedule> addOrUpdateStaffSchedule(@RequestBody StaffSchedule staffSchedule) {
        return ResponseEntity.ok(this.staffScheduleService.addOrUpdateStaffSchedule(staffSchedule));
    }

    @DeleteMapping("staff-schedule-delete/{id}")
    public ResponseEntity<Void> deleteStaffSchedule(@PathVariable("id") Integer id) {
        if (this.staffScheduleService.deleteStaffSchedule(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

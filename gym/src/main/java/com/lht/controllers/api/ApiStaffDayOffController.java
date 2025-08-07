/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.StaffDayOff;
import com.lht.services.StaffDayOffService;
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
public class ApiStaffDayOffController {
    @Autowired
    private StaffDayOffService staffDayOffService;

    @GetMapping("/staff-day-offs-all")
    public ResponseEntity<List<StaffDayOff>> getStaffDayOffsAll() {
        return ResponseEntity.ok(this.staffDayOffService.getAllStaffDayOffs());
    }
    
    @GetMapping("/staff-day-offs-filter")
    public ResponseEntity<List<StaffDayOff>> getStaffDayOffsFilter(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(this.staffDayOffService.getStaffDayOffs(params));
    }

    @GetMapping("staff-day-offs-sort")
    public ResponseEntity<List<StaffDayOff>> getStaffDayOffsSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(this.staffDayOffService.getAllSort(sortField, sortDir));
    }
    
    @GetMapping("staff-day-off/{id}")
    public ResponseEntity<StaffDayOff> getStaffDayOffById(@PathVariable("id") Integer id) {   
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.staffDayOffService.getStaffDayOffById(id));
    }

    @PostMapping("staff-day-off-update")
    public ResponseEntity<StaffDayOff> addOrUpdateStaffDayOff(@RequestBody StaffDayOff staffDayOff) {
        return ResponseEntity.ok(this.staffDayOffService.addOrUpdateStaffDayOff(staffDayOff));
    }

    @DeleteMapping("staff-day-off-delete/{id}")
    public ResponseEntity<Void> deleteStaffDayOff(@PathVariable("id") Integer id) {
        if (this.staffDayOffService.deleteStaffDayOff(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

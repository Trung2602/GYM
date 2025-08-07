/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.Shift;
import com.lht.services.ShiftService;
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
public class ApiShiftController {
    
    @Autowired
    private ShiftService shiftService;

    @GetMapping("/shifts-all")
    public ResponseEntity<List<Shift>> getShiftsAll() {
        return ResponseEntity.ok(this.shiftService.getAllShifts());
    }
    
    @GetMapping("/shifts-filter")
    public ResponseEntity<List<Shift>> getShiftsFilter(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(this.shiftService.getShifts(params));
    }

    @GetMapping("shifts-sort")
    public ResponseEntity<List<Shift>> getShiftsSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(this.shiftService.getAllSort(sortField, sortDir));
    }
    
    @GetMapping("shift/{id}")
    public ResponseEntity<Shift> getShiftById(@PathVariable("id") Integer id) {   
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.shiftService.getShiftById(id));
    }

    @PostMapping("shift-update")
    public ResponseEntity<Shift> addOrUpdateShift(@RequestBody Shift shift) {
        return ResponseEntity.ok(this.shiftService.addOrUpdateShift(shift));
    }

    @DeleteMapping("shift-delete/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable("id") Integer id) {
        if (this.shiftService.deleteShift(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

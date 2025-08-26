/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.StaffType;
import com.lht.services.StaffTypeService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class ApiStaffTypeController {
    
    @Autowired
    private StaffTypeService staffTypeService;

    @GetMapping("/staff-types-all")
    public ResponseEntity<List<StaffType>> getStaffTypesAll() {
        return ResponseEntity.ok(this.staffTypeService.getAllStaffTypes());
    }
    
    @GetMapping("/staff-types-filter")
    public ResponseEntity<List<StaffType>> getStaffTypesFilter(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(this.staffTypeService.getStaffTypes(params));
    }

    @GetMapping("staff-types-sort")
    public ResponseEntity<Page<StaffType>> getStaffTypesSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(this.staffTypeService.getAllSort(sortField, sortDir, page, size));
    }
    
    @GetMapping("staff-type/{id}")
    public ResponseEntity<StaffType> getStaffTypeById(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.staffTypeService.getStaffTypeById(id));
    }

    @PostMapping("staff-type-update")
    public ResponseEntity<StaffType> addOrUpdateStaffType(@RequestBody StaffType staffType) {
        return ResponseEntity.ok(this.staffTypeService.addOrUpdateStaffType(staffType));
    }

    @DeleteMapping("staff-type-delete/{id}")
    public ResponseEntity<Void> deleteStaffType(@PathVariable("id") Integer id) {
        if (this.staffTypeService.deleteStaffType(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

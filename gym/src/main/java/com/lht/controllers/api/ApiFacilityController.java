/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.Facility;
import com.lht.services.FacilityService;
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
public class ApiFacilityController {
    @Autowired
    private FacilityService facilityService;

    @GetMapping("/facilities-all")
    public ResponseEntity<List<Facility>> getFacilitiesAll() {
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }
    
    @GetMapping("/facilities-filter")
    public ResponseEntity<List<Facility>> getFacilitiesFilter(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(facilityService.getFacilities(params));
    }

    @GetMapping("facilities-sort")
    public ResponseEntity<List<Facility>> getFacilitiesSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(facilityService.getAllSort(sortField, sortDir));
    }
    
    @GetMapping("facility/{id}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.facilityService.getFacilityById(id));
    }

    @PostMapping("facility-update")
    public ResponseEntity<Facility> addOrUpdateFacility(@RequestBody Facility facility) {
        return ResponseEntity.ok(this.facilityService.addOrUpdateFacility(facility));
    }

    @DeleteMapping("facility-delete/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable("id") Integer id) {
        if (this.facilityService.deleteFacility(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

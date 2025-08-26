/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.Plan;
import com.lht.services.PlanService;
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
public class ApiPlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("/plans-all")
    public ResponseEntity<List<Plan>> getPlansAll() {
        return ResponseEntity.ok(this.planService.getAllPlans());
    }
    
    @GetMapping("/plans-filter")
    public ResponseEntity<List<Plan>> getPlansFilter(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(this.planService.getPlans(params));
    }

    @GetMapping("plans-sort")
    public ResponseEntity<Page<Plan>> getPlansSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        return ResponseEntity.ok(this.planService.getAllSort(sortField, sortDir,page,size));
    }
    
    @GetMapping("plan/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.planService.getPlanById(id));
    }

    @PostMapping("plan-update")
    public ResponseEntity<Plan> addOrUpdatePlan(@RequestBody Plan plan) {
        return ResponseEntity.ok(this.planService.addOrUpdatePlan(plan));
    }

    @DeleteMapping("plan-delete/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable("id") Integer id) {
        if (this.planService.deletePlan(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

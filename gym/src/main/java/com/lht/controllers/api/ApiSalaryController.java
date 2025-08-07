/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.Salary;
import com.lht.services.SalaryService;
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
public class ApiSalaryController {
    @Autowired
    private SalaryService salaryService;

    @GetMapping("/salaries-all")
    public ResponseEntity<List<Salary>> getSalariesAll() {
        return ResponseEntity.ok(this.salaryService.getAllSalaries());
    }
    
    @GetMapping("/salaries-filter")
    public ResponseEntity<List<Salary>> getSalariesFilter(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(this.salaryService.getSalaries(params));
    }

    @GetMapping("salaries-sort")
    public ResponseEntity<List<Salary>> getSalariesSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(this.salaryService.getAllSort(sortField, sortDir));
    }
    
    @GetMapping("salary/{id}")
    public ResponseEntity<Salary> getSalaryById(@PathVariable("id") Integer id) {   
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.salaryService.getSalaryById(id));
    }

    @PostMapping("salary-update")
    public ResponseEntity<Salary> addOrUpdateSalary(@RequestBody Salary salary) {
        return ResponseEntity.ok(this.salaryService.addOrUpdateSalary(salary));
    }

    @DeleteMapping("salary-delete/{id}")
    public ResponseEntity<Void> deleteSalary(@PathVariable("id") Integer id) {
        if (this.salaryService.deleteSalary(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

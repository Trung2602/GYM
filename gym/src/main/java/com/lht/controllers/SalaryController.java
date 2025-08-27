/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.Salary;
import com.lht.services.SalaryService;
import com.lht.services.StaffDayOffService;
import com.lht.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
public class SalaryController {

    @Autowired
    private SalaryService salaryService;
    
    @Autowired
    private StaffService staffService;
    
    @Autowired
    private StaffDayOffService staffDayOffService;

    @GetMapping("/salaries")
    public String listSalaries(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<Salary> salaryPage = salaryService.getAllSort(sortField, sortDir, page, size);

        model.addAttribute("salaries", salaryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", salaryPage.getTotalPages());
        model.addAttribute("totalItems", salaryPage.getTotalElements());
        model.addAttribute("pageSize", size);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "salaries";
    }

    @GetMapping("/salary/{id}")
    public String getSalary(@PathVariable("id") Integer id, Model model) {
        Salary salary = salaryService.getSalaryById(id);
        model.addAttribute("salary", salary);
        return "salaries";
    }

    @GetMapping("/salary/add")
    public String convertSalaryForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("salary", (id != null) ? salaryService.getSalaryById(id) : new Salary());
        model.addAttribute("staffs", this.staffService.getAllStaffs());
        model.addAttribute("dayOffs", this.staffDayOffService.getAllStaffDayOffs());
        return "salary-add";
    }

    @PostMapping("/salary-update")
    public String updateSalary(@ModelAttribute(value = "salary") Salary s, BindingResult result,
            Model model) {
        if (this.salaryService.addOrUpdateSalary(s) != null) {
            return "redirect:/salaries";
        }
        return "salary-add";
    }

    @DeleteMapping("/salary-delete/{id}")
    public String destroySalary(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.salaryService.deleteSalary(id);
        }
        return "redirect:/salaries";
    }

    @GetMapping("/salary-momth")
    public String calculateMonthly(Model model,
            @RequestParam int month,
            @RequestParam int year) {
        
        this.salaryService.calculateMonthlySalaries(month, year);
        model.addAttribute("salaries", salaryService.getAllSalaries());
        return "salaries";
    }
    
}

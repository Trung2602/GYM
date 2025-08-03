/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.Salary;
import com.lht.services.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/salaries")
    public String listSalaries(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        model.addAttribute("salaries", salaryService.getAllSort(sortField, sortDir));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
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

}

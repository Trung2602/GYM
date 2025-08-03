/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.StaffType;
import com.lht.services.StaffTypeService;
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
public class StaffTypeController {

    @Autowired
    private StaffTypeService staffTypeService;

    @GetMapping("/staff-types")
    public String listStaffTypes(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        model.addAttribute("staffTypes", staffTypeService.getAllSort(sortField, sortDir));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "staff-types";
    }

    @GetMapping("/staff-type/{id}")
    public String getStaffType(@PathVariable("id") Integer id, Model model) {
        StaffType staffType = staffTypeService.getStaffTypeById(id);
        model.addAttribute("staffType", staffType);
        return "staff-types";
    }

    @GetMapping("/staff-type/add")
    public String convertStaffTypeForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("staffType", (id != null) ? staffTypeService.getStaffTypeById(id) : new StaffType());
        return "staff-type-add";
    }

    @PostMapping("/staff-type-update")
    public String updateStaffType(@ModelAttribute(value = "staffType") StaffType s, BindingResult result,
            Model model) {
        if (this.staffTypeService.addOrUpdateStaffType(s) != null) {
            return "redirect:/staff-types";
        }
        return "staff-type-add";
    }

    @DeleteMapping("/staff-type-delete/{id}")
    public String destroyStaffType(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.staffTypeService.deleteStaffType(id);
        }
        return "redirect:/staff-types";
    }

}

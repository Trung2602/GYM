/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.Staff;
import com.lht.services.FacilityService;
import com.lht.services.StaffService;
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
public class StaffController {
    
    @Autowired
    private StaffService staffService;
    
    @Autowired
    private FacilityService facilityService;
    
    @Autowired
    private StaffTypeService staffTypeService;
    

    @GetMapping("/staffs")
    public String listStaffs(Model model) {
        model.addAttribute("staffs", this.staffService.getAllStaffs());
        return "accounts";
    }

    @GetMapping("/staff/{id}")
    public String getStaff(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            model.addAttribute("staff", this.staffService.getStaffById(id));
        }
        return "accounts";
    }

    @GetMapping("/staff/add")
    public String convertStaffForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("account", (id != null) ? this.staffService.getStaffById(id) : new Staff());
        model.addAttribute("facilities", this.facilityService.getAllFacilities());
        model.addAttribute("types", this.staffTypeService.getAllStaffTypes());
        model.addAttribute("formAction", "/staff-update");
        model.addAttribute("role", "staff");

        return "account-add";
    }

    @PostMapping("/staff-update")
    public String updateStaff(@ModelAttribute(value = "account") Staff p, BindingResult result,
            Model model) {
        if (this.staffService.addOrUpdateStaff(p) != null) {
            return "redirect:/accounts";
        }
        return "account-add";
    }

    @DeleteMapping("/staff-delete/{id}")
    public String destroyStaff(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.staffService.deleteStaff(id);
        }
        return "redirect:/accounts";
    }
}

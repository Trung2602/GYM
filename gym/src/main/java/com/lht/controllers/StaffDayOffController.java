/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.StaffDayOff;
import com.lht.services.StaffDayOffService;
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
public class StaffDayOffController {

    @Autowired
    private StaffDayOffService staffDayOffService;

    @GetMapping("/staff-day-offs")
    public String listStaffDayOffs(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        model.addAttribute("staffDayOffs", staffDayOffService.getAllSort(sortField, sortDir));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "staff-day-offs";
    }

    @GetMapping("/staff-day-off/{id}")
    public String getStaffDayOff(@PathVariable("id") Integer id, Model model) {
        StaffDayOff staffDayOff = staffDayOffService.getStaffDayOffById(id);
        model.addAttribute("staffDayOff", staffDayOff);
        return "staff-day-offs";
    }

    @GetMapping("/staff-day-off/add")
    public String convertStaffDayOffForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("staffDayOff", (id != null) ? staffDayOffService.getStaffDayOffById(id) : new StaffDayOff());
        return "staff-day-off-add";
    }

    @PostMapping("/staff-day-off-update")
    public String updateStaffDayOff(@ModelAttribute(value = "staffDayOff") StaffDayOff sdo, BindingResult result,
            Model model) {
        if (this.staffDayOffService.addOrUpdateStaffDayOff(sdo) != null) {
            return "redirect:/staff-day-offs";
        }
        return "staff-day-off-add";
    }

    @DeleteMapping("/staff-day-off-delete/{id}")
    public String destroyStaffDayOff(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.staffDayOffService.deleteStaffDayOff(id);
        }
        return "redirect:/staff-day-offs";
    }

}

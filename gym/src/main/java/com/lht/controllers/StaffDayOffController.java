/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.services.StaffDayOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author admin
 */
@Controller
public class StaffDayOffController {

    @Autowired
    private StaffDayOffService staffDayOffService;

    @GetMapping("/staff-day-offs")
    public String listStaffDayOffs(Model model) {
        model.addAttribute("staffDayOffs", staffDayOffService.getAllStaffDayOffs());
        return "staff-day-offs";
    }

}

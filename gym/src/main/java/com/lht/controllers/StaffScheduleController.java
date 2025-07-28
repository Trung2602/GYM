/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.services.StaffScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author admin
 */
@Controller
public class StaffScheduleController {

    @Autowired
    private StaffScheduleService staffScheduleService;

    @GetMapping("/staff-schedules")
    public String listStaffSchedules(Model model) {
        model.addAttribute("staffSchedules", staffScheduleService.getAllStaffSchedules());
        return "staff-schedules";
    }

}

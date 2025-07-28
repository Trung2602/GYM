/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.services.CustomerScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author admin
 */
@Controller
public class CustomerScheduleController {

    @Autowired
    private CustomerScheduleService customerScheduleService;

    @GetMapping("/customer-schedules")
    public String listCustomerSchedules(Model model) {
        model.addAttribute("customerSchedules", customerScheduleService.getAllCustomerSchedules());
        return "customer-schedules";
    }

}

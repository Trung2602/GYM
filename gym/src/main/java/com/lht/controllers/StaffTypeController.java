/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.services.StaffTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author admin
 */
@Controller
public class StaffTypeController {

    @Autowired
    private StaffTypeService staffTypeService;

    @GetMapping("/staffTypes")
    public String listStaffTypes(Model model) {
        model.addAttribute("staffTypes", staffTypeService.getAllStaffTypes());
        return "staff-types";
    }

}

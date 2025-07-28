/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.Plan;
import com.lht.services.PlanService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("/plans")
    public String listFacilities(Model model) {
        model.addAttribute("plans", planService.getAllPlans());
        return "plans";
    }

    @GetMapping("/plan/{id}")
    public String getPlan(@PathVariable("id") Integer id, Model model) {
        Plan plan = planService.getPlanById(id);
        model.addAttribute("plan", plan);
        return "plans";
    }

}

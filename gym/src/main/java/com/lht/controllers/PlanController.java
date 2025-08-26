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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("/plans")
    public String listPlans(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        //Nếu không có sortField thì sẽ sắp xếp theo "id" và thứ tự tăng dần "asc"
        Page<Plan> planPage = planService.getAllSort(sortField, sortDir, page, size);

        model.addAttribute("plans", planPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", planPage.getTotalPages());
        model.addAttribute("totalItems", planPage.getTotalElements());
        model.addAttribute("pageSize", size);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "plans";
    }

    @GetMapping("/plan/{id}")
    public String getPlan(@PathVariable("id") Integer id, Model model) {
        Plan plan = planService.getPlanById(id);
        model.addAttribute("plan", plan);
        return "plans";
    }

    @GetMapping("/plan/add")
    public String convertPlanForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("plan", (id != null) ? planService.getPlanById(id) : new Plan());
        return "plan-add";
    }

    @PostMapping("/plan-update")
    public String updatePlan(@ModelAttribute(value = "plan") Plan p, BindingResult result,
            Model model) {
        if (this.planService.addOrUpdatePlan(p) != null) {
            return "redirect:/plans";
        }
        return "plan-add";
    }

    @DeleteMapping("/plan-delete/{id}")
    public String destroyPlan(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.planService.deletePlan(id);
        }
        return "redirect:/plans";
    }
}

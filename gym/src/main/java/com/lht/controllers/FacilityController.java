/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.Facility;
import com.lht.services.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping("/facilities")
    public String listFacilities(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        model.addAttribute("facilities", facilityService.getAllSort(sortField, sortDir));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "facilities"; // Trỏ tới facility.html
    }

    @GetMapping("/facilitiy/{id}")
    public String getFacility(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("facilities", facilityService.getFacilityById(id));
        return "facilities"; // Trỏ tới facility.html
    }
    
    @GetMapping("/facility/add")
    public String convertFacilityForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("facility", (id != null) ? facilityService.getFacilityById(id) : new Facility());
        return "facility-add"; // Trang form thêm/sửa
    }

    @PostMapping("/facility-update")
    public String updateFacility(@ModelAttribute(value = "facility") Facility p, BindingResult result,
            Model model) {
        if(facilityService.addOrUpdateFacility(p) != null) {
            return "redirect:/facilities";
        }
        return "facilitiy-add";
    }

    @GetMapping("/facility-delete/{id}")
    public String destroyFacility(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            facilityService.deleteFacility(id);
        }
        return "redirect:/facilities";
    }

}

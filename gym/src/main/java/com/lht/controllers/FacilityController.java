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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author admin
 */
@Controller
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @GetMapping("/facilities")
    public String listFacilities(Model model) {
        model.addAttribute("facilities", facilityService.getAllFacilities());
        return "facilities"; // Trỏ tới facility.html
    }

    @GetMapping("/facility-add")
    public String addForm(Model model) {
        model.addAttribute("facility", new Facility());
        return "facility-form"; // Trang form thêm/sửa
    }

    @PostMapping("/facility-add")
    public String saveFacility(@ModelAttribute Facility facility) {
        facilityService.addOrUpdateFacility(facility);
        return "redirect:/Facilities";
    }

    @GetMapping("/facility-edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("facility", facilityService.getFacilityById(id));
        return "facility-form";
    }

    @GetMapping("/facility-delete/{id}")
    public String delete(@PathVariable int id) {
        facilityService.deleteFacility(id);
        return "redirect:/Facilities";
    }

}

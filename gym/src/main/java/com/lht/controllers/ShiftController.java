/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.Shift;
import com.lht.services.ShiftService;
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
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @GetMapping("/shifts")
    public String listShifts(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        model.addAttribute("shifts", shiftService.getAllSort(sortField, sortDir));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "shifts";
    }

    @GetMapping("/shift/{id}")
    public String getShift(@PathVariable("id") Integer id, Model model) {
        Shift shift = shiftService.getShiftById(id);
        model.addAttribute("shift", shift);
        return "shifts";
    }

    @GetMapping("/shift/add")
    public String convertShiftForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("shift", (id != null) ? shiftService.getShiftById(id) : new Shift());
        return "shift-add";
    }

    @PostMapping("/shift-update")
    public String updateShift(@ModelAttribute(value = "shift") Shift s, BindingResult result,
            Model model) {
        if (this.shiftService.addOrUpdateShift(s) != null) {
            return "redirect:/shifts";
        }
        return "shift-add";
    }

    @DeleteMapping("/shift-delete/{id}")
    public String destroyShift(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.shiftService.deleteShift(id);
        }
        return "redirect:/shifts";
    }

}

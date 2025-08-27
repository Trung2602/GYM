/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.StaffSchedule;
import com.lht.services.ShiftService;
import com.lht.services.StaffScheduleService;
import com.lht.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class StaffScheduleController {

    @Autowired
    private StaffScheduleService staffScheduleService;
    
    @Autowired
    private StaffService staffService;
    
    @Autowired
    private ShiftService shiftService;

    @GetMapping("/staff-schedules")
    public String listStaffSchedules(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<StaffSchedule> schedulePage = staffScheduleService.getAllSort(sortField, sortDir, page, size);

        model.addAttribute("staffSchedules", schedulePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", schedulePage.getTotalPages());
        model.addAttribute("totalItems", schedulePage.getTotalElements());
        model.addAttribute("pageSize", size);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "staff-schedules";
    }

    @GetMapping("/staff-schedule/{id}")
    public String getStaffSchedule(@PathVariable("id") Integer id, Model model) {
        StaffSchedule staffSchedule = staffScheduleService.getStaffScheduleById(id);
        model.addAttribute("staffSchedule", staffSchedule);
        return "staff-schedules";
    }

    @GetMapping("/staff-schedule/add")
    public String convertStaffScheduleForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("staffSchedule", (id != null) ? staffScheduleService.getStaffScheduleById(id) : new StaffSchedule());
        model.addAttribute("staffs", this.staffService.getAllStaffs());
        model.addAttribute("shifts", this.shiftService.getAllShifts());
        return "staff-schedule-add";
    }

    @PostMapping("/staff-schedule-update")
    public String updateStaffSchedule(@ModelAttribute(value = "staffSchedule") StaffSchedule ss, BindingResult result,
            Model model) {
        if (this.staffScheduleService.addOrUpdateStaffSchedule(ss) != null) {
            return "redirect:/staff-schedules";
        }
        return "staff-schedule-add";
    }

    @DeleteMapping("/staff-schedule-delete/{id}")
    public String destroyStaffSchedule(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.staffScheduleService.deleteStaffSchedule(id);
        }
        return "redirect:/staff-schedules";
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.CustomerSchedule;
import com.lht.services.CustomerScheduleService;
import com.lht.services.CustomerService;
import com.lht.services.FacilityService;
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
public class CustomerScheduleController {

    @Autowired
    private CustomerScheduleService customerScheduleService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private FacilityService facilityService;

    @GetMapping("/customer-schedules")
    public String listCustomerSchedules(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CustomerSchedule> schedulePage = customerScheduleService.getAllSort(sortField, sortDir, page, size);

        model.addAttribute("customerSchedules", schedulePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", schedulePage.getTotalPages());
        model.addAttribute("totalItems", schedulePage.getTotalElements());
        model.addAttribute("pageSize", size);
        
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "customer-schedules";
    }

    @GetMapping("/customer-schedule/{id}")
    public String getCustomerSchedule(@PathVariable("id") Integer id, Model model) {
        CustomerSchedule customerSchedule = customerScheduleService.getCustomerScheduleById(id);
        model.addAttribute("customerSchedule", customerSchedule);
        return "customer-schedules";
    }

    @GetMapping("/customer-schedule/add")
    public String convertCustomerScheduleForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("customerSchedule", (id != null) ? customerScheduleService.getCustomerScheduleById(id) : new CustomerSchedule());
        model.addAttribute("customers", this.customerService.getAllCustomers());
        model.addAttribute("staffs", this.staffService.getAllStaffs());
        model.addAttribute("facilities", this.facilityService.getAllFacilities());
        return "customer-schedule-add";
    }

    @PostMapping("/customer-schedule-update")
    public String updateCustomerSchedule(@ModelAttribute(value = "customerSchedule") CustomerSchedule cs, BindingResult result,
            Model model) {
        if (this.customerScheduleService.addOrUpdateCustomerSchedule(cs) != null) {
            return "redirect:/customer-schedules";
        }
        return "customer-schedule-add";
    }

    @DeleteMapping("/customer-schedule-delete/{id}")
    public String destroyCustomerSchedule(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.customerScheduleService.deleteCustomerSchedule(id);
        }
        return "redirect:/customer-schedules";
    }

}

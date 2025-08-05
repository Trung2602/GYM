/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.PayCustomer;
import com.lht.services.CustomerService;
import com.lht.services.PayCustomerService;
import com.lht.services.PlanService;
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
public class PayCustomerController {

    @Autowired
    private PayCustomerService payCustomerService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private PlanService planService;

    @GetMapping("/pay-customers")
    public String listPayCustomers(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        model.addAttribute("payCustomers", payCustomerService.getAllSort(sortField, sortDir));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "pay-customers";
    }

    @GetMapping("/pay-customer/{id}")
    public String getPayCustomer(@PathVariable("id") Integer id, Model model) {
        PayCustomer payCustomer = payCustomerService.getPayCustomerById(id);
        model.addAttribute("payCustomer", payCustomer);
        return "pay-customers";
    }

    @GetMapping("/pay-customer/add")
    public String convertPayCustomerForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("payCustomer", (id != null) ? payCustomerService.getPayCustomerById(id) : new PayCustomer());
        model.addAttribute("customers", this.customerService.getAllCustomers());
        model.addAttribute("plans", this.planService.getAllPlans());
        return "pay-customer-add";
    }

    @PostMapping("/pay-customer-update")
    public String updatePayCustomer(@ModelAttribute(value = "payCustomer") PayCustomer pc, BindingResult result,
            Model model) {
        if (this.payCustomerService.addOrUpdatePayCustomer(pc) != null) {
            return "redirect:/pay-customers";
        }
        return "pay-customer-add";
    }

    @DeleteMapping("/pay-customer-delete/{id}")
    public String destroyPayCustomer(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.payCustomerService.deletePayCustomer(id);
        }
        return "redirect:/pay-customers";
    }

}

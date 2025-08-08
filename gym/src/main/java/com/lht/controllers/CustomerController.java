/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.Customer;
import com.lht.services.CustomerService;
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
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String listAdmins(Model model) {
        model.addAttribute("customers", this.customerService.getAllCustomers());
        return "accounts";
    }

    @GetMapping("/customer/{id}")
    public String getAdmin(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            model.addAttribute("customer", this.customerService.getCustomerById(id));
        }
        return "accounts";
    }

    @GetMapping("/customer/add")
    public String convertAdminForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("account", (id != null) ? this.customerService.getCustomerById(id) : new Customer());
        model.addAttribute("formAction", "/customer-update");
        model.addAttribute("role", "customer");

        return "account-add";
    }

    @PostMapping("/customer-update")
    public String updateAdmin(@ModelAttribute(value = "customer") Customer p, BindingResult result,
            Model model) {
        if (this.customerService.addOrUpdateCustomer(p) != null) {
            return "redirect:/accounts";
        }
        return "account-add";
    }

    @DeleteMapping("/customer-delete/{id}")
    public String destroyAdmin(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.customerService.deleteCustomer(id);
        }
        return "redirect:/accounts";
    }
}

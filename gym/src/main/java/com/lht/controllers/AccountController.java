/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.services.AdminService;
import com.lht.services.CustomerService;
import com.lht.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 *
 * @author admin
 */
@Controller
public class AccountController {
    
    @Autowired
    private AdminService adminService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private CustomerService customerService;
    
    @GetMapping("/accounts")
    public String showAccounts(Model model) {
    model.addAttribute("admins", adminService.getAllAdmins());
    model.addAttribute("customers", customerService.getAllCustomers());
    model.addAttribute("staffs", staffService.getAllStaffs());
    return "accounts"; // accounts.html
}

}

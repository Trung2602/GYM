/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.Account;
import com.lht.services.AccountService;
import com.lht.services.AdminService;
import com.lht.services.CustomerService;
import com.lht.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;
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
    
    @GetMapping("/account/add")
    public String accountAdd(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null) {
            model.addAttribute("account", accountService.getAccountById(id));
        } else {
            model.addAttribute("account", new Account());
        }
       
        return "account-edit"; // accounts.html
    }
    
    @PostMapping("/account-add")
    public String addOrUpdateAccount(@ModelAttribute("account") Account a) {
        this.accountService.addOrUpdateAccount(a);
        return "redirect:/accounts"; // quay về trang danh sách tài khoản
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.Admin;
import com.lht.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author admin
 */
@Controller
public class AdminController {
    
//    @Autowired
//    private AdminService adminService;
//    
//    @GetMapping("/admin/add")
//    public String adminAdd(Model model) {
//        model.addAttribute("admin", new Admin());
//        return "account-edit";
//    }
//    
//    @PostMapping("/admin-add")
//    public String addOrUpdateAdmin(@ModelAttribute("admin") Admin a) {
//        this.adminService.addOrUpdateAdmin(a);
//        return "redirect:/accounts"; // quay về trang danh sách tài khoản
//    }
    
}

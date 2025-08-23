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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author admin
 */
@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admins")
    public String listAdmins(Model model) {
        model.addAttribute("admins", this.adminService.getAllAdmins());
        return "accounts";
    }

    @GetMapping("/admin/{id}")
    public String getAdmin(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            model.addAttribute("admin", this.adminService.getAdminById(id));
        }
        return "accounts";
    }

    @GetMapping("/admin/add")
    public String convertAdminForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("account", (id != null) ? this.adminService.getAdminById(id) : new Admin());
        model.addAttribute("formAction", "/admin-update");
        model.addAttribute("role", "admin");

        return "account-add";
    }

    @PostMapping("/admin-update")
    public String updateAdmin(@ModelAttribute(value = "account") Admin p, BindingResult result,
            Model model) {
        if (this.adminService.addOrUpdateAdmin(p) != null) {
            return "redirect:/accounts";
        }
        return "account-add";
    }

    @DeleteMapping("/admin-delete/{id}")
    public String destroyAdmin(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.adminService.deleteAdmin(id);
        }
        return "redirect:/accounts";
    }

}

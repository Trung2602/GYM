/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.services.PayCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author admin
 */
@Controller
public class PayCustomerController {

    @Autowired
    private PayCustomerService payCustomerService;

    @GetMapping("/pay-customers")
    public String listPayCustomers(Model model) {
        model.addAttribute("payCustomers", payCustomerService.getAllPayCustomers());
        return "pay-customers";
    }

}

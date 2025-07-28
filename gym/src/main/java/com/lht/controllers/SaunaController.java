/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.services.SaunaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author admin
 */
@Controller
public class SaunaController {

    @Autowired
    private SaunaService saunaService;

    @GetMapping("/saunas")
    public String listSaunas(Model model) {
        model.addAttribute("saunas", saunaService.getAllSaunas());
        return "saunas";
    }

}

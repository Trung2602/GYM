/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.Sauna;
import com.lht.services.SaunaService;
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
public class SaunaController {

    @Autowired
    private SaunaService saunaService;

    @GetMapping("/saunas")
    public String listSaunas(Model model,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        model.addAttribute("saunas", saunaService.getAllSort(sortField, sortDir));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "saunas";
    }

    @GetMapping("/sauna/{id}")
    public String getSauna(@PathVariable("id") Integer id, Model model) {
        Sauna sauna = saunaService.getSaunaById(id);
        model.addAttribute("sauna", sauna);
        return "saunas";
    }

    @GetMapping("/sauna/add")
    public String convertSaunaForm(@RequestParam(name = "id", required = false) Integer id, Model model) {
        model.addAttribute("sauna", (id != null) ? saunaService.getSaunaById(id) : new Sauna());
        return "sauna-add";
    }

    @PostMapping("/sauna-update")
    public String updateSauna(@ModelAttribute(value = "sauna") Sauna s, BindingResult result,
            Model model) {
        if (this.saunaService.addOrUpdateSauna(s) != null) {
            return "redirect:/saunas";
        }
        return "sauna-add";
    }

    @DeleteMapping("/sauna-delete/{id}")
    public String destroySauna(@PathVariable("id") Integer id, Model model) {
        if (id != null) {
            this.saunaService.deleteSauna(id);
        }
        return "redirect:/saunas";
    }

}

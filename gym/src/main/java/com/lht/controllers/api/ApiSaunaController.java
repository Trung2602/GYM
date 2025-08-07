/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.Sauna;
import com.lht.services.SaunaService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api")
public class ApiSaunaController {
    @Autowired
    private SaunaService saunaService;

    @GetMapping("/saunas-all")
    public ResponseEntity<List<Sauna>> getSaunasAll() {
        return ResponseEntity.ok(this.saunaService.getAllSaunas());
    }
    
    @GetMapping("/saunas-filter")
    public ResponseEntity<List<Sauna>> getSaunasFilter(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(this.saunaService.getSaunas(params));
    }

    @GetMapping("saunas-sort")
    public ResponseEntity<List<Sauna>> getSaunasSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(this.saunaService.getAllSort(sortField, sortDir));
    }
    
    @GetMapping("sauna/{id}")
    public ResponseEntity<Sauna> getSaunaById(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.saunaService.getSaunaById(id));
    }

    @PostMapping("sauna-update")
    public ResponseEntity<Sauna> addOrUpdateSauna(@RequestBody Sauna sauna) {
        return ResponseEntity.ok(this.saunaService.addOrUpdateSauna(sauna));
    }

    @DeleteMapping("sauna-delete/{id}")
    public ResponseEntity<Void> deleteSauna(@PathVariable("id") Integer id) {
        if (this.saunaService.deleteSauna(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.dto.PayCustomerDTO;
import com.lht.pojo.PayCustomer;
import com.lht.services.PayCustomerService;
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
public class ApiPayCustomerController {
    @Autowired
    private PayCustomerService payCustomerService;

    @GetMapping("/pay-customers-all/{id}")
    public ResponseEntity<List<PayCustomerDTO>> getPayCustomersAll(@PathVariable("id") Integer id) {
        List<PayCustomer> schedules = payCustomerService.getPayCustomerByCustomerId(id);
        // Map sang DTO
        List<PayCustomerDTO> dtos = schedules.stream()
                .map(PayCustomerDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/pay-customers-filter")
    public ResponseEntity<List<PayCustomer>> getPayCustomersFilter(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(this.payCustomerService.getPayCustomers(params));
    }

    @GetMapping("pay-customers-sort")
    public ResponseEntity<List<PayCustomer>> getPayCustomersSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(this.payCustomerService.getAllSort(sortField, sortDir));
    }
    
    @GetMapping("pay-customer/{id}")
    public ResponseEntity<PayCustomer> getPayCustomerById(@PathVariable("id") Integer id) {   
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.payCustomerService.getPayCustomerById(id));
    }

    @PostMapping("pay-customer-update")
    public ResponseEntity<PayCustomer> addOrUpdatePayCustomer(@RequestBody PayCustomer payCustomer) {
        return ResponseEntity.ok(this.payCustomerService.addOrUpdatePayCustomer(payCustomer));
    }

    @DeleteMapping("pay-customer-delete/{id}")
    public ResponseEntity<Void> deletePayCustomer(@PathVariable("id") Integer id) {
        if (this.payCustomerService.deletePayCustomer(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

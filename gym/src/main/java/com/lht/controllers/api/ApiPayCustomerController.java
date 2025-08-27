/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.dto.PayCustomerDTO;
import com.lht.pojo.Customer;
import com.lht.pojo.PayCustomer;
import com.lht.pojo.Plan;
import com.lht.services.CustomerService;
import com.lht.services.PayCustomerService;
import com.lht.services.PlanService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PlanService planService;

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
    public ResponseEntity<Page<PayCustomer>> getPayCustomersSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        return ResponseEntity.ok(this.payCustomerService.getAllSort(sortField, sortDir, page, size));
    }

    @GetMapping("pay-customer/{id}")
    public ResponseEntity<PayCustomer> getPayCustomerById(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.payCustomerService.getPayCustomerById(id));
    }

    @PostMapping("pay-customer-update")
    public ResponseEntity<?> addOrUpdatePayCustomer(@RequestBody PayCustomerDTO dto) {
        PayCustomer pc = new PayCustomer();
        pc.setId(dto.getId());
        pc.setDate(dto.getDate());

        // Gán customer
        if (dto.getCustomerName() != null) {
            Optional<Customer> customerOpt = customerService.getCustomerByName(dto.getCustomerName());
            if (customerOpt.isPresent()) {
                pc.setCustomerId(customerOpt.get());
            } else {
                return ResponseEntity.badRequest().body("Customer không tồn tại");
            }
        } else {
            return ResponseEntity.badRequest().body("Customer không được để trống");
        }

        // Gán plan
        if (dto.getPlanName() != null) {
            Optional<Plan> planOpt = planService.getPlanByName(dto.getPlanName());
            if (planOpt.isPresent()) {
                pc.setPlanId(planOpt.get());
            } else {
                return ResponseEntity.badRequest().body("Plan không tồn tại");
            }
        } else {
            return ResponseEntity.badRequest().body("Plan không được để trống");
        }
        
        
        PayCustomer saved = payCustomerService.addOrUpdatePayCustomer(pc);
        return ResponseEntity.ok(new PayCustomerDTO(saved));
    }

    @DeleteMapping("pay-customer-delete/{id}")
    public ResponseEntity<Void> deletePayCustomer(@PathVariable("id") Integer id) {
        if (this.payCustomerService.deletePayCustomer(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.dto.AccountDTO;
import com.lht.dto.CustomerDTO;
import com.lht.pojo.Customer;
import com.lht.services.AccountService;
import com.lht.services.CustomerService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api")
public class ApiCustomerController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private AccountService accountService;

    // Lấy danh sách có thể có filter qua query param (tên, giới tính, ... tùy bạn định nghĩa)
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomers(@RequestParam Map<String, String> params) {
        List<Customer> customers = params.isEmpty()
                ? customerService.getAllCustomers()
                : customerService.getCustomers(params);
        return ResponseEntity.ok(customers);
    }

    // Lấy toàn bộ khách hàng (nếu không muốn dùng filter)
    @GetMapping("/customers-all")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // Lấy 1 khách hàng theo ID
    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.notFound().build();
    }

    // Thêm mới hoặc cập nhật khách hàng (dựa vào có ID hay không)
    @PostMapping("/customer-update")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {
        if (customer.getId() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerService.addOrUpdateCustomer(customer));
    }

    // Xóa khách hàng theo ID
    @DeleteMapping("/custome/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/register-customer")
    public ResponseEntity<?> register(
            @ModelAttribute("account") Customer customer,
            @RequestPart(value = "image", required = false) MultipartFile file) {
        if (file != null) {
            customer.setFile(file); // gán MultipartFile vào Customer
        }
        // kiểm tra trùng username và mail
        String check = accountService.checkDuplicate(customer.getUsername(), customer.getMail());
        if (!"OK".equals(check)) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", check)); // trả lỗi nếu trùng
        }
        customer.setId(null);
        Customer saved = customerService.addOrUpdateCustomer(customer);
        return ResponseEntity.ok(new CustomerDTO(saved));
    }

}

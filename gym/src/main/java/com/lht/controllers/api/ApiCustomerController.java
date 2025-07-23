/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.Customer;
import com.lht.services.CustomerService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/customers")
public class ApiCustomerController {

    @Autowired
    private CustomerService customerService;

    // Lấy danh sách có thể có filter qua query param (tên, giới tính, ... tùy bạn định nghĩa)
    @GetMapping
    public List<Customer> getCustomers(@RequestParam Map<String, String> params) {
        return customerService.getCustomers(params);
    }

    // Lấy toàn bộ khách hàng (nếu không muốn dùng filter)
    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // Lấy 1 khách hàng theo ID
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Integer id) {
        return customerService.getCustomerById(id);
    }

    // Thêm mới hoặc cập nhật khách hàng (dựa vào có ID hay không)
    @PostMapping
    public Customer addOrUpdateCustomer(@RequestBody Customer customer) {
        return customerService.addOrUpdateCustomer(customer);
    }

    // Xóa khách hàng theo ID
    @DeleteMapping("/{id}")
    public boolean deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomer(id);
    }
}

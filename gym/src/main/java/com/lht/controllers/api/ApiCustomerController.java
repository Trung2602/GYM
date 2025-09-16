/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.dto.AccountDTO;
import com.lht.dto.CustomerDTO;
import com.lht.dto.VerifyCustomerDTO;
import com.lht.pojo.Customer;
import com.lht.services.AccountService;
import com.lht.services.CustomerService;
import com.lht.services.MailService;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
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

    @Autowired
    private MailService mailService;

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

//    @PostMapping("/register-customer")
//    public ResponseEntity<?> register(
//            @ModelAttribute("account") Customer customer,
//            @RequestPart(value = "image", required = false) MultipartFile file) {
//        if (file != null) {
//            customer.setFile(file); // gán MultipartFile vào Customer
//        }
//        // kiểm tra trùng username và mail
//        String check = accountService.checkDuplicate(customer.getUsername(), customer.getMail());
//        if (!"OK".equals(check)) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(Map.of("error", check)); // trả lỗi nếu trùng
//        }
//        customer.setId(null);
//        Customer saved = customerService.addOrUpdateCustomer(customer);
//        return ResponseEntity.ok(new CustomerDTO(saved));
//    }
    private final Map<String, VerifyCustomerDTO> pendingRegistrations = new ConcurrentHashMap<>();

    @PostMapping("/register-customer")
    public ResponseEntity<?> register(
            @ModelAttribute("account") Customer customer,
            @RequestPart(value = "image", required = false) MultipartFile file) {

        if (file != null) {
            customer.setFile(file);
        }

        // kiểm tra trùng username và mail
        String check = accountService.checkDuplicate(customer.getUsername(), customer.getMail());
        if (!"OK".equals(check)) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", check));
        }

        // Sinh OTP 6 số
        int otp = 100000 + new Random().nextInt(900000);

        // Gửi OTP qua mail
        mailService.sendOTP(customer.getMail(), otp);

        // Lưu tạm thông tin customer + otp vào session (hoặc Redis, hoặc Map static)
        pendingRegistrations.put(customer.getMail(), new VerifyCustomerDTO(customer, otp));

        return ResponseEntity.ok(Map.of("message", "OTP đã được gửi tới email, có hiệu lực trong 5 phút"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String mail, @RequestParam int otp) {
        VerifyCustomerDTO pending = pendingRegistrations.get(mail);

        if (pending == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy yêu cầu đăng ký"));
        }

        if (pending.isExpired()) {
            pendingRegistrations.remove(mail);
            return ResponseEntity.badRequest().body(Map.of("error", "OTP đã hết hạn"));
        }

        if (pending.getOtp() == otp) {
            Customer saved = customerService.addOrUpdateCustomer(pending.getCustomer());
            pendingRegistrations.remove(mail);
            
            mailService.sendWelcomeMail(saved.getMail());
            return ResponseEntity.ok(new CustomerDTO(saved));
        }

        return ResponseEntity.badRequest().body(Map.of("error", "OTP không hợp lệ"));
    }

}

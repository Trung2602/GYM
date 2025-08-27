/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.dto.AccountDTO;
import com.lht.dto.PasswordDTO;
import com.lht.pojo.Account;
import com.lht.services.AccountService;
import com.lht.jwt.JwtUtils;
import com.lht.services.CustomerService;
import com.lht.services.StaffService;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class ApiAccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private StaffService staffService;
    
    @Autowired
    private CustomerService customerService;

    // GET /api/accounts?username=abc&isActive=true
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts(@RequestParam Map<String, String> params) {
        List<Account> accounts = accountService.getAccounts(params);
        return ResponseEntity.ok(accounts);
    }

    // GET /api/accounts/all
    @GetMapping("/accounts/getAll")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // GET /api/account/{id}
    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Integer id) {
        Account a = accountService.getAccountById(id);
        if (a != null) {
            return ResponseEntity.ok(a);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/account
    @PostMapping("/account/update")
    public ResponseEntity<Account> updateAccount(@ModelAttribute("account") Account account,
            @RequestPart(value = "image", required = false) MultipartFile file) {
        if (account == null || account.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Load đúng entity từ DB
        Account acc = accountService.getAccountById(account.getId());
        if (acc == null) {
            return ResponseEntity.notFound().build();
        }

        // Copy dữ liệu từ client sang entity trong DB
        acc.setName(account.getName());
        acc.setMail(account.getMail());
        acc.setBirthday(account.getBirthday());
        acc.setGender(account.getGender());
        acc.setRole(account.getRole());
        acc.setIsActive(account.getIsActive());

        if (account.getPassword() != null && !account.getPassword().isEmpty()) {
            acc.setPassword(account.getPassword()); // service sẽ encode lại
        }

        if (file != null) {
            acc.setFile(file); // xử lý upload trong service
        }

        return ResponseEntity.ok(accountService.addOrUpdateAccount(acc));
    }

    // DELETE /api/account/{id}
    @DeleteMapping("/account/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Integer id) {
        boolean success = accountService.deleteAccount(id);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/account/isActive/{id}")
    public ResponseEntity<Account> changeIsActive(@PathVariable Integer id) {
        boolean success = accountService.changeIsActive(id);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //==============================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account u) {
        if (u.getUsername() == null || u.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username hoặc password không được để trống");
        }

        if (this.accountService.authenticate(u.getUsername(), u.getPassword())) {
            try {
                String token = JwtUtils.generateToken(u.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @GetMapping("/account/me")
    public ResponseEntity<?> getCurrentAccount(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }

        // principal.getName() chính là username trong token
        String username = principal.getName();

        Account acc = accountService.getAccountByUsername(username);
        if (acc == null) {
            return ResponseEntity.notFound().build();
        }
        AccountDTO dto = new AccountDTO(acc);
        if("Staff".equals(acc.getRole())){
            dto.setType(staffService.getStaffById(acc.getId()).getStaffTypeId().getName());
        }else{
            dto.setExpiryDate(customerService.getCustomerById(acc.getId()).getExpiryDate());
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody PasswordDTO request) {
        Account account = accountService.getAccountByUsername(request.getUsername());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại");
        }

        if (passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            return ResponseEntity.ok("Mật khẩu chính xác");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mật khẩu không đúng");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordDTO request) {
        Account account = accountService.getAccountByUsername(request.getUsername());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại");
        }

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mật khẩu cũ không đúng");
        }

        account.setPassword(request.getNewPassword());
        accountService.addOrUpdateAccount(account); // lưu vào DB

        return ResponseEntity.ok("Mật khẩu đã được cập nhật thành công");
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.pojo.Account;
import com.lht.services.AccountService;
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
public class ApiAccountController {
    @Autowired
    private AccountService accountService;

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
        if (a != null)
            return ResponseEntity.ok(a);
        else
            return ResponseEntity.notFound().build();
    }

    // POST /api/account
    @PostMapping("/account/addOrUpdate")
    public ResponseEntity<Account> addOrUpdateAccount(@RequestBody Account account) {
        Account saved = accountService.addOrUpdateAccount(account);
        return ResponseEntity.ok(saved);
    }

    // DELETE /api/account/{id}
    @DeleteMapping("/account/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Integer id) {
        boolean success = accountService.deleteAccount(id);
        if (success)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/account/isActive/{id}")
    public ResponseEntity<Account> changeIsActive(@PathVariable Integer id) {
        boolean success = accountService.changeIsActive(id);
        if (success)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }
}

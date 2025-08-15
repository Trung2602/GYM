/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.Account;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author admin
 */
public interface AccountService extends UserDetailsService{
    List<Account> getAccounts(Map<String, String> params);

    List<Account> getAllAccounts();

    Account getAccountById(Integer id);

    Account addOrUpdateAccount(Account p);

    boolean deleteAccount(Integer id);
    
    boolean changeIsActive (Integer id);
    
    public boolean authenticate(String username, String password);

    public Account getAccountByUsername(String username);    
}

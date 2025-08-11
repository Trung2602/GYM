/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Account;
import com.lht.pojo.Admin;
import com.lht.pojo.Customer;
import com.lht.pojo.Plan;
import com.lht.pojo.Staff;
import com.lht.reponsitories.AccountRepository;
import com.lht.reponsitories.AdminRepository;
import com.lht.services.AccountService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author admin
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public List<Account> getAccounts(Map<String, String> params) {
        Specification<Account> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.containsKey("username")) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + params.get("username").toLowerCase() + "%"));
            }

            if (params.containsKey("name")) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + params.get("name").toLowerCase() + "%"));
            }

            if (params.containsKey("mail")) {
                predicates.add(cb.like(cb.lower(root.get("mail")), "%" + params.get("mail").toLowerCase() + "%"));
            }

            if (params.containsKey("gender")) {
                predicates.add(cb.equal(root.get("gender"), Boolean.parseBoolean(params.get("gender"))));
            }

            if (params.containsKey("isActive")) {
                predicates.add(cb.equal(root.get("isActive"), Boolean.parseBoolean(params.get("isActive"))));
            }

            if (params.containsKey("birthday")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = df.parse(params.get("birthday"));
                    predicates.add(cb.equal(root.get("birthday"), date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (params.containsKey("isActive")) {
                predicates.add(cb.equal(root.get("isActive"), Boolean.parseBoolean(params.get("isActive"))));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return accountRepository.findAll(spec);
    }

    @Override
    public Account addOrUpdateAccount(Account a) {
        return accountRepository.save(a);
    }

    @Override
    public boolean deleteAccount(Integer id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean changeIsActive(Integer id) {
        Account acc = accountRepository.findById(id).orElse(null);
        if (acc != null) {
            if (acc.getIsActive()) {
                acc.setIsActive(Boolean.FALSE);
            } else {
                acc.setIsActive(Boolean.TRUE);
            }
            return true;
        }
        return false;

    }

    @Override
    public boolean authenticate(String username, String password) {
        Optional<Account> acc = this.accountRepository.findByUsername(username);
        if (acc.isPresent()) {
            Account user = acc.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    public Account getAccountByUsername(String username) {
        Optional<Account> user = this.accountRepository.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account acc = accountRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));
        
        if (acc == null) {
            throw new UsernameNotFoundException("Invalid username");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + acc.getRole()));

        return new org.springframework.security.core.userdetails.User(
                acc.getUsername(), acc.getPassword(), authorities);
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lht.pojo.Account;
import com.lht.pojo.Customer;
import com.lht.reponsitories.AccountRepository;
import com.lht.services.AccountService;
import com.lht.services.CustomerService;
import jakarta.persistence.criteria.Predicate;
import java.io.IOException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

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
        Account currentAccount = null;

        if (a.getId() != null) {
            // Tìm account có tồn tại không: Có -> uppdate // Không -> add
            currentAccount = this.accountRepository.findById(a.getId())
                    .orElse(null);
        }

        // Xử lý password
        if (a.getId() == null) {
            // Trường hợp thêm mới
            if (a.getRole() == null || a.getRole().isEmpty()) {
                a.setRole("Customer");
            }
            a.setPassword(this.passwordEncoder.encode(a.getPassword()));
        } else {
            // Trường hợp update
            if (a.getPassword() != null && !a.getPassword().isEmpty()) {
                // Nếu người dùng truyền password mới → encode lại
                a.setPassword(this.passwordEncoder.encode(a.getPassword()));
            } else if (currentAccount != null) {
                // Nếu không nhập password mới → giữ password cũ
                a.setPassword(currentAccount.getPassword());
            }
        }

        // Xử lý avatar
        if (a.getFile() != null && !a.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(a.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                a.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (currentAccount != null) {
                // Nếu update mà không đổi ảnh
                a.setAvatar(currentAccount.getAvatar());
            } else {
                // Nếu tạo mới mà không có ảnh
                a.setAvatar("https://res.cloudinary.com/dxgc9wwrd/image/upload/v1754928114/nzoi1xjxasxfvsut1azv.jpg");
            }
        }

        try {
            return accountRepository.save(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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

    @Override
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
        authorities.add(new SimpleGrantedAuthority("ROLE_" + acc.getRole().toUpperCase()));
        Boolean accountNonExpired = true;
        if ("Customer".equals(acc.getRole())) {
            Customer customer = customerService.getCustomerById(acc.getId());
            if (customer.getExpiryDate() != null && customer.getExpiryDate().before(new Date())) {
                accountNonExpired = false; // đã hết hạn
            }
        }

        return new org.springframework.security.core.userdetails.User(
                acc.getUsername(), //username
                acc.getPassword(), //password
                acc.getIsActive(), //enabled: có được kich hoạt chưa
                accountNonExpired, //expired: hết hạn chưa
                true,
                true,
                authorities);
    }
    
    @Override
    public String checkDuplicate(String username, String mail) {
        if (accountRepository.findByUsername(username).isPresent()) {
            return "Username đã tồn tại!";
        }
        if (accountRepository.findByMail(mail).isPresent()) {
            return "Mail đã tồn tại!";
        }
        return "OK";
    }
    
    @Override
    public Account getAccountByMail(String mail) {
        Optional<Account> user = this.accountRepository.findByMail(mail);
        return user.orElse(null);
    }
}

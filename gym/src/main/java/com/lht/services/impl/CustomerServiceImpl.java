/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lht.pojo.Customer;
import com.lht.reponsitories.CustomerRepository;
import com.lht.services.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author admin
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Customer> getCustomers(Map<String, String> params) {
        Specification<Customer> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return customerRepository.findAll(spec);
    }

    @Override
    public Customer addOrUpdateCustomer(Customer c) {
        if (c.getExpiryDate() == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date()); // ngày hiện tại
            cal.add(Calendar.DATE, 1); // cộng 1 ngày
            c.setExpiryDate(cal.getTime()); // set lại
        }
        Customer currentAccount = null;
        if (c.getId() != null) {
            currentAccount = this.getCustomerById(c.getId());
        }
        if (c.getId() == null) {
            // Trường hợp thêm mới: mã hóa password bắt buộc
            c.setRole("Customer");
            c.setPassword(this.passwordEncoder.encode(c.getPassword()));
        } else {
            // Trường hợp update: kiểm tra nếu password khác null và khác rỗng, encode lại
            c.setRole(c.getRole());
            if (c.getPassword() != null && !c.getPassword().isEmpty()) {
                // Có thể kiểm tra nếu khác mật khẩu hiện tại mới encode (tùy logic)
                c.setPassword(this.passwordEncoder.encode(c.getPassword()));
            } else {
                // Giữ nguyên password cũ
                c.setPassword(currentAccount.getPassword());
            }
        }
        if (c.getFile() != null && !c.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(c.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                c.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(AdminServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (currentAccount != null) {
                // Update mà không đổi ảnh
                c.setAvatar(currentAccount.getAvatar());
            } else {
                // Tạo mới mà không có ảnh
                c.setAvatar("https://res.cloudinary.com/dxgc9wwrd/image/upload/v1754928114/nzoi1xjxasxfvsut1azv.jpg");
            }
        }

        try {
            return customerRepository.save(c);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean deleteCustomer(Integer id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Customer> getCustomerByName(String name) {
        if (name == null || name.isEmpty()) {
            return Optional.empty(); // trả về empty nếu name rỗng
        }
        return this.customerRepository.findByName(name);
    }
}

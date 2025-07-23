/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Customer;
import com.lht.reponsitories.CustomerRepository;
import com.lht.services.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.Predicate;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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

            String quantitySauna = params.get("quantitySauna");
            if (quantitySauna != null) {
                predicates.add(cb.equal(root.get("quantitySauna"), Integer.parseInt(quantitySauna)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return customerRepository.findAll(spec);
    }

    @Override
    public Customer addOrUpdateCustomer(Customer c) {
        if (c.getExpiryDate() == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date()); // ngày hiện tại
            cal.add(Calendar.MONTH, 1); // cộng 1 tháng
            c.setExpiryDate(cal.getTime()); // set lại
        }
        if (c.getQuantitySauna() == null) {
            c.setQuantitySauna(0);
        }

        return customerRepository.save(c);
    }

    @Override
    public boolean deleteCustomer(Integer id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

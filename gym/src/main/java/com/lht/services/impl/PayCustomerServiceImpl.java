/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.PayCustomer;
import com.lht.reponsitories.PayCustomerRepository;
import com.lht.services.PayCustomerService;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class PayCustomerServiceImpl implements PayCustomerService {

    @Autowired

    private PayCustomerRepository payCustomerRepository;

    @Override
    public List<PayCustomer> getAllPayCustomers() {
        return payCustomerRepository.findAll();
    }

    @Override
    public PayCustomer getPayCustomerById(Integer id) {
        return payCustomerRepository.findById(id).orElse(null);
    }

    @Override
    public List<PayCustomer> getPayCustomers(Map<String, String> params) {
        Specification<PayCustomer> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("customerId")) {
                predicates.add(cb.equal(root.get("customer").get("id"), Integer.parseInt(params.get("customerId"))));
            }
            if (params.containsKey("planId")) {
                predicates.add(cb.equal(root.get("plan").get("id"), Integer.parseInt(params.get("planId"))));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return payCustomerRepository.findAll(spec);
    }

    @Override
    public PayCustomer addOrUpdatePayCustomer(PayCustomer p) {
        return payCustomerRepository.save(p);
    }

    @Override
    public boolean deletePayCustomer(Integer id) {
        if (payCustomerRepository.existsById(id)) {
            payCustomerRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

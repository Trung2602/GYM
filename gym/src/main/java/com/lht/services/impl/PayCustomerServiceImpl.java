/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.PayCustomer;
import com.lht.reponsitories.PayCustomerRepository;
import com.lht.services.PayCustomerService;

import jakarta.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                predicates.add(cb.equal(root.get("customerId").get("id"), Integer.parseInt(params.get("customerId"))));
            }

            if (params.containsKey("planId")) {
                predicates.add(cb.equal(root.get("planId").get("id"), Integer.parseInt(params.get("planId"))));
            }

            if (params.containsKey("price")) {
                try {
                    Integer price = Integer.parseInt(params.get("price"));
                    predicates.add(cb.equal(root.get("planId").get("price"), price));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid price format, expected a number");
                }
            }

            if (params.containsKey("date")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = df.parse(params.get("date"));
                    predicates.add(cb.equal(root.get("date"), date));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid date format, expected yyyy-MM-dd");
                }
            }

            if (params.containsKey("startDate") && params.containsKey("endDate")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = df.parse(params.get("startDate"));
                    Date end = df.parse(params.get("endDate"));
                    predicates.add(cb.between(root.get("date"), start, end));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid date format");
                }
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

    @Override
    public Page<PayCustomer> getAllSort(String sortField, String sortDir, int page, int size) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return payCustomerRepository.findAll(pageable);
    }
    
    @Override
    public List<PayCustomer> getPayCustomerByCustomerId(Integer id) {
        return this.payCustomerRepository.findByCustomerId_Id(id);
    }
}

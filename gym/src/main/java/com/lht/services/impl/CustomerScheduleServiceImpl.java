/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.CustomerSchedule;
import com.lht.reponsitories.CustomerScheduleRepository;
import com.lht.services.CustomerScheduleService;
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
public class CustomerScheduleServiceImpl implements CustomerScheduleService {

    @Autowired
    private CustomerScheduleRepository customerScheduleRepository;

    @Override
    public List<CustomerSchedule> getAllCustomerSchedules() {
        return customerScheduleRepository.findAll();
    }

    @Override
    public CustomerSchedule getCustomerScheduleById(Integer id) {
        return customerScheduleRepository.findById(id).orElse(null);
    }

    @Override
    public List<CustomerSchedule> getCustomerSchedules(Map<String, String> params) {
        Specification<CustomerSchedule> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("customerId")) {
                predicates.add(cb.equal(root.get("customer").get("id"), Integer.parseInt(params.get("customerId"))));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return customerScheduleRepository.findAll(spec);
    }

    @Override
    public CustomerSchedule addOrUpdateCustomerSchedule(CustomerSchedule s) {
        return customerScheduleRepository.save(s);
    }

    @Override
    public boolean deleteCustomerSchedule(Integer id) {
        if (customerScheduleRepository.existsById(id)) {
            customerScheduleRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

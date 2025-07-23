/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Salary;
import com.lht.reponsitories.SalaryRepository;
import com.lht.services.SalaryService;

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
public class SalaryServiceImpl implements SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    @Override
    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }

    @Override
    public Salary getSalaryById(Integer id) {
        return salaryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Salary> getSalaries(Map<String, String> params) {
        Specification<Salary> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("staffId")) {
                predicates.add(cb.equal(root.get("staff").get("id"), Integer.parseInt(params.get("staffId"))));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return salaryRepository.findAll(spec);
    }

    @Override
    public Salary addOrUpdateSalary(Salary s) {
        return salaryRepository.save(s);
    }

    @Override
    public boolean deleteSalary(Integer id) {
        if (salaryRepository.existsById(id)) {
            salaryRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

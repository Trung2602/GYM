/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Staff;
import com.lht.reponsitories.StaffRepository;
import com.lht.services.StaffService;
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
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public List<Staff> getAllStaffs() {
        return staffRepository.findAll();
    }

    @Override
    public Staff getStaffById(Integer id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public List<Staff> getStaffs(Map<String, String> params) {
        Specification<Staff> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("name")) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + params.get("name").toLowerCase() + "%"));
            }
            if (params.containsKey("phone")) {
                predicates.add(cb.like(root.get("phone"), "%" + params.get("phone") + "%"));
            }
            if (params.containsKey("position")) {
                predicates.add(cb.equal(root.get("position"), params.get("position")));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return staffRepository.findAll(spec);
    }

    @Override
    public Staff addOrUpdateStaff(Staff s) {
        return staffRepository.save(s);
    }

    @Override
    public boolean deleteStaff(Integer id) {
        if (staffRepository.existsById(id)) {
            staffRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

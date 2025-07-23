/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.StaffSchedule;
import com.lht.reponsitories.StaffScheduleRepository;
import com.lht.services.StaffScheduleService;
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
public class StaffScheduleServiceImpl implements StaffScheduleService {

    @Autowired
    private StaffScheduleRepository staffScheduleRepository;

    @Override
    public List<StaffSchedule> getAllStaffSchedules() {
        return staffScheduleRepository.findAll();
    }

    @Override
    public StaffSchedule getStaffScheduleById(Integer id) {
        return staffScheduleRepository.findById(id).orElse(null);
    }

    @Override
    public List<StaffSchedule> getStaffSchedules(Map<String, String> params) {
        Specification<StaffSchedule> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("staffId")) {
                predicates.add(cb.equal(root.get("staff").get("id"), Integer.parseInt(params.get("staffId"))));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return staffScheduleRepository.findAll(spec);
    }

    @Override
    public StaffSchedule addOrUpdateStaffSchedule(StaffSchedule s) {
        return staffScheduleRepository.save(s);
    }

    @Override
    public boolean deleteStaffSchedule(Integer id) {
        if (staffScheduleRepository.existsById(id)) {
            staffScheduleRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

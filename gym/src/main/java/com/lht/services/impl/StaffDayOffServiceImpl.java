/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.StaffDayOff;
import com.lht.reponsitories.StaffDayOffRepository;
import com.lht.services.StaffDayOffService;
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
public class StaffDayOffServiceImpl implements StaffDayOffService {

    @Autowired
    private StaffDayOffRepository staffDayOffRepository;

    @Override
    public List<StaffDayOff> getAllStaffDayOffs() {
        return staffDayOffRepository.findAll();
    }

    @Override
    public StaffDayOff getStaffDayOffById(Integer id) {
        return staffDayOffRepository.findById(id).orElse(null);
    }

    @Override
    public List<StaffDayOff> getStaffDayOffs(Map<String, String> params) {
        Specification<StaffDayOff> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("staffId")) {
                predicates.add(cb.equal(root.get("staff").get("id"), Integer.parseInt(params.get("staffId"))));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return staffDayOffRepository.findAll(spec);
    }

    @Override
    public StaffDayOff addOrUpdateStaffDayOff(StaffDayOff s) {
        return staffDayOffRepository.save(s);
    }

    @Override
    public boolean deleteStaffDayOff(Integer id) {
        if (staffDayOffRepository.existsById(id)) {
            staffDayOffRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Shift;
import com.lht.reponsitories.ShiftRepository;
import com.lht.services.ShiftService;
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
public class ShiftServiceImpl implements ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Override
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    @Override
    public Shift getShiftById(Integer id) {
        return shiftRepository.findById(id).orElse(null);
    }

    @Override
    public List<Shift> getShifts(Map<String, String> params) {
        Specification<Shift> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("duration")) {
                predicates.add(cb.equal(root.get("duration"), Integer.parseInt(params.get("duration"))));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return shiftRepository.findAll(spec);
    }

    @Override
    public Shift addOrUpdateShift(Shift s) {
        return shiftRepository.save(s);
    }

    @Override
    public boolean deleteShift(Integer id) {
        if (shiftRepository.existsById(id)) {
            shiftRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

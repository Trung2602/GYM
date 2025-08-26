/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Shift;
import com.lht.reponsitories.ShiftRepository;
import com.lht.services.ShiftService;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
            if (params.containsKey("checkin")) {
                try {
                    LocalTime time = LocalTime.parse(params.get("checkin")); // HH:mm:ss
                    predicates.add(cb.equal(root.get("checkin"), time));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid checkin format, expected HH:mm:ss");
                }
            }

            if (params.containsKey("checkout")) {
                try {
                    LocalTime time = LocalTime.parse(params.get("checkout")); // HH:mm:ss
                    predicates.add(cb.equal(root.get("checkout"), time));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid checkout format, expected HH:mm:ss");
                }
            }

            if (params.containsKey("duration")) {
                try {
                    predicates.add(cb.equal(root.get("duration"), Integer.parseInt(params.get("duration"))));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid duration, expected a number");
                }
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

    @Override
    public Page<Shift> getAllSort(String sortField, String sortDir, int page, int size) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return shiftRepository.findAll(pageable);
    }

}

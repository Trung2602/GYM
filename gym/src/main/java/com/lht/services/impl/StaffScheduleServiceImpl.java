/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.StaffSchedule;
import com.lht.reponsitories.StaffScheduleRepository;
import com.lht.services.StaffScheduleService;
import jakarta.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
            // Lọc theo staffId
            if (params.containsKey("staffId")) {
                try {
                    Integer staffId = Integer.parseInt(params.get("staffId"));
                    predicates.add(cb.equal(root.get("staffId").get("id"), staffId));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid staffId, expected a number");
                }
            }

            // Lọc theo shiftId
            if (params.containsKey("shiftId")) {
                try {
                    Integer shiftId = Integer.parseInt(params.get("shiftId"));
                    predicates.add(cb.equal(root.get("shiftId").get("id"), shiftId));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid shiftId, expected a number");
                }
            }

            // Lọc theo date
            if (params.containsKey("date")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = df.parse(params.get("date"));
                    predicates.add(cb.equal(root.get("date"), date));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid date format, expected yyyy-MM-dd");
                }
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

    @Override
    public List<StaffSchedule> getAllSort(String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return staffScheduleRepository.findAll(sort);
    }
}

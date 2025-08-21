/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.StaffDayOff;
import com.lht.reponsitories.StaffDayOffRepository;
import com.lht.services.StaffDayOffService;
import jakarta.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
            // Lọc theo staffId
            if (params.containsKey("staffId")) {
                try {
                    Integer staffId = Integer.parseInt(params.get("staffId"));
                    predicates.add(cb.equal(root.get("staffId").get("id"), staffId));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid staffId. Expected a number.");
                }
            }

            // Lọc theo date
            if (params.containsKey("date")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = df.parse(params.get("date"));
                    predicates.add(cb.equal(root.get("date"), date));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd.");
                }
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

    @Override
    public List<StaffDayOff> getAllSort(String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return staffDayOffRepository.findAll(sort);
    }

    @Override
    public int countByStaffIdAndMonthYear(int staffId, int month, int year) {
        List<StaffDayOff> allDayOffs = staffDayOffRepository.findByStaffId_Id(staffId);

        return (int) allDayOffs.stream() //từ List thành Stream để thao tác kiểu hàm functional
                .filter(d -> {
                    LocalDate localDate = ((java.sql.Date) d.getDate()).toLocalDate();
                    return localDate.getMonthValue() == month && localDate.getYear() == year;
                })
                .count();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Staff;
import com.lht.reponsitories.StaffRepository;
import com.lht.services.StaffService;
import jakarta.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            // Lọc theo createdDate
            if (params.containsKey("createdDate")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date createdDate = df.parse(params.get("createdDate"));
                    predicates.add(cb.equal(root.get("createdDate"), createdDate));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid createdDate format. Expected yyyy-MM-dd");
                }
            }

            // Lọc theo facilityId
            if (params.containsKey("facilityId")) {
                try {
                    predicates.add(cb.equal(root.get("facilityId").get("id"), Integer.parseInt(params.get("facilityId"))));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid facilityId. Expected number.");
                }
            }

            // Lọc theo staffTypeId
            if (params.containsKey("staffTypeId")) {
                try {
                    predicates.add(cb.equal(root.get("staffTypeId").get("id"), Integer.parseInt(params.get("staffTypeId"))));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid staffTypeId. Expected number.");
                }
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

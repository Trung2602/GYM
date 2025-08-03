/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.StaffType;
import com.lht.reponsitories.StaffTypeRepository;
import com.lht.services.StaffTypeService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
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
public class StaffTypeServiceImpl implements StaffTypeService {

    @Autowired
    private StaffTypeRepository staffTypeRepository;

    @Override
    public List<StaffType> getAllStaffTypes() {
        return staffTypeRepository.findAll();
    }

    @Override
    public StaffType getStaffTypeById(Integer id) {
        return staffTypeRepository.findById(id).orElse(null);
    }

    @Override
    public List<StaffType> getStaffTypes(Map<String, String> params) {
        Specification<StaffType> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo name (LIKE, không phân biệt hoa thường)
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // Lọc theo salary (chính xác)
            if (params.containsKey("salary")) {
                try {
                    Integer salary = Integer.parseInt(params.get("salary"));
                    predicates.add(cb.equal(root.get("salary"), salary));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid salary, must be a number");
                }
            }

            // Lọc theo dayOff (chính xác)
            if (params.containsKey("dayOff")) {
                try {
                    Integer dayOff = Integer.parseInt(params.get("dayOff"));
                    predicates.add(cb.equal(root.get("dayOff"), dayOff));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid dayOff, must be a number");
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return staffTypeRepository.findAll(spec);
    }

    @Override
    public StaffType addOrUpdateStaffType(StaffType st) {
        if (st.getName() == null || st.getName().isEmpty()) {
            st.setName("Loại chưa đặt tên");
        }
        if (st.getSalary() == null || st.getSalary() <= 0) {
            st.setSalary(1000000);
        }
        if (st.getDayOff() == null) {
            st.setDayOff(0);
        }

        return staffTypeRepository.save(st);
    }

    @Override
    public boolean deleteStaffType(Integer id) {
        if (staffTypeRepository.existsById(id)) {
            staffTypeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<StaffType> getAllSort(String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return staffTypeRepository.findAll(sort);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Facility;
import com.lht.reponsitories.FacilityRepository;
import com.lht.services.FacilityService;
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
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Override
    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    @Override
    public Facility getFacilityById(Integer id) {
        return facilityRepository.findById(id).orElse(null);
    }

    @Override
    public List<Facility> getFacilities(Map<String, String> params) {
        Specification<Facility> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            String address = params.get("address");
            if (address != null && !address.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return facilityRepository.findAll(spec);
    }

    @Override
    public Facility addOrUpdateFacility(Facility f) {
        if (f.getName() == null || f.getName().isEmpty()) {
            f.setName("Cơ sở chưa đặt tên");
        }
        if (f.getAddress() == null || f.getAddress().isEmpty()) {
            f.setAddress("Địa chỉ chưa xác định");
        }

        return facilityRepository.save(f);
    }

    @Override
    public boolean deleteFacility(Integer id) {
        if (facilityRepository.existsById(id)) {
            facilityRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

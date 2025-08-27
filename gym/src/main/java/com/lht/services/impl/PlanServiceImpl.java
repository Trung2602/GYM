/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Plan;
import com.lht.reponsitories.PlanRepository;
import com.lht.services.PlanService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.Predicate;
import java.util.Optional;
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
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanRepository planRepository;

    @Override
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    @Override
    public Plan getPlanById(Integer id) {
        return planRepository.findById(id).orElse(null); // hoặc throw exception nếu không tìm thấy
    }

    @Override
    public List<Plan> getPlans(Map<String, String> params) {
        Specification<Plan> spec = (Root<Plan> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(cb.equal(root.get("id"), Integer.parseInt(id)));
            }

            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            String price = params.get("price");
            if (price != null && !price.isEmpty()) {
                predicates.add(cb.equal(root.get("price"), Double.parseDouble(price)));
            }

            String duration = params.get("durationDays");
            if (duration != null && !duration.isEmpty()) {
                predicates.add(cb.equal(root.get("durationDays"), Integer.parseInt(duration)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return planRepository.findAll(spec);
    }

    @Override
    public Plan addOrUpdatePlan(Plan p) {
        // Mặc định nếu thiếu thông tin
        if (p.getName() == null || p.getName().isEmpty()) {
            p.setName("Gói cơ bản");
        }
        if (p.getDescription() == null || p.getDescription().isEmpty()) {
            p.setDescription("Gói tập cơ bản cho người mới");
        }
        if (p.getPrice() == null || p.getPrice() <= 0) {
            p.setPrice(300000);
        }
        if (p.getDurationDays() == null || p.getDurationDays() <= 0) {
            p.setDurationDays(30);
        }

        return planRepository.save(p);
    }

    @Override
    public boolean deletePlan(Integer id) {
        if (planRepository.existsById(id)) {
            planRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<Plan> getAllSort(String sortField, String sortDir, int page, int size) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return planRepository.findAll(pageable);
    }
    
    @Override
    public Optional<Plan> getPlanByName(String name) {
        return planRepository.findByName(name);
    }
}

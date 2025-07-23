/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Sauna;
import com.lht.reponsitories.SaunaRepository;
import com.lht.services.SaunaService;
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
public class SaunaServiceImpl implements SaunaService {

    @Autowired
    private SaunaRepository saunaRepository;

    @Override
    public List<Sauna> getAllSaunas() {
        return saunaRepository.findAll();
    }

    @Override
    public Sauna getSaunaById(Integer id) {
        return saunaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Sauna> getSaunas(Map<String, String> params) {
        Specification<Sauna> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("customerId")) {
                predicates.add(cb.equal(root.get("customer").get("id"), Integer.parseInt(params.get("customerId"))));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return saunaRepository.findAll(spec);
    }

    @Override
    public Sauna addOrUpdateSauna(Sauna s) {
        return saunaRepository.save(s);
    }

    @Override
    public boolean deleteSauna(Integer id) {
        if (saunaRepository.existsById(id)) {
            saunaRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

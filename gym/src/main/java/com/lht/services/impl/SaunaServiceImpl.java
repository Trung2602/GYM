/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Sauna;
import com.lht.reponsitories.SaunaRepository;
import com.lht.services.SaunaService;
import jakarta.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
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
            if (params.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), Integer.parseInt(params.get("id"))));
            }

            if (params.containsKey("date")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = df.parse(params.get("date"));
                    predicates.add(cb.equal(root.get("date"), date));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid date format, expected yyyy-MM-dd");
                }
            }

            if (params.containsKey("checkin")) {
                try {
                    LocalTime time = LocalTime.parse(params.get("checkin")); // format: HH:mm:ss
                    predicates.add(cb.equal(root.get("checkin"), time));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid time format, expected HH:mm:ss");
                }
            }

            if (params.containsKey("isPaid")) {
                Boolean isPaid = Boolean.parseBoolean(params.get("isPaid"));
                predicates.add(cb.equal(root.get("isPaid"), isPaid));
            }

            if (params.containsKey("customerId")) {
                predicates.add(cb.equal(root.get("customerId").get("id"), Integer.parseInt(params.get("customerId"))));
            }

            if (params.containsKey("roomId")) {
                predicates.add(cb.equal(root.get("roomId").get("id"), Integer.parseInt(params.get("roomId"))));
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
    
    @Override
    public List<Sauna> getAllSort(String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return saunaRepository.findAll(sort);
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.CustomerSchedule;
import com.lht.reponsitories.CustomerScheduleRepository;
import com.lht.services.CustomerScheduleService;
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
public class CustomerScheduleServiceImpl implements CustomerScheduleService {

    @Autowired
    private CustomerScheduleRepository customerScheduleRepository;

    @Override
    public List<CustomerSchedule> getAllCustomerSchedules() {
        return customerScheduleRepository.findAll();
    }

    @Override
    public CustomerSchedule getCustomerScheduleById(Integer id) {
        return customerScheduleRepository.findById(id).orElse(null);
    }

    @Override
    public List<CustomerSchedule> getCustomerSchedules(Map<String, String> params) {
        Specification<CustomerSchedule> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), Integer.parseInt(params.get("id"))));
            }

            if (params.containsKey("customerId")) {
                predicates.add(cb.equal(root.get("customerId").get("id"), Integer.parseInt(params.get("customerId"))));
            }

            if (params.containsKey("staffId")) {
                predicates.add(cb.equal(root.get("staffId").get("id"), Integer.parseInt(params.get("staffId"))));
            }

            if (params.containsKey("facilityId")) {
                predicates.add(cb.equal(root.get("facility").get("id"), Integer.parseInt(params.get("facilityId"))));
            }

            if (params.containsKey("date")) {
                //format yyyy-MM-dd
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
                    LocalTime checkin = LocalTime.parse(params.get("checkin")); // format HH:mm:ss
                    predicates.add(cb.equal(root.get("checkin"), checkin));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid checkin format, expected HH:mm:ss");
                }
            }

            if (params.containsKey("checkout")) {
                try {
                    LocalTime checkout = LocalTime.parse(params.get("checkout")); // format HH:mm:ss
                    predicates.add(cb.equal(root.get("checkout"), checkout));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid checkout format, expected HH:mm:ss");
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return customerScheduleRepository.findAll(spec);
    }

    @Override
    public CustomerSchedule addOrUpdateCustomerSchedule(CustomerSchedule s) {
        return customerScheduleRepository.save(s);
    }

    @Override
    public boolean deleteCustomerSchedule(Integer id) {
        if (customerScheduleRepository.existsById(id)) {
            customerScheduleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<CustomerSchedule> getAllSort(String sortField, String sortDir, int page, int size) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return customerScheduleRepository.findAll(pageable);
    }

    @Override
    public List<CustomerSchedule> getCustomerScheduleByCustomerId(Integer id) {
        return this.customerScheduleRepository.findByCustomerId_Id(id);
    }

    @Override
    public List<CustomerSchedule> getCustomerScheduleByStaffId(Integer id) {
        return this.customerScheduleRepository.findByStaffId_Id(id);
    }
}

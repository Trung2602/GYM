/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Customer;
import com.lht.pojo.PayCustomer;
import com.lht.reponsitories.CustomerRepository;
import com.lht.reponsitories.PayCustomerRepository;
import com.lht.services.PayCustomerService;

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
public class PayCustomerServiceImpl implements PayCustomerService {

    @Autowired
    private PayCustomerRepository payCustomerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<PayCustomer> getAllPayCustomers() {
        return payCustomerRepository.findAll();
    }

    @Override
    public PayCustomer getPayCustomerById(Integer id) {
        return payCustomerRepository.findById(id).orElse(null);
    }

    @Override
    public List<PayCustomer> getPayCustomers(Map<String, String> params) {
        Specification<PayCustomer> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("customerId")) {
                predicates.add(cb.equal(root.get("customerId").get("id"), Integer.parseInt(params.get("customerId"))));
            }

            if (params.containsKey("planId")) {
                predicates.add(cb.equal(root.get("planId").get("id"), Integer.parseInt(params.get("planId"))));
            }

            if (params.containsKey("price")) {
                try {
                    Integer price = Integer.parseInt(params.get("price"));
                    predicates.add(cb.equal(root.get("planId").get("price"), price));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid price format, expected a number");
                }
            }

            // Lọc theo txnRef
            if (params.containsKey("txnRef")) {
                predicates.add(cb.equal(root.get("txnRef"), params.get("txnRef")));
            }

            // Lọc theo status
            if (params.containsKey("status")) {
                predicates.add(cb.equal(root.get("status"), params.get("status")));
            }

            // Lọc theo bankCode
            if (params.containsKey("bankCode")) {
                predicates.add(cb.equal(root.get("bankCode"), params.get("bankCode")));
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

            if (params.containsKey("startDate") && params.containsKey("endDate")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = df.parse(params.get("startDate"));
                    Date end = df.parse(params.get("endDate"));
                    predicates.add(cb.between(root.get("date"), start, end));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid date format");
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return payCustomerRepository.findAll(spec);
    }

    @Override
    public PayCustomer addOrUpdatePayCustomer(PayCustomer p) {
        if (p.getId() == null) {
            // Trường hợp tạo mới
            if (p.getStatus() == null) {
                p.setStatus("PENDING");
                
            }
        } else {
            Optional<PayCustomer> payCustomer = payCustomerRepository.findById(p.getId());
            p.setBankCode(payCustomer.get().getBankCode());
            p.setTxnRef(payCustomer.get().getTxnRef());
            p.setStatus(payCustomer.get().getStatus());
        }
        return payCustomerRepository.save(p);

    }

    @Override
    public boolean deletePayCustomer(Integer id) {
        if (payCustomerRepository.existsById(id)) {
            payCustomerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<PayCustomer> getAllSort(String sortField, String sortDir, int page, int size) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return payCustomerRepository.findAll(pageable);
    }

    @Override
    public List<PayCustomer> getPayCustomerByCustomerId(Integer id) {
        return this.payCustomerRepository.findByCustomerId_Id(id);
    }

    @Override
    public void updatePaymentStatus(PayCustomer pay, String txnRef, String status, String bankCode) {
        if (pay != null) {
            pay.setTxnRef(txnRef);
            pay.setStatus(status);
            pay.setBankCode(bankCode);
            payCustomerRepository.save(pay);
        }
    }

    @Override
    public void updateExpiryDate(Integer payCustomerId) {
        Optional<PayCustomer> payOpt = this.payCustomerRepository.findById(payCustomerId);
        if (payOpt.isPresent()) {
            PayCustomer pay = payOpt.get();
            Optional<Customer> customerOpt = this.customerRepository.findById(pay.getCustomerId().getId());

            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();

                // Lấy ngày hiện tại
                LocalDate now = LocalDate.now();

                // Lấy ngày hết hạn hiện tại
                Date expiry = customer.getExpiryDate();
                LocalDate expiryDate = expiry != null
                        ? expiry.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : now;

                // Số ngày của gói
                int durationDays = pay.getPlanId().getDurationDays();

                // Tính ngày hết hạn mới
                LocalDate newExpiryDate = expiryDate.isBefore(now) ? now.plusDays(durationDays)
                        : expiryDate.plusDays(durationDays);

                // Cập nhật và lưu lại
                customer.setExpiryDate(Date.from(newExpiryDate.atStartOfDay(ZoneId.from(expiryDate).systemDefault()).toInstant()));
                this.customerRepository.save(customer);
            }
        }
    }

}

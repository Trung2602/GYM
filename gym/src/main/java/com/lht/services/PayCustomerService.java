/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.PayCustomer;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

/**
 *
 * @author admin
 */
public interface PayCustomerService {

    List<PayCustomer> getPayCustomers(Map<String, String> params);

    List<PayCustomer> getAllPayCustomers();

    PayCustomer getPayCustomerById(Integer id);

    PayCustomer addOrUpdatePayCustomer(PayCustomer pc);

    boolean deletePayCustomer(Integer id);
    
    public Page<PayCustomer> getAllSort(String sortField, String sortDir, int page, int size);
    
    public List<PayCustomer> getPayCustomerByCustomerId(Integer id);
}

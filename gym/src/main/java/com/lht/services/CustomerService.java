/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.Customer;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface CustomerService {

    List<Customer> getCustomers(Map<String, String> params);

    List<Customer> getAllCustomers();

    Customer getCustomerById(Integer id);

    Customer addOrUpdateCustomer(Customer c);

    boolean deleteCustomer(Integer id);

}

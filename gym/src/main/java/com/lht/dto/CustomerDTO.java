/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.dto;

import com.lht.pojo.Customer;
import java.util.Date;

/**
 *
 * @author admin
 */
public class CustomerDTO extends AccountDTO{
    
    private Date expiryDate;

    public CustomerDTO(Customer customer) {
        super(customer); // Gán các field của AccountDTO
        if (customer != null) {
            this.expiryDate = customer.getExpiryDate();
        }
    }

    // Getter & Setter
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}

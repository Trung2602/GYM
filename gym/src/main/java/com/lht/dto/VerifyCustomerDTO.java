/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.dto;

import com.lht.pojo.Customer;
import java.time.LocalDateTime;

/**
 *
 * @author admin
 */
public class VerifyCustomerDTO {

    private Customer customer;
    private int otp;
    private LocalDateTime expiryTime;
    
    public VerifyCustomerDTO() {
    }

    public VerifyCustomerDTO(Customer customer, int otp) {
        this.customer = customer;
        this.otp = otp;
        this.expiryTime = LocalDateTime.now().plusMinutes(5);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }
    
    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }
}

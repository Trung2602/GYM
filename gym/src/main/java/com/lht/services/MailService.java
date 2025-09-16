/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

/**
 *
 * @author admin
 */
public interface MailService {
    public void sendPaymentSuccess(String mail, String payName);
    
    public void sendOTP(String mail, int otp);
    
    public void sendWelcomeMail(String mail);
}

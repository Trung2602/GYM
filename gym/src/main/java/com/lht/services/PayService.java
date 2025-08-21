/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author admin
 */
public interface PayService {
    public String createPaymentUrl(long amount, String orderInfo) throws UnsupportedEncodingException;
}

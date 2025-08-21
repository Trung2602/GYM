/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.services.PayService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api/payment")
public class PayController {

    @Autowired
    private PayService payService;

    public PayController(PayService payService) {
        this.payService = payService;
    }

    @GetMapping("/create")
    public ResponseEntity<?> createPayment(@RequestParam long amount, @RequestParam String orderInfo) {
        try {
            String url = payService.createPaymentUrl(amount, orderInfo);
            return ResponseEntity.ok(Map.of("paymentUrl", url));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/vnpay_return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> params) {
        // TODO: xác minh chữ ký (vnp_SecureHash) và xử lý kết quả
        return ResponseEntity.ok(params);
    }
}

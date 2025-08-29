/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.configs.PaymentConfig;
import com.lht.pojo.PayCustomer;
import com.lht.services.CustomerService;
import com.lht.services.PayCustomerService;
import com.lht.services.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayCustomerService payCustomerService;

    @Autowired
    private PlanService planService;

    @Autowired
    private CustomerService customerService;

    private final PaymentConfig vnpConfig;

    public PaymentController(PaymentConfig vnpConfig) {
        this.vnpConfig = vnpConfig;
    }

    @GetMapping("/create")
    public String createPayment(@RequestParam("pay-customer-id") Integer id,
            HttpServletRequest request) throws UnsupportedEncodingException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = request.getRemoteAddr();
        String orderType = "170000"; // Thương mại điện tử

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnpConfig.getVnp_TmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(payCustomerService.getPayCustomerById(id).getPlanId().getPrice() * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan : " + payCustomerService.getPayCustomerById(id).getPlanId().getName());
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime()));

        String returnUrl = "http://localhost:8080/payment/return?pay_customer_id=" + id;
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        //vnp_Params.put("pay_customer_id", String.valueOf(id));

        // Sort params
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append('&');
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append('&');
            }
        }
        hashData.deleteCharAt(hashData.length() - 1);
        query.deleteCharAt(query.length() - 1);

        String vnp_SecureHash = hmacSHA512(vnpConfig.getVnp_HashSecret(), hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = vnpConfig.getVnp_Url() + "?" + query.toString();

        Map<String, String> result = new HashMap<>();
        result.put("paymentUrl", paymentUrl);

        return "redirect:" + paymentUrl;
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    @GetMapping("/return")
    public String handleReturn(@RequestParam Map<String, String> params, HttpServletResponse response) throws Exception {
        String vnp_SecureHash = params.get("vnp_SecureHash");

        // --- Tính chữ ký lại ---
        Map<String, String> sortedParams = new TreeMap<>(params);
        sortedParams.remove("vnp_SecureHash");

        StringBuilder signData = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (entry.getKey().startsWith("vnp_")) {
                signData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()))
                        .append("&");
            }
        }
        signData.deleteCharAt(signData.length() - 1);
        String calculatedHash = hmacSHA512(vnpConfig.getVnp_HashSecret(), signData.toString());

        String frontendUrl = "http://localhost:8080/pay-customers";

        if (calculatedHash.equals(vnp_SecureHash)) {
            // --- Xác định trạng thái ---
            String status = "FAILED";
            if ("00".equals(params.get("vnp_ResponseCode"))) {
                status = "SUCCESS";
            }
            //lấy giờ giao dịch
            String createDateStr = params.get("vnp_CreateDate");
            Date createDate = null;

            if (createDateStr != null && !createDateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                createDate = sdf.parse(createDateStr); // parse thành Date
            } else {
                createDate = new Date();
            }

            int pcId = Integer.parseInt(params.get("pay_customer_id"));
            // --- Tạo PayCustomer ---
            PayCustomer pc = this.payCustomerService.getPayCustomerById(pcId);
            pc.setDate(createDate);
            PayCustomer pay = this.payCustomerService.addOrUpdatePayCustomer(pc);
            if (pay != null) {
                String txnRef = params.get("vnp_TxnRef");
                String bankCode = params.get("vnp_BankCode");
                this.payCustomerService.updatePaymentStatus(pay, txnRef, status, bankCode);
                if ("SUCCESS".equals(status)) {
                    this.payCustomerService.updateExpiryDate(pcId);
                }
            }
            // --- Redirect về frontend ---
            return "redirect:" + frontendUrl;
        } else {
            return "redirect:" + frontendUrl;
        }
    }

}

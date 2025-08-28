/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.dto;

import com.lht.pojo.Customer;
import com.lht.pojo.PayCustomer;
import com.lht.pojo.Plan;
import java.util.Date;

/**
 *
 * @author admin
 */
public class PayCustomerDTO {

    private Integer id;
    private Date date;
    private String customerName;
    private String planName;
    private Integer price;

    private String txnRef;   // thêm mã giao dịch VNPAY
    private String status;   // PENDING / SUCCESS / FAILED
    private String bankCode; // optional, ngân hàng

    // Constructor không tham số
    public PayCustomerDTO() {
    }

    // Constructor đầy đủ
    public PayCustomerDTO(Integer id, Date date, String customerName, String planName, String txnRef, String status, Integer price, String bankCode) {
        this.id = id;
        this.date = date;
        this.customerName = customerName;
        this.planName = planName;
        this.txnRef = txnRef;
        this.status = status;
        this.bankCode = bankCode;
    }

    public PayCustomerDTO(PayCustomer entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.customerName = entity.getCustomerId() != null ? entity.getCustomerId().getName() : null;
        this.planName = entity.getPlanId() != null ? entity.getPlanId().getName() : null;
        this.price = entity.getPlanId() != null && entity.getPlanId() != null
                ? entity.getPlanId().getPrice()
                : null;
        this.txnRef = entity.getTxnRef();
        this.status = entity.getStatus();
        this.bankCode = entity.getBankCode();
    }

    public PayCustomer convertToEntity(PayCustomerDTO dto, Customer customer, Plan plan) {
        PayCustomer pc = new PayCustomer();
        pc.setDate(dto.getDate());
        pc.setCustomerId(customer);
        pc.setPlanId(plan);
        pc.setTxnRef(dto.getTxnRef());
        pc.setStatus(dto.getStatus());
        pc.setBankCode(dto.getBankCode());
        return pc;
    }

    // Getter & Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTxnRef() {
        return txnRef;
    }

    public void setTxnRef(String txnRef) {
        this.txnRef = txnRef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}

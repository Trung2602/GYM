/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.dto;

import com.lht.pojo.PayCustomer;
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

    // Constructor không tham số
    public PayCustomerDTO() {
    }

    // Constructor đầy đủ
    public PayCustomerDTO(Integer id, Date date, String customerName, String planName) {
        this.id = id;
        this.date = date;
        this.customerName = customerName;
        this.planName = planName;
    }

    public PayCustomerDTO(PayCustomer entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.customerName = entity.getCustomerId() != null ? entity.getCustomerId().getName(): null;
        this.planName = entity.getPlanId() != null ? entity.getPlanId().getName(): null;
        this.price = entity.getPlanId()!= null && entity.getPlanId()!= null 
        ? entity.getPlanId().getPrice()
        : null;
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
}

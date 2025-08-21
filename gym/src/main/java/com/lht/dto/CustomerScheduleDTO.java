/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.dto;

import com.lht.pojo.CustomerSchedule;
import java.time.LocalTime;
import java.util.Date;

/**
 *
 * @author admin
 */
public class CustomerScheduleDTO {

    private Integer id;
    private Date date;
    private LocalTime checkin;
    private LocalTime checkout;
    private String customerName;
    private String facilityName;
    private String staffName;

    public CustomerScheduleDTO() {}
     
    public CustomerScheduleDTO(CustomerSchedule cs) {
        this.id = cs.getId();
        this.date = cs.getDate();
        this.checkin = cs.getCheckin();
        this.checkout = cs.getCheckout();
        this.customerName = cs.getCustomerId() != null ? cs.getCustomerId().getName() : null;
        this.staffName = cs.getStaffId() != null ? cs.getStaffId().getName() : null;
        this.facilityName = cs.getStaffId() != null && cs.getStaffId().getFacilityId() != null 
        ? cs.getStaffId().getFacilityId().getName() 
        : null;
    }

    // getters
    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public LocalTime getCheckin() {
        return checkin;
    }

    public LocalTime getCheckout() {
        return checkout;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public String getStaffName() {
        return staffName;
    }

    // setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCheckin(LocalTime checkin) {
        this.checkin = checkin;
    }

    public void setCheckout(LocalTime checkout) {
        this.checkout = checkout;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}

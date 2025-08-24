/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.dto;

import com.lht.pojo.Salary;
import java.util.Date;

/**
 *
 * @author admin
 */
public class SalaryDTO {

    private Integer id;
    private Date date;
    private Double duration;
    private Integer dayOff;
    private Double price;
    private String staffName;

    public SalaryDTO() {
    }

    public SalaryDTO(Integer id, Date date, Double duration, Integer dayOff, Double price, Integer staffId, String staffName) {
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.dayOff = dayOff;
        this.price = price;
        this.staffName = staffName;
    }

    // Constructor từ entity Salary
    public SalaryDTO(Salary salary) {
        this.id = salary.getId();
        this.date = salary.getDate();
        this.duration = salary.getDuration();
        this.dayOff = salary.getDayOff();
        this.price = salary.getPrice();
        if (salary.getStaffId() != null) {
            this.staffName = salary.getStaffId().getName();
        }
    }

    // Getters và Setters
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

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Integer getDayOff() {
        return dayOff;
    }

    public void setDayOff(Integer dayOff) {
        this.dayOff = dayOff;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}

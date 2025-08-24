
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.dto;

import com.lht.pojo.StaffDayOff;
import java.util.Date;

/**
 *
 * @author admin
 */
public class StaffDayOffDTO {

    private Integer id;
    private Date date;
    private String staffName;

    public StaffDayOffDTO() {
    }

    public StaffDayOffDTO(Integer id, Date date, String staffName) {
        this.id = id;
        this.date = date;
        this.staffName = staffName;
    }

    // Constructor từ entity StaffDayOff
    public StaffDayOffDTO(StaffDayOff dayOff) {
        this.id = dayOff.getId();
        this.date = dayOff.getDate();
        this.staffName = dayOff.getStaffId() != null ? dayOff.getStaffId().getName() : null;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.dto;

import com.lht.pojo.StaffSchedule;
import java.time.LocalTime;
import java.util.Date;

/**
 *
 * @author admin
 */
public class StaffScheduleDTO {

    private Integer id;
    private Date date;
    private String staffName;   // tên nhân viên
    private String shiftName;   // tên ca làm việc
    private LocalTime checkIn;  // giờ bắt đầu ca
    private LocalTime checkOut; // giờ kết thúc ca

    public StaffScheduleDTO() {
    }

    public StaffScheduleDTO(Integer id, Date date, String staffName, String shiftName, LocalTime checkIn, LocalTime checkOut) {
        this.id = id;
        this.date = date;
        this.staffName = staffName;
        this.shiftName = shiftName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    // Constructor từ entity
    public StaffScheduleDTO(StaffSchedule schedule) {
        this.id = schedule.getId();
        this.date = schedule.getDate();
        this.staffName = schedule.getStaffId() != null ? schedule.getStaffId().getName() : null;
        if (schedule.getShiftId() != null) {
            this.shiftName = schedule.getShiftId().getName();
            this.checkIn = schedule.getShiftId().getCheckin();
            this.checkOut = schedule.getShiftId().getCheckout();
        }
    }

    // Getters & Setters
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

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }
}

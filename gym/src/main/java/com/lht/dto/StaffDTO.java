/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lht.pojo.Account;
import com.lht.pojo.Facility;
import com.lht.pojo.Staff;
import com.lht.pojo.StaffType;
import java.util.Date;

/**
 *
 * @author admin
 */
public class StaffDTO extends AccountDTO{
    private Date createdDate;
    private String facilityName;
    private String staffTypeName;
    
    public StaffDTO(Staff staff) {
        super(staff);
        this.createdDate = staff.getCreatedDate();
        this.facilityName = staff.getFacilityId().getName();
        this.staffTypeName = staff.getStaffTypeId().getName();
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getFacility() {
        return facilityName;
    }

    public void setFacility(String facility) {
        this.facilityName = facility;
    }

    public String getStaffType() {
        return staffTypeName;
    }

    public void setStaffType(String staffType) {
        this.staffTypeName = staffType;
    }
}

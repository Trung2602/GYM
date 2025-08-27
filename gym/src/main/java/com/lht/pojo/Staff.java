/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "staff")
@NamedQueries({
    @NamedQuery(name = "Staff.findAll", query = "SELECT s FROM Staff s"),
    @NamedQuery(name = "Staff.findById", query = "SELECT s FROM Staff s WHERE s.id = :id"),
    @NamedQuery(name = "Staff.findByCreatedDate", query = "SELECT s FROM Staff s WHERE s.createdDate = :createdDate")})
public class Staff extends Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;
    
    @OneToMany(mappedBy = "staffId")
    @JsonIgnore
    private Set<CustomerSchedule> customerScheduleSet;
    @OneToMany(mappedBy = "staffId")
    @JsonIgnore
    private Set<StaffSchedule> staffScheduleSet;
    @OneToMany(mappedBy = "staffId")
    @JsonIgnore
    private Set<StaffDayOff> staffDayOffSet;
    @OneToMany(mappedBy = "staffId")
    @JsonIgnore
    private Set<Salary> salarySet;
    
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    //@JsonIgnoreProperties({"staffSet"})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "staffSet"})
    private Facility facilityId;
    @JoinColumn(name = "staff_type_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    //@JsonIgnoreProperties({"staffSet"})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "staffSet"})
    private StaffType staffTypeId;
    
    public Staff() {
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Set<CustomerSchedule> getCustomerScheduleSet() {
        return customerScheduleSet;
    }

    public void setCustomerScheduleSet(Set<CustomerSchedule> customerScheduleSet) {
        this.customerScheduleSet = customerScheduleSet;
    }

    public Set<StaffSchedule> getStaffScheduleSet() {
        return staffScheduleSet;
    }

    public void setStaffScheduleSet(Set<StaffSchedule> staffScheduleSet) {
        this.staffScheduleSet = staffScheduleSet;
    }

    public Set<StaffDayOff> getStaffDayOffSet() {
        return staffDayOffSet;
    }

    public void setStaffDayOffSet(Set<StaffDayOff> staffDayOffSet) {
        this.staffDayOffSet = staffDayOffSet;
    }

    public Facility getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Facility facilityId) {
        this.facilityId = facilityId;
    }

    public StaffType getStaffTypeId() {
        return staffTypeId;
    }

    public void setStaffTypeId(StaffType staffTypeId) {
        this.staffTypeId = staffTypeId;
    }

    public Set<Salary> getSalarySet() {
        return salarySet;
    }

    public void setSalarySet(Set<Salary> salarySet) {
        this.salarySet = salarySet;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Staff)) {
//            return false;
//        }
//        Staff other = (Staff) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "com.lht.pojo.Staff[ id=" + id + " ]";
//    }
    
}

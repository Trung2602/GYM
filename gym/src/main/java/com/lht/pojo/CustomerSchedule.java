/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "customer_schedule")
@NamedQueries({
    @NamedQuery(name = "CustomerSchedule.findAll", query = "SELECT c FROM CustomerSchedule c"),
    @NamedQuery(name = "CustomerSchedule.findById", query = "SELECT c FROM CustomerSchedule c WHERE c.id = :id"),
    @NamedQuery(name = "CustomerSchedule.findByDate", query = "SELECT c FROM CustomerSchedule c WHERE c.date = :date"),
    @NamedQuery(name = "CustomerSchedule.findByCheckin", query = "SELECT c FROM CustomerSchedule c WHERE c.checkin = :checkin"),
    @NamedQuery(name = "CustomerSchedule.findByCheckout", query = "SELECT c FROM CustomerSchedule c WHERE c.checkout = :checkout")})
public class CustomerSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @Column(name = "checkin")
//    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime checkin;
    @Column(name = "checkout")
//    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime checkout;
    
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnoreProperties({"customerScheduleSet"})
    private Customer customerId;
    @JoinColumn(name = "facility_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnoreProperties({"customerScheduleSet"})
    private Facility facilityId;
    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnoreProperties({"customerScheduleSet"})
    private Staff staffId;

    public CustomerSchedule() {
    }

    public CustomerSchedule(Integer id) {
        this.id = id;
    }

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

    public LocalTime getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalTime checkin) {
        this.checkin = checkin;
    }

    public LocalTime getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalTime checkout) {
        this.checkout = checkout;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Facility getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Facility facilityId) {
        this.facilityId = facilityId;
    }

    public Staff getStaffId() {
        return staffId;
    }

    public void setStaffId(Staff staffId) {
        this.staffId = staffId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerSchedule)) {
            return false;
        }
        CustomerSchedule other = (CustomerSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lht.pojo.CustomerSchedule[ id=" + id + " ]";
    }
    
}

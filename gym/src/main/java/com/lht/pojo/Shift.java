/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "shift")
@NamedQueries({
    @NamedQuery(name = "Shift.findAll", query = "SELECT s FROM Shift s"),
    @NamedQuery(name = "Shift.findById", query = "SELECT s FROM Shift s WHERE s.id = :id"),
    @NamedQuery(name = "Shift.findByCheckin", query = "SELECT s FROM Shift s WHERE s.checkin = :checkin"),
    @NamedQuery(name = "Shift.findByCheckout", query = "SELECT s FROM Shift s WHERE s.checkout = :checkout"),
    @NamedQuery(name = "Shift.findByDuration", query = "SELECT s FROM Shift s WHERE s.duration = :duration")})
public class Shift implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "checkin")
    @Temporal(TemporalType.TIME)
    private LocalTime checkin;
    @Column(name = "checkout")
    @Temporal(TemporalType.TIME)
    private LocalTime checkout;
    @Column(name = "duration")
    private Integer duration;
    
    @OneToMany(mappedBy = "shiftId")
    @JsonIgnore
    private Set<StaffSchedule> staffScheduleSet;

    public Shift() {
    }

    public Shift(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Set<StaffSchedule> getStaffScheduleSet() {
        return staffScheduleSet;
    }

    public void setStaffScheduleSet(Set<StaffSchedule> staffScheduleSet) {
        this.staffScheduleSet = staffScheduleSet;
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
        if (!(object instanceof Shift)) {
            return false;
        }
        Shift other = (Shift) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lht.pojo.Shift[ id=" + id + " ]";
    }
    
}

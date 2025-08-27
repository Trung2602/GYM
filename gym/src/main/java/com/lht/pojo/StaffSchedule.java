/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import java.util.Date;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "staff_schedule")
@NamedQueries({
    @NamedQuery(name = "StaffSchedule.findAll", query = "SELECT s FROM StaffSchedule s"),
    @NamedQuery(name = "StaffSchedule.findById", query = "SELECT s FROM StaffSchedule s WHERE s.id = :id"),
    @NamedQuery(name = "StaffSchedule.findByDate", query = "SELECT s FROM StaffSchedule s WHERE s.date = :date")})
public class StaffSchedule implements Serializable {

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
    
    @JoinColumn(name = "shift_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnoreProperties({"staffScheduleSet"})
    private Shift shiftId;
    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    //@JsonIgnoreProperties({"staffScheduleSet"})
    private Staff staffId;

    public StaffSchedule() {
    }

    public StaffSchedule(Integer id) {
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

    public Shift getShiftId() {
        return shiftId;
    }

    public void setShiftId(Shift shiftId) {
        this.shiftId = shiftId;
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
        if (!(object instanceof StaffSchedule)) {
            return false;
        }
        StaffSchedule other = (StaffSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lht.pojo.StaffSchedule[ id=" + id + " ]";
    }
    
}

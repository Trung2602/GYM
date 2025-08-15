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
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "facility")
@NamedQueries({
    @NamedQuery(name = "Facility.findAll", query = "SELECT f FROM Facility f"),
    @NamedQuery(name = "Facility.findById", query = "SELECT f FROM Facility f WHERE f.id = :id"),
    @NamedQuery(name = "Facility.findByName", query = "SELECT f FROM Facility f WHERE f.name = :name"),
    @NamedQuery(name = "Facility.findByAddress", query = "SELECT f FROM Facility f WHERE f.address = :address")})
public class Facility implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    
    @OneToMany(mappedBy = "facilityId")
    @JsonIgnore
    private Set<CustomerSchedule> customerScheduleSet;
    @OneToMany(mappedBy = "facilityId")
    @JsonIgnore
    private Set<Staff> staffSet;

    public Facility() {
    }

    public Facility(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<CustomerSchedule> getCustomerScheduleSet() {
        return customerScheduleSet;
    }

    public void setCustomerScheduleSet(Set<CustomerSchedule> customerScheduleSet) {
        this.customerScheduleSet = customerScheduleSet;
    }

    public Set<Staff> getStaffSet() {
        return staffSet;
    }

    public void setStaffSet(Set<Staff> staffSet) {
        this.staffSet = staffSet;
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
        if (!(object instanceof Facility)) {
            return false;
        }
        Facility other = (Facility) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lht.pojo.Facility[ id=" + id + " ]";
    }
    
}

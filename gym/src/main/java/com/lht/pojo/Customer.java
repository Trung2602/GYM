/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
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
@Table(name = "customer")
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c"),
    @NamedQuery(name = "Customer.findById", query = "SELECT c FROM Customer c WHERE c.id = :id"),
    @NamedQuery(name = "Customer.findByExpiryDate", query = "SELECT c FROM Customer c WHERE c.expiryDate = :expiryDate"),
    @NamedQuery(name = "Customer.findByQuantitySauna", query = "SELECT c FROM Customer c WHERE c.quantitySauna = :quantitySauna")})
public class Customer extends Account implements Serializable {

    private static final long serialVersionUID = 1L;
//    @Id
//    @Basic(optional = false)
//    @Column(name = "id")
//    private Integer id;
    @Column(name = "expiry_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;
    @Column(name = "quantity_sauna")
    private Integer quantitySauna;
    
    @OneToMany(mappedBy = "customerId")
    @JsonIgnore
    private Set<CustomerSchedule> customerScheduleSet;
    @OneToMany(mappedBy = "customerId")
    @JsonIgnore
    private Set<PayCustomer> payCustomerSet;
    @OneToMany(mappedBy = "customerId")
    @JsonIgnore
    private Set<Sauna> saunaSet;
    

    public Customer() {
    }

//    public Customer(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getQuantitySauna() {
        return quantitySauna;
    }

    public void setQuantitySauna(Integer quantitySauna) {
        this.quantitySauna = quantitySauna;
    }

    public Set<CustomerSchedule> getCustomerScheduleSet() {
        return customerScheduleSet;
    }

    public void setCustomerScheduleSet(Set<CustomerSchedule> customerScheduleSet) {
        this.customerScheduleSet = customerScheduleSet;
    }

    public Set<PayCustomer> getPayCustomerSet() {
        return payCustomerSet;
    }

    public void setPayCustomerSet(Set<PayCustomer> payCustomerSet) {
        this.payCustomerSet = payCustomerSet;
    }

    public Set<Sauna> getSaunaSet() {
        return saunaSet;
    }

    public void setSaunaSet(Set<Sauna> saunaSet) {
        this.saunaSet = saunaSet;
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
//        if (!(object instanceof Customer)) {
//            return false;
//        }
//        Customer other = (Customer) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "com.lht.pojo.Customer[ id=" + id + " ]";
//    }
//    
}

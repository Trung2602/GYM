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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "sauna")
@NamedQueries({
    @NamedQuery(name = "Sauna.findAll", query = "SELECT s FROM Sauna s"),
    @NamedQuery(name = "Sauna.findById", query = "SELECT s FROM Sauna s WHERE s.id = :id"),
    @NamedQuery(name = "Sauna.findByDate", query = "SELECT s FROM Sauna s WHERE s.date = :date"),
    @NamedQuery(name = "Sauna.findByCheckin", query = "SELECT s FROM Sauna s WHERE s.checkin = :checkin"),
    @NamedQuery(name = "Sauna.findByIsPaid", query = "SELECT s FROM Sauna s WHERE s.isPaid = :isPaid")})
public class Sauna implements Serializable {

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
    @Column(name = "is_paid")
    private Boolean isPaid;
    
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"saunaSet"})
    private Customer customerId;
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"saunaSet"})
    private Room roomId;

    public Sauna() {
    }

    public Sauna(Integer id) {
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

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Room getRoomId() {
        return roomId;
    }

    public void setRoomId(Room roomId) {
        this.roomId = roomId;
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
        if (!(object instanceof Sauna)) {
            return false;
        }
        Sauna other = (Sauna) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lht.pojo.Sauna[ id=" + id + " ]";
    }
    
}

package com.lht.dto;

import com.lht.pojo.Account;
import java.util.Date;

/**
 * Data Transfer Object (DTO) cho Account
 */
public class AccountDTO {

    private Integer id;
    private String username;
    private String role;
    private String name;
    private String mail;
    private String avatar;
    private Date birthday;
    private Boolean gender;
    private Boolean isActive;

    // Constructor từ entity Account
    public AccountDTO(Account acc) {
        if (acc != null) {
            this.id = acc.getId();
            this.username = acc.getUsername();
            this.role = acc.getRole();
            this.name = acc.getName();
            this.mail = acc.getMail();
            this.avatar = acc.getAvatar();
            this.birthday = acc.getBirthday();
            this.gender = acc.getGender();
            this.isActive = acc.getIsActive();
        }
    }

    // Getter & Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}

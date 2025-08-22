/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.dto;

/**
 *
 * @author admin
 */
public class PasswordDTO {
    private String username;
    private String password;
    private String newPassword;

    public PasswordDTO() {}
    
    public PasswordDTO (String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public PasswordDTO (String username, String password, String newPassword){
        this.username = username;
        this.password = password;
        this.newPassword = newPassword;
    }
            
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNewPassword() {
        return password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

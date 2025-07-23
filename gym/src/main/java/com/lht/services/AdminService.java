/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.Admin;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface AdminService {

    List<Admin> getAdmins(Map<String, String> params);

    List<Admin> getAllAdmins();

    Admin getAdminById(Integer id);

    Admin addOrUpdateAdmin(Admin a);

    boolean deleteAdmin(Integer id);
}

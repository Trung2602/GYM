/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lht.pojo.Admin;
import com.lht.reponsitories.AdminRepository;
import com.lht.services.AdminService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author admin
 *
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin getAdminById(Integer id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public Admin addOrUpdateAdmin(Admin a) {
        Admin currentAccount = null;
        if (a.getId() != null) {
            currentAccount = this.getAdminById(a.getId());
        }
        if (a.getFile() != null && !a.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(a.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                a.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(AdminServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (currentAccount != null) {
                // Update mà không đổi ảnh
                a.setAvatar(currentAccount.getAvatar());
            } else {
                // Tạo mới mà không có ảnh
                a.setAvatar("/images/default-avatar.png");
            }
        }
        return adminRepository.save(a);
    }

    @Override
    public boolean deleteAdmin(Integer id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Admin> getAdmins(Map<String, String> params) {
        return null;
    }

}

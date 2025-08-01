/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.reponsitories;

import com.lht.pojo.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 *
 * @author admin
 */
public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account>{
    
}

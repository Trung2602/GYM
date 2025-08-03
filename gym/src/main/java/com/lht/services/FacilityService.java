/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.Facility;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface FacilityService {

    List<Facility> getFacilities(Map<String, String> params);

    List<Facility> getAllFacilities();

    Facility getFacilityById(Integer id);

    Facility addOrUpdateFacility(Facility f);

    boolean deleteFacility(Integer id);
    
    public List<Facility> getAllSort(String sortField, String sortDir);

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.Sauna;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface SaunaService {

    List<Sauna> getSaunas(Map<String, String> params);

    List<Sauna> getAllSaunas();

    Sauna getSaunaById(Integer id);

    Sauna addOrUpdateSauna(Sauna s);

    boolean deleteSauna(Integer id);
    
    public List<Sauna> getAllSort(String sortField, String sortDir);
}

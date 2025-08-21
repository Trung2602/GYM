/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.Salary;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface SalaryService {

    List<Salary> getSalaries(Map<String, String> params);

    List<Salary> getAllSalaries();

    Salary getSalaryById(Integer id);

    Salary addOrUpdateSalary(Salary s);

    boolean deleteSalary(Integer id);

    public List<Salary> getAllSort(String sortField, String sortDir);

    public void calculateMonthlySalaries(int month, int year);
}

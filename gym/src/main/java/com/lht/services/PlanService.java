/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services;

import com.lht.pojo.Plan;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public interface PlanService {

    List<Plan> getPlans(Map<String, String> params);

    List<Plan> getAllPlans();

    Plan getPlanById(Integer id);

    Plan addOrUpdatePlan(Plan p);

    boolean deletePlan(Integer id);
}

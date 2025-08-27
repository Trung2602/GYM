/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.dto.CustomerScheduleDTO;
import com.lht.dto.StaffDTO;
import com.lht.pojo.CustomerSchedule;
import com.lht.pojo.Staff;
import com.lht.services.StaffService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api")
public class ApiStaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/staffs-all") //lấy theo custotmerId
    public ResponseEntity<List<StaffDTO>> getCustomerSchedulesAll() {
        List<Staff> staff = staffService.getAllStaffs();
        // Map sang DTO
        List<StaffDTO> dtos = staff.stream()
                .map(StaffDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/working-staff")
    public ResponseEntity<List<StaffDTO>> getWorkingStaff(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkIn,
            @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkOut
    ) {
        List<Staff> staffList = staffService.getWorkingStaffByDateTime(date, checkIn, checkOut);

        // map sang DTO
        List<StaffDTO> dtos = staffList.stream()
                .map(StaffDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }

}

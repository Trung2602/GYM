/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.dto.StaffScheduleDTO;
import com.lht.pojo.Shift;
import com.lht.pojo.Staff;
import com.lht.pojo.StaffSchedule;
import com.lht.services.ShiftService;
import com.lht.services.StaffScheduleService;
import com.lht.services.StaffService;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api")
public class ApiStaffScheduleController {

    @Autowired
    private StaffScheduleService staffScheduleService;
    
    @Autowired
    private ShiftService shiftService;
    
    @Autowired
    private StaffService staffService;

    @GetMapping("/staff-schedules-all/{id}") // lấy tất cả lịch làm việc theo staffId
    public ResponseEntity<List<StaffScheduleDTO>> getStaffSchedules(@PathVariable("id") Integer id) {
        List<StaffSchedule> schedules = staffScheduleService.getStaffScheduleByStaffId(id);
        List<StaffScheduleDTO> dtos = schedules.stream()
                .map(StaffScheduleDTO::new) // constructor DTO nhận entity
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Lấy danh sách staff schedules theo filter
    @GetMapping("/staff-schedules-filter")
    public ResponseEntity<List<StaffScheduleDTO>> getStaffSchedulesFilter(@RequestParam Map<String, String> params) {
        // Lấy staffId nếu có
        String staffIdStr = params.get("staffId");
        if (staffIdStr != null) {
            params.put("staffId", staffIdStr);
        }

        List<StaffSchedule> schedules = staffScheduleService.getStaffSchedules(params);
        List<StaffScheduleDTO> dtos = schedules.stream()
                .map(StaffScheduleDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Lấy 1 staff schedule theo id
    @GetMapping("/staff-schedule/{id}")
    public ResponseEntity<StaffScheduleDTO> getStaffScheduleById(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        StaffSchedule schedule = staffScheduleService.getStaffScheduleById(id);
        if (schedule == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new StaffScheduleDTO(schedule));
    }

    // Thêm hoặc cập nhật staff schedule
    @PostMapping("/staff-schedule-update")
    public ResponseEntity<?> addOrUpdateStaffSchedule(@RequestBody StaffScheduleDTO dto) {
        StaffSchedule schedule = new StaffSchedule();
        schedule.setId(dto.getId());
        schedule.setDate(dto.getDate());

        // Gắn Staff
        if (dto.getStaffName() != null) {
            Optional<Staff> staffOpt = staffService.getStaffByName(dto.getStaffName());
            if (staffOpt.isPresent()) {
                schedule.setStaffId(staffOpt.get());
            } else {
                return ResponseEntity.badRequest().body("Không tìm thấy staff");
            }
        } else {
            return ResponseEntity.badRequest().body("Staff không được để trống");
        }

        // Gắn Shift
        if (dto.getShiftName() != null) {
            Optional<Shift> shiftOpt = shiftService.getShiftByName(dto.getShiftName());
            if (shiftOpt.isPresent()) {
                schedule.setShiftId(shiftOpt.get());
            } else {
                return ResponseEntity.badRequest().body("Không tìm thấy shift");
            }
        } else {
            return ResponseEntity.badRequest().body("Shift không được để trống");
        }

        // Kiểm tra trùng ngày + checkIn
        Map<String, String> params = new HashMap<>();
        params.put("date", new SimpleDateFormat("yyyy-MM-dd").format(schedule.getDate()));
        List<StaffSchedule> existingSchedules = staffScheduleService.getStaffSchedules(params);

        boolean isConflict = existingSchedules.stream()
                .anyMatch(e -> e.getShiftId() != null
                && e.getShiftId().getCheckin().equals(schedule.getShiftId().getCheckin())
                && e.getDate().equals(schedule.getDate()));

        if (isConflict) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Đã có lịch trùng ngày và giờ checkin");
        }

        StaffSchedule saved = staffScheduleService.addOrUpdateStaffSchedule(schedule);
        return ResponseEntity.ok(new StaffScheduleDTO(saved));
    }

    // Xóa staff schedule
    @DeleteMapping("/staff-schedule-delete/{id}")
    public ResponseEntity<Void> deleteStaffSchedule(@PathVariable("id") Integer id) {
        if (staffScheduleService.deleteStaffSchedule(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.dto.StaffDayOffDTO;
import com.lht.pojo.Staff;
import com.lht.pojo.StaffDayOff;
import com.lht.services.StaffDayOffService;
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
public class ApiStaffDayOffController {

    @Autowired
    private StaffDayOffService staffDayOffService;

    @Autowired
    private StaffService staffService;

    @GetMapping("/staff-day-offs-all/{id}") // lấy tất cả ngày nghỉ theo staffId
    public ResponseEntity<List<StaffDayOffDTO>> getStaffDayOffs(@PathVariable("id") Integer id) {
        List<StaffDayOff> dayOffs = staffDayOffService.getStaffDayOffByStaffId(id);
        List<StaffDayOffDTO> dtos = dayOffs.stream()
                .map(StaffDayOffDTO::new) // đã có constructor DTO nhận entity
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/staff-day-offs-filter")
    public ResponseEntity<List<StaffDayOffDTO>> getStaffDayOffsFilter(@RequestParam Map<String, String> params) {
        List<StaffDayOff> dayOffs = this.staffDayOffService.getStaffDayOffs(params);
        List<StaffDayOffDTO> dtos = dayOffs.stream()
                .map(StaffDayOffDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("staff-day-off/{id}")
    public ResponseEntity<StaffDayOff> getStaffDayOffById(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.staffDayOffService.getStaffDayOffById(id));
    }

    @PostMapping("staff-day-off-update")
    public ResponseEntity<?> addOrUpdateStaffDayOff(@RequestBody StaffDayOffDTO dto) {
        if (dto.getDate() == null) {
            return ResponseEntity.badRequest().body("Ngày nghỉ không được null");
        }
        StaffDayOff sdo = new StaffDayOff();
        sdo.setId(dto.getId());
        sdo.setDate(dto.getDate());

        // Tìm staff theo tên
        if (dto.getStaffName() != null) {
            Optional<Staff> staffOpt = staffService.getStaffByName(dto.getStaffName());
            if (staffOpt.isPresent()) {
                sdo.setStaffId(staffOpt.get());
            } else {
                return ResponseEntity.badRequest().body("Không tìm thấy staff: " + dto.getStaffName());
            }
        } else {
            return ResponseEntity.badRequest().body("StaffName không được null");
        }

        // Kiểm tra trùng ngày (một staff không thể xin nghỉ 2 lần cùng ngày)
        Map<String, String> params = new HashMap<>();
        params.put("staffId", sdo.getStaffId().getId().toString());
        params.put("date", new SimpleDateFormat("yyyy-MM-dd").format(sdo.getDate()));

        List<StaffDayOff> existing = staffDayOffService.getStaffDayOffs(params);

        boolean isConflict = existing.stream()
                .anyMatch(e -> !e.getId().equals(sdo.getId())); // khác id hiện tại => trùng ngày

        if (isConflict) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Nhân viên đã xin nghỉ ngày này rồi");
        }

        StaffDayOff saved = staffDayOffService.addOrUpdateStaffDayOff(sdo);
        return ResponseEntity.ok(new StaffDayOffDTO(saved));
    }

    @DeleteMapping("staff-day-off-delete/{id}")
    public ResponseEntity<Void> deleteStaffDayOff(@PathVariable("id") Integer id) {
        if (this.staffDayOffService.deleteStaffDayOff(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

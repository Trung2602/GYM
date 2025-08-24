/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers.api;

import com.lht.dto.CustomerScheduleDTO;
import com.lht.pojo.Account;
import com.lht.pojo.Customer;
import com.lht.pojo.CustomerSchedule;
import com.lht.pojo.Staff;
import com.lht.services.AccountService;
import com.lht.services.CustomerScheduleService;
import com.lht.services.CustomerService;
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
public class ApiCustomerScheduleController {

    @Autowired
    private CustomerScheduleService customerScheduleService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/customer-schedules-customer/{id}") //lấy theo custotmerId
    public ResponseEntity<List<CustomerScheduleDTO>> getCustomerSchedulesCustomer(@PathVariable("id") Integer id) {
        List<CustomerSchedule> schedules = customerScheduleService.getCustomerScheduleByCustomerId(id);
        // Map sang DTO
        List<CustomerScheduleDTO> dtos = schedules.stream()
                .map(CustomerScheduleDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/customer-schedules-staff/{id}") //lấy theo staffId
    public ResponseEntity<List<CustomerScheduleDTO>> getCustomerSchedulesStaff(@PathVariable("id") Integer id) {
        List<CustomerSchedule> schedules = customerScheduleService.getCustomerScheduleByStaffId(id);
        // Map sang DTO
        List<CustomerScheduleDTO> dtos = schedules.stream()
                .map(CustomerScheduleDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/customer-schedules-all/{id}") //lấy theo accountId
    public ResponseEntity<List<CustomerScheduleDTO>> getCustomerScheduleAll(@PathVariable("id") Integer id) {
        List<CustomerSchedule> schedules;
        if (accountService.getAccountById(id).getRole().equals("Customer")) {
            schedules = customerScheduleService.getCustomerScheduleByCustomerId(id);
        } else {
            schedules = customerScheduleService.getCustomerScheduleByStaffId(id);
        }

        // Map sang DTO
        List<CustomerScheduleDTO> dtos = schedules.stream()
                .map(CustomerScheduleDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/customer-schedules-filter")
    public ResponseEntity<List<CustomerScheduleDTO>> getCustomerSchedulesFilter(@RequestParam Map<String, String> params) {

        // Lấy accountId từ params
        String accountIdStr = params.get("accountId");
        if (accountIdStr == null) {
            return ResponseEntity.badRequest().build();
        }
        Integer accountId = Integer.valueOf(accountIdStr);

        // Lấy Account từ id
        Account acc = accountService.getAccountById(accountId);
        if (acc == null) {
            return ResponseEntity.notFound().build();
        }

        // Tùy role mà thêm param cho service
        if (acc.getRole().equals("Customer")) {
            params.put("customerId", accountIdStr);
        } else if (acc.getRole().equals("Staff")) {
            params.put("staffId", accountIdStr);
        }

        List<CustomerSchedule> schedules = this.customerScheduleService.getCustomerSchedules(params);
        List<CustomerScheduleDTO> dtos = schedules.stream()
                .map(CustomerScheduleDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("customer-schedules-sort")
    public ResponseEntity<List<CustomerScheduleDTO>> getCustomerSchedulesSort(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        List<CustomerSchedule> schedules = this.customerScheduleService.getAllSort(sortField, sortDir);
        List<CustomerScheduleDTO> dtos = schedules.stream()
                .map(CustomerScheduleDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("customer-schedule/{id}")
    public ResponseEntity<CustomerScheduleDTO> getCustomerScheduleById(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseEntity.notFound().build();
        }
        CustomerSchedule cs = this.customerScheduleService.getCustomerScheduleById(id);
        if (cs == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CustomerScheduleDTO(cs));
    }

    @PostMapping("customer-schedule-update")
    public ResponseEntity<?> addOrUpdateCustomerSchedule(@RequestBody CustomerScheduleDTO dto) {
        CustomerSchedule cs = new CustomerSchedule();
        cs.setId(dto.getId());
        cs.setDate(dto.getDate());
        cs.setCheckin(dto.getCheckin());
        cs.setCheckout(dto.getCheckout());

        if (dto.getCustomerName() != null) {
            Optional<Customer> customerOpt = customerService.getCustomerByName(dto.getCustomerName());
            if (customerOpt.isPresent()) {
                cs.setCustomerId(customerOpt.get());
            } else {
                return ResponseEntity.badRequest().body(null); // Hoặc ném exception
            }
        } else {
            return ResponseEntity.badRequest().body(null); // Không được để null
        }

        if (dto.getStaffName() != null) {
            Optional<Staff> staffOpt = staffService.getStaffByName(dto.getStaffName());
            if (staffOpt.isPresent()) {
                Staff staff = staffOpt.get();
                cs.setStaffId(staff);
                if (staff.getFacilityId() != null) {
                    cs.setFacilityId(staff.getFacilityId());
                }
            } else {
                // Xử lý nếu không tìm thấy staff
                cs.setStaffId(null);
                cs.setFacilityId(null);
            }
        }

        // Kiểm tra trùng ngày + checkin
        Map<String, String> params = new HashMap<>();
        params.put("date", new SimpleDateFormat("yyyy-MM-dd").format(cs.getDate()));
        List<CustomerSchedule> existingSchedules = customerScheduleService.getCustomerSchedules(params);

        boolean isConflict = existingSchedules.stream()
                .anyMatch(e -> e.getCheckin() != null && e.getCheckin().equals(cs.getCheckin()));

        if (isConflict) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Đã có lịch trùng ngày và giờ checkin");
        }

        CustomerSchedule saved = customerScheduleService.addOrUpdateCustomerSchedule(cs);
        return ResponseEntity.ok(new CustomerScheduleDTO(saved));
    }

    @DeleteMapping("customer-schedule-delete/{id}")
    public ResponseEntity<Void> deleteCustomerSchedule(@PathVariable("id") Integer id) {
        if (this.customerScheduleService.deleteCustomerSchedule(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

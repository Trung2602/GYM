/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Salary;
import com.lht.pojo.Staff;
import com.lht.pojo.StaffDayOff;
import com.lht.pojo.StaffSchedule;
import com.lht.pojo.StaffType;
import com.lht.reponsitories.SalaryRepository;
import com.lht.reponsitories.StaffRepository;
import com.lht.services.SalaryService;
import com.lht.services.StaffDayOffService;
import com.lht.services.StaffScheduleService;
import com.lht.services.StaffService;

import jakarta.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author admin
 */
@Service
@Transactional
public class SalaryServiceImpl implements SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;
    
    @Autowired
    private StaffService staffService;

    @Autowired
    private StaffDayOffService staffDayOffService;

    @Autowired
    private StaffScheduleService staffScheduleService;

    @Override
    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }

    @Override
    public Salary getSalaryById(Integer id) {
        return salaryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Salary> getSalaries(Map<String, String> params) {
        Specification<Salary> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), Integer.parseInt(params.get("id"))));
            }

            if (params.containsKey("date")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = df.parse(params.get("date"));
                    predicates.add(cb.equal(root.get("date"), date));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid date format, expected yyyy-MM-dd");
                }
            }

            if (params.containsKey("startDate") && params.containsKey("endDate")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = df.parse(params.get("startDate"));
                    Date end = df.parse(params.get("endDate"));
                    predicates.add(cb.between(root.get("date"), start, end));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid date format");
                }
            }

            if (params.containsKey("duration")) {
                predicates.add(cb.equal(root.get("duration"), Integer.parseInt(params.get("duration"))));
            }

            if (params.containsKey("dayOff")) {
                predicates.add(cb.equal(root.get("dayOff"), Integer.parseInt(params.get("dayOff"))));
            }

            if (params.containsKey("price")) {
                predicates.add(cb.equal(root.get("price"), Integer.parseInt(params.get("price"))));
            }

            if (params.containsKey("staffId")) {
                predicates.add(cb.equal(
                        root.get("staffId").get("id"),
                        Integer.parseInt(params.get("staffId"))
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return salaryRepository.findAll(spec);
    }

    @Override
    public Salary addOrUpdateSalary(Salary s) {
        return salaryRepository.save(s);
    }

    @Override
    public boolean deleteSalary(Integer id) {
        if (salaryRepository.existsById(id)) {
            salaryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<Salary> getAllSort(String sortField, String sortDir, int page, int size) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return salaryRepository.findAll(pageable);
    }

    @Override
    public List<Salary> getSalaryByStaffId(Integer id) {
        return this.salaryRepository.findByStaffId_Id(id);
    }
    
//=================================================================================
    @Override
    public void calculateMonthlySalaries(int month, int year) {
        List<Staff> staffList = staffService.getAllStaffs();

        for (Staff staff : staffList) {
            StaffType type = staff.getStaffTypeId();
            int typeId = type.getId();

            // Lấy số ngày nghỉ trong tháng
            int totalDayOff = staffDayOffService.countByStaffIdAndMonthYear(staff.getId(), month, year);

            double duration = 0;
            double totalSalary = 0;

            if ("Fulltime".equals(type.getName())) { // FullTime
                duration = 240; // 240h/tháng
                int allowedDayOff = type.getDayOff(); //Số ngày cho phép nghỉ
                int extraDayOff = Math.max(totalDayOff - allowedDayOff, 0); //Nghỉ quá ngày cho phépp
                double bonusOrPenalty ;
                if (totalDayOff == 0) {
                    bonusOrPenalty  = -5000000; //Nếu không nghỉ ngày nào thì thưởng 500.000
                } else {
                    bonusOrPenalty  = extraDayOff * 500; //Phạt 500/ngày nghỉ
                }
                totalSalary = type.getSalary() - bonusOrPenalty ;

            } else if ("PartTime".equals(type.getName()) || "Intern".equals(type.getName())) { // PartTime hoặc Intern
                // Tính tổng số giờ từ các lịch làm
                duration = staffScheduleService.sumDurationByStaffIdAndMonthYear(staff.getId(), month, year);
                totalSalary = type.getSalary() * duration;
            }

            // Tạo bản ghi Salary
            Salary salary = new Salary();
            salary.setStaffId(staff);

            LocalDate localDate = LocalDate.of(year, month, 10); // Ngày 10 hằng tháng sẽ trả lương
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            salary.setDate(date);

            salary.setDuration(duration);
            salary.setDayOff(totalDayOff);
            salary.setPrice(totalSalary);

            salaryRepository.save(salary);
        }
    }
}

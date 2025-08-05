/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.lht.pojo.Salary;
import com.lht.reponsitories.SalaryRepository;
import com.lht.reponsitories.StaffDayOffRepository;
import com.lht.reponsitories.StaffRepository;
import com.lht.reponsitories.StaffScheduleRepository;
import com.lht.services.SalaryService;

import jakarta.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
    private StaffRepository staffRepository;

    @Autowired
    private StaffDayOffRepository staffDayOffRepository;

    @Autowired
    private StaffScheduleRepository staffScheduleRepository;

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
    public List<Salary> getAllSort(String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return salaryRepository.findAll(sort);
    }

    
//=================================================================================

//    public void calculateMonthlySalaries(int month, int year) {
//        List<Staff> staffList = staffRepository.findAll();
//
//        for (Staff staff : staffList) {
//            StaffType type = staff.getStaffType();
//            int typeId = type.getId();
//
//            // Lấy số ngày nghỉ trong tháng
//            List<StaffDayOff> dayOffs = staffDayOffRepository.findByStaffIdAndMonthYear(staff.getId(), month, year);
//            int totalDayOff = dayOffs.size();
//
//            double duration = 0;
//            double totalSalary = 0;
//
//            if (typeId == 1) { // FullTime
//                duration = 160; // giả sử 160h/tháng (có thể tuỳ chỉnh nếu cần)
//                int allowedDayOff = type.getDayOff();
//                int extraDayOff = Math.max(totalDayOff - allowedDayOff, 0);
//                double penalty = extraDayOff * 500;
//                totalSalary = type.getSalary() - penalty;
//
//            } else if (typeId == 2 || typeId == 3) { // PartTime hoặc Intern
//                // Tính tổng số giờ từ các lịch làm
//                List<StaffSchedule> schedules = staffScheduleRepository.findByStaffIdAndMonthYear(staff.getId(), month, year);
//                for (StaffSchedule s : schedules) {
//                    duration += s.getShift().getDuration();
//                }
//                totalSalary = type.getSalary() * duration;
//            }
//
//            // Tạo bản ghi Salary
//            Salary salary = new Salary();
//            salary.setStaffId(staff);
//            salary.setDate(LocalDate.of(year, month, 10)); // Ví dụ ngày chốt lương là mùng 10
//            salary.setDuration(duration);
//            salary.setDayOff(totalDayOff);
//            salary.setPrice(totalSalary);
//
//            salaryRepository.save(salary);
//        }
//    }

//    @Override
//    public Salary calculateMonthlySalaries() {
//        
//        
//        return salaryRepository.save(s);
//    }
}

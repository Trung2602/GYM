/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lht.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lht.pojo.Shift;
import com.lht.pojo.Staff;
import com.lht.pojo.StaffSchedule;
import com.lht.reponsitories.StaffDayOffRepository;
import com.lht.reponsitories.StaffRepository;
import com.lht.reponsitories.StaffScheduleRepository;
import com.lht.services.StaffService;
import jakarta.persistence.criteria.Predicate;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author admin
 */
@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private StaffDayOffRepository staffDayOffRepository;
    
    @Autowired
    private StaffScheduleRepository staffScheduleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<Staff> getAllStaffs() {
        return staffRepository.findAll();
    }

    @Override
    public Staff getStaffById(Integer id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public List<Staff> getStaffs(Map<String, String> params) {
        Specification<Staff> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Lọc theo createdDate
            if (params.containsKey("createdDate")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date createdDate = df.parse(params.get("createdDate"));
                    predicates.add(cb.equal(root.get("createdDate"), createdDate));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid createdDate format. Expected yyyy-MM-dd");
                }
            }

            // Lọc theo facilityId
            if (params.containsKey("facilityId")) {
                try {
                    predicates.add(cb.equal(root.get("facilityId").get("id"), Integer.parseInt(params.get("facilityId"))));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid facilityId. Expected number.");
                }
            }

            // Lọc theo staffTypeId
            if (params.containsKey("staffTypeId")) {
                try {
                    predicates.add(cb.equal(root.get("staffTypeId").get("id"), Integer.parseInt(params.get("staffTypeId"))));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid staffTypeId. Expected number.");
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return staffRepository.findAll(spec);
    }

    @Override
    public Staff addOrUpdateStaff(Staff s) {
        Staff currentAccount = null;
        if (s.getId() != null) {
            currentAccount = this.getStaffById(s.getId());
        }

        if (s.getId() == null) {
            // Trường hợp thêm mới: mã hóa password bắt buộc
            s.setRole("Staff");
            s.setPassword(this.passwordEncoder.encode(s.getPassword()));
        } else {
            // Trường hợp update: kiểm tra nếu password khác null và khác rỗng, encode lại
            s.setRole(s.getRole());
            if (s.getPassword() != null && !s.getPassword().isEmpty()) {
                // Có thể kiểm tra nếu khác mật khẩu hiện tại mới encode (tùy logic)
                s.setPassword(this.passwordEncoder.encode(s.getPassword()));
            } else {
                // Giữ nguyên password cũ
                s.setPassword(currentAccount.getPassword());
            }
        }

        if (s.getFile() != null && !s.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(s.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                s.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(AdminServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (currentAccount != null) {
                // Update mà không đổi ảnh
                s.setAvatar(currentAccount.getAvatar());
            } else {
                // Tạo mới mà không có ảnh
                s.setAvatar("https://res.cloudinary.com/dxgc9wwrd/image/upload/v1754928114/nzoi1xjxasxfvsut1azv.jpg");
            }
        }

        try {
            return staffRepository.save(s);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public boolean deleteStaff(Integer id) {
        if (staffRepository.existsById(id)) {
            staffRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Staff> getStaffByName(String name) {
        if (name == null || name.isEmpty()) {
            return Optional.empty(); // trả về empty nếu name rỗng
        }
        return this.staffRepository.findByName(name);
    }
    
    private static final LocalTime FULLTIME_START = LocalTime.of(5, 0);
    private static final LocalTime FULLTIME_END   = LocalTime.of(21, 0);

    public List<Staff> getWorkingStaffByDateTime(Date date, LocalTime checkIn, LocalTime checkOut) {
        List<Staff> result = new ArrayList<>();

        // Convert java.util.Date -> LocalDate để dễ xử lý
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 1. Lấy fulltime staff
        List<Staff> fulltimeStaff = staffRepository.findByStaffTypeId_Name("Fulltime");
        for (Staff s : fulltimeStaff) {
            
            Date utilDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            boolean isOff = !staffDayOffRepository.findByDateAndStaffId_Id(utilDate, s.getId()).isEmpty();
            boolean validTime = !checkIn.isBefore(FULLTIME_START) && !checkOut.isAfter(FULLTIME_END);

            if (!isOff && validTime) {
                result.add(s);
            }
        }

        // 2. Lấy parttime staff theo schedule
        List<StaffSchedule> schedules = staffScheduleRepository.findByDate(localDate);
        for (StaffSchedule sc : schedules) {
            Shift shift = sc.getShiftId();
            if (shift != null) {
                boolean validShift = !checkIn.isBefore(shift.getCheckin()) && !checkOut.isAfter(shift.getCheckout());
                if (validShift) {
                    Staff staff = sc.getStaffId();
                    if (!result.contains(staff)) {
                        result.add(staff);
                    }
                }
            }
        }

        return result;
    }

}

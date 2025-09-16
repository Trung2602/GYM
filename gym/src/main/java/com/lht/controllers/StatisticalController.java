/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lht.controllers;

import com.lht.pojo.PayCustomer;
import com.lht.pojo.Plan;
import com.lht.pojo.Salary;
import com.lht.pojo.StaffType;
import com.lht.reponsitories.PayCustomerRepository;
import com.lht.reponsitories.PlanRepository;
import com.lht.reponsitories.SalaryRepository;
import com.lht.services.PayCustomerService;
import com.lht.services.PlanService;
import com.lht.services.SalaryService;
import com.lht.services.StaffTypeService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
public class StatisticalController {

    @Autowired
    private PlanService planService;

    @Autowired
    private StaffTypeService staffTypeService;

    @Autowired
    private PayCustomerService payCustomerService;

    @Autowired
    private SalaryService salaryService;

    @GetMapping("/statistical")
    public String financeStats(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {

        if (startDate == null) {
            startDate = LocalDate.of(2025, 1, 1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        // --- Chuẩn bị params để lọc theo khoảng ngày ---
        Map<String, String> params = new LinkedHashMap<>();
        params.put("startDate", startDate.toString());
        params.put("endDate", endDate.toString());

        // --- Lấy danh sách PayCustomer ---
        List<PayCustomer> payList = payCustomerService.getPayCustomers(params);

        // --- Pay_customer theo thời gian ---
        Map<String, Double> payByTime = new TreeMap<>();
        SimpleDateFormat monthFmt = new SimpleDateFormat("yyyy-MM");
        for (PayCustomer pay : payList) {
            String key = monthFmt.format(pay.getDate()); // format java.util.Date trực tiếp
            double price = pay.getPlanId().getPrice();
            payByTime.put(key, payByTime.getOrDefault(key, 0.0) + price);
        }

        // --- Pay_customer theo Plan ---
        Map<String, Double> payByPlan = new LinkedHashMap<>();
        List<Plan> allPlans = planService.getPlans(new LinkedHashMap<>());
        for (Plan plan : allPlans) {
            double total = payList.stream()
                    .filter(p -> p.getPlanId().getId().equals(plan.getId()))
                    .mapToDouble(p -> p.getPlanId().getPrice())
                    .sum();
            if (total > 0) {
                payByPlan.put(plan.getName(), total);
            }
        }

        // --- Lấy danh sách Salary ---
        List<Salary> salaryList = salaryService.getSalaries(params);

        // --- Salary theo thời gian ---
        Map<String, Double> salaryByTime = new TreeMap<>();
        for (Salary s : salaryList) {
            String key = monthFmt.format(s.getDate()); // format Date trực tiếp
            salaryByTime.put(key, salaryByTime.getOrDefault(key, 0.0) + s.getPrice());
        }
        // --- Salary theo staffType ---
        Map<String, Double> salaryByType = new LinkedHashMap<>();
        List<StaffType> allStaffTypes = staffTypeService.getStaffTypes(new LinkedHashMap<>());
        for (StaffType s : allStaffTypes) {
            double total = salaryList.stream()
                    .filter(p -> p.getStaffId().getStaffTypeId().getId().equals(s.getId()))
                    .mapToDouble(Salary::getPrice)
                    .sum();
            if (total > 0) {
                salaryByType.put(s.getName(), total);
            }
        }

        // --- Gom dữ liệu Doanh thu & Chi tiêu để vẽ chart so sánh ---
        Set<String> allMonths = new TreeSet<>();
        allMonths.addAll(payByTime.keySet());
        allMonths.addAll(salaryByTime.keySet());

        List<Double> payValues = new ArrayList<>();
        List<Double> salaryValues = new ArrayList<>();
        for (String month : allMonths) {
            payValues.add(payByTime.getOrDefault(month, 0.0));
            salaryValues.add(salaryByTime.getOrDefault(month, 0.0));
        }

        // --- Tổng quan ---
        double totalPayCustomerRevenue = payList.stream().mapToDouble(p -> p.getPlanId().getPrice()).sum();
        double totalSalary = salaryList.stream().mapToDouble(Salary::getPrice).sum();
        double totalProfit = totalPayCustomerRevenue - totalSalary;

        // --- Add model ---
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        model.addAttribute("payByTime", payByTime);
        model.addAttribute("payByPlan", payByPlan);
        model.addAttribute("salaryByTime", salaryByTime);
        model.addAttribute("salaryByType", salaryByType);

        model.addAttribute("payCustomerList", payList);
        model.addAttribute("totalPayCustomerRevenue", totalPayCustomerRevenue);
        model.addAttribute("totalSalary", totalSalary);
        model.addAttribute("totalProfit", totalProfit);

        model.addAttribute("allMonths", allMonths);
        model.addAttribute("payValues", payValues);
        model.addAttribute("salaryValues", salaryValues);

        return "statistical";
    }

    private LocalDate getDefaultStartDate() {
        return LocalDate.of(2025, 1, 1);
    }

//    @GetMapping("/statistical/export-pay")
//    public void exportPayStats(
//            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
//            HttpServletResponse response) throws IOException {
//
//        // Gán ngày mặc định
//        if (startDate == null) {
//            startDate = getDefaultStartDate();
//        }
//        if (endDate == null) {
//            endDate = LocalDate.now();
//        }
//
//        // Chuẩn bị params
//        Map<String, String> params = new HashMap<>();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        params.put("startDate", startDate.format(formatter));
//        params.put("endDate", endDate.format(formatter));
//
//        // Lấy dữ liệu pay-customer theo filter
//        List<PayCustomer> payList = payCustomerService.getPayCustomers(params);
//
//        // Tạo workbook
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Pay Statistics");
//
//        // Header
//        Row header = sheet.createRow(0);
//        header.createCell(0).setCellValue("Tên Khách Hàng");
//        header.createCell(1).setCellValue("Gói Tập");
//        header.createCell(2).setCellValue("Giá Gói");
//        header.createCell(3).setCellValue("Ngày Thanh Toán");
//
//        // Dữ liệu
//        int rowNum = 1;
//        try {
//            for (PayCustomer pay : payList) {
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(pay.getCustomerId() != null ? pay.getCustomerId().getName() : "N/A");
//                row.createCell(1).setCellValue(pay.getPlanId() != null ? pay.getPlanId().getName() : "N/A");
//                row.createCell(2).setCellValue(pay.getPlanId() != null ? pay.getPlanId().getPrice() : 0.0);
//                row.createCell(3).setCellValue(
//                        pay.getDate() != null ? pay.getDate().toString() : "N/A" // nếu pay.getDate là LocalDate
//                );
//            }
//        } catch (Exception e) {
//            System.err.println("Lỗi khi ghi dòng: " + e.getMessage());
//            throw new IOException("Không thể xuất thống kê", e);
//        }
//
//        // Auto size cột
//        for (int i = 0; i < 4; i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//        // Headers response
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename=pay_stats.xlsx");
//
//        // Xuất ra file
//        try {
//            workbook.write(response.getOutputStream());
//        } finally {
//            workbook.close();
//        }
//    }
    @GetMapping("/statistical/export-pay")
    public void exportAllStats(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(name = "totalRevenue", required = false) Double totalRevenue,
            @RequestParam(name = "totalExpense", required = false) Double totalExpense,
            @RequestParam(name = "totalProfit", required = false) Double totalProfit,
            HttpServletResponse response) throws IOException {

        if (startDate == null) {
            startDate = getDefaultStartDate();
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, String> params = new HashMap<>();
        params.put("startDate", startDate.format(formatter));
        params.put("endDate", endDate.format(formatter));

        // Lấy dữ liệu từ service
        List<PayCustomer> payList = payCustomerService.getPayCustomers(params);
        List<Salary> salaryList = salaryService.getSalaries(params);

        Workbook workbook = new XSSFWorkbook();

        // ===== Sheet 1: Thanh toán khách hàng =====
        Sheet sheet1 = workbook.createSheet("Thanh toán khách hàng");
        Row header1 = sheet1.createRow(0);
        header1.createCell(0).setCellValue("Tên Khách Hàng");
        header1.createCell(1).setCellValue("Gói Tập");
        header1.createCell(2).setCellValue("Giá Gói");
        header1.createCell(3).setCellValue("Ngày Thanh Toán");

        int rowNum = 1;
        for (PayCustomer pay : payList) {
            Row row = sheet1.createRow(rowNum++);
            row.createCell(0).setCellValue(pay.getCustomerId() != null ? pay.getCustomerId().getName() : "N/A");
            row.createCell(1).setCellValue(pay.getPlanId() != null ? pay.getPlanId().getName() : "N/A");
            row.createCell(2).setCellValue(pay.getPlanId() != null ? pay.getPlanId().getPrice() : 0.0);
            row.createCell(3).setCellValue(pay.getDate() != null ? pay.getDate().toString() : "N/A");
        }
        for (int i = 0; i < 4; i++) {
            sheet1.autoSizeColumn(i);
        }

        // ===== Sheet 2: Lương nhân viên =====
        Sheet sheet2 = workbook.createSheet("Thanh toán lương");
        Row header2 = sheet2.createRow(0);
        header2.createCell(0).setCellValue("Tên Nhân Viên");
        header2.createCell(1).setCellValue("Lương");
        header2.createCell(2).setCellValue("Ngày Thanh Toán");

        rowNum = 1;
        for (Salary s : salaryList) {
            Row row = sheet2.createRow(rowNum++);
            row.createCell(0).setCellValue(s.getStaffId() != null ? s.getStaffId().getName() : "N/A");
            row.createCell(1).setCellValue(s.getPrice());
            row.createCell(2).setCellValue(s.getDate() != null ? s.getDate().toString() : "N/A");
        }
        for (int i = 0; i < 3; i++) {
            sheet2.autoSizeColumn(i);
        }

        // ===== Sheet 3: Tổng quan =====
        Sheet sheet3 = workbook.createSheet("Tổng quan");
        Row row0 = sheet3.createRow(0);
        row0.createCell(0).setCellValue("Chỉ số");
        row0.createCell(1).setCellValue("Giá trị (VNĐ)");

        Row row1 = sheet3.createRow(1);
        row1.createCell(0).setCellValue("Tổng Doanh Thu");
        row1.createCell(1).setCellValue(totalRevenue);

        Row row2 = sheet3.createRow(2);
        row2.createCell(0).setCellValue("Tổng Chi Tiêu");
        row2.createCell(1).setCellValue(totalExpense);

        Row row3 = sheet3.createRow(3);
        row3.createCell(0).setCellValue("Tổng Lợi Nhuận");
        row3.createCell(1).setCellValue(totalProfit);

        sheet3.autoSizeColumn(0);
        sheet3.autoSizeColumn(1);

        // ===== Response =====
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=financial_stats.xlsx");

        try {
            workbook.write(response.getOutputStream());
        } finally {
            workbook.close();
        }
    }

}

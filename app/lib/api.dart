class Api {
  static const String baseUrl = "http://192.168.1.10:8080/api";

  static const String login = "$baseUrl/login";
  static const String me = "$baseUrl/account/me";
  static const String register = "$baseUrl/register-customer";
  // ================== FACILITY ==================
  static const String getFacilities = "$baseUrl/facilities-all";
  static const String getFacilitiesFilter = "$baseUrl/facilities-filter";
  static const String getFacilitiesSort = "$baseUrl/facilities-sort";
  static String getFacilityById(int id) => "$baseUrl/facility/$id";
  static const String postFacility = "$baseUrl/facility-update";
  static String deleteFacility(int id) => "$baseUrl/facility-delete/$id";
  // ================== PLAN ==================
  static const String getPlans = "$baseUrl/plans-all";
  static const String getPlansFilter = "$baseUrl/plans-filter";
  static const String getPlansSort = "$baseUrl/plans-sort";
  static String getPlanById(int id) => "$baseUrl/plan/$id";
  static const String postPlan = "$baseUrl/plan-update";
  // ================== SHIFT ==================
  static const String getShifts = "$baseUrl/shifts-all";
  static const String getShiftsFilter = "$baseUrl/shifts-filter";
  static const String getShiftsSort = "$baseUrl/shifts-sort";
  static String getShiftById(int id) => "$baseUrl/shift/$id";
  static const String postShift = "$baseUrl/shift-update";


  // ==================///// CUSTOMER \\\\\==================
  // ================== CUSTOMER SCHEDULE ==================
  static String getCustomerSchedulesAll(int customerId) => "$baseUrl/customer-schedules-all/$customerId";
  static const String getCustomerSchedulesFilter = "$baseUrl/customer-schedules-filter";
  static const String getCustomerSchedulesSort = "$baseUrl/customer-schedules-sort";
  static String getCustomerScheduleById(int id) => "$baseUrl/customer-schedule/$id";
  static const String postCustomerSchedule = "$baseUrl/customer-schedule-update";
  static String deleteCustomerSchedule(int id) => "$baseUrl/customer-schedule-delete/$id";
// ================== PAY CUSTOMER ==================
  static const String getPayCustomers = "$baseUrl/pay-customer-all";
  static const String getPayCustomersFilter = "$baseUrl/pay-customer-filter";
  static const String getPayCustomersSort = "$baseUrl/pay-customer-sort";
  static String getPayCustomerById(int id) => "$baseUrl/pay-customer/$id";
  static const String postPayCustomer = "$baseUrl/pay-customer-update";
  static String deletePayCustomer(int id) => "$baseUrl/pay-customer-delete/$id";


  // ==================///// STAFF \\\\\==================
  // ================== STAFF =================
  static const String getStaffs = "$baseUrl/staffs-all";

  // ================== STAFF TYPE ==================
  static const String getStaffTypes = "$baseUrl/staff-type-all";
  static const String getStaffTypesFilter = "$baseUrl/staff-type-filter";
  static const String getStaffTypesSort = "$baseUrl/staff-type-sort";
  static String getStaffTypeById(int id) => "$baseUrl/staff-type/$id";
  static const String postStaffType = "$baseUrl/staff-type-update";
  // ================== STAFF SCHEDULE ==================
  static const String getStaffSchedules = "$baseUrl/staff-schedules-all";
  static const String getStaffSchedulesFilter = "$baseUrl/staff-schedules-filter";
  static const String getStaffSchedulesSort = "$baseUrl/staff-schedules-sort";
  static String getStaffScheduleById(int id) => "$baseUrl/staff-schedule/$id";
  static const String postStaffSchedule = "$baseUrl/staff-schedule-update";
  static String deleteStaffSchedule(int id) => "$baseUrl/staff-schedule-delete/$id";
  // ================== STAFF DAY OFF ==================
  static const String getStaffDayOffs = "$baseUrl/staff-day-offs-all";
  static const String getStaffDayOffsFilter = "$baseUrl/staff-day-offs-filter";
  static const String getStaffDayOffsSort = "$baseUrl/staff-day-offs-sort";
  static String getStaffDayOffById(int id) => "$baseUrl/staff-day-off/$id";
  static const String postStaffDayOff = "$baseUrl/staff-day-off-update";
  static String deleteStaffDayOff(int id) => "$baseUrl/staff-day-off-delete/$id";
  // ================== SALARY ==================
  static const String getSalaries = "$baseUrl/salaries-all";
  static const String getSalariesFilter = "$baseUrl/salaries-filter";
  static const String getSalariesSort = "$baseUrl/salaries-sort";
  static String getSalaryById(int id) => "$baseUrl/salary/$id";
  static const String postSalary = "$baseUrl/salary-update";
  static String deleteSalary(int id) => "$baseUrl/salary-delete/$id";

}

class Api {
  static const String baseUrl = "http://192.168.1.7:8080/api";

  static const String login = "$baseUrl/login";
  static const String me = "$baseUrl/account/me";
  static const String register = "$baseUrl/register";
  // ================== PLAN ==================
  static const String getPlans = "$baseUrl/plans-all";
  static const String getPlansFilter = "$baseUrl/plans-filter";
  static const String getPlansSort = "$baseUrl/plans-sort";
  static String getPlanById(int id) => "$baseUrl/plan/$id";
  static const String postPlan = "$baseUrl/plan-update";
  static String deletePlan(int id) => "$baseUrl/plan-delete/$id";
  // ================== CUSTOMER SCHEDULE ==================
  static const String getCustomerSchedules = "$baseUrl/customer-schedules-all";
  static const String getCustomerSchedulesFilter = "$baseUrl/customer-schedules-filter";
  static const String getCustomerSchedulesSort = "$baseUrl/customer-schedules-sort";
  static String getCustomerScheduleById(int id) => "$baseUrl/customer-schedule/$id";
  static const String postCustomerSchedule = "$baseUrl/customer-schedule-update";
  static String deleteCustomerSchedule(int id) => "$baseUrl/customer-schedule-delete/$id";
}

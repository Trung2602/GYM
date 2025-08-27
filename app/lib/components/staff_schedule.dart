// staff_schedule.dart
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:table_calendar/table_calendar.dart';
import 'package:provider/provider.dart';

import 'package:gym/api.dart';
import 'package:gym/models/StaffSchedule.dart';
import 'package:gym/models/AccountProvider.dart';
import 'package:gym/models/Account.dart';

class StaffScheduleScreen extends StatefulWidget {
  const StaffScheduleScreen({super.key});

  @override
  State<StaffScheduleScreen> createState() => _StaffScheduleScreenState();
}

class _StaffScheduleScreenState extends State<StaffScheduleScreen> {
  CalendarFormat _calendarFormat = CalendarFormat.month;
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;
  Account? account;

  bool isLoading = false;
  List<StaffSchedule> schedulesForSelectedDay = [];

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    account = Provider.of<AccountProvider>(context).account;
  }

  Future<List<StaffSchedule>> getStaffSchedules(int staffId) async {
    final uri = Uri.parse(Api.getStaffSchedules(staffId));
    final response = await http.get(uri);

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((e) => StaffSchedule.fromJson(e)).toList();
    } else {
      throw Exception("Failed to load staff schedules");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      // decoration: const BoxDecoration(
      //   image: DecorationImage(
      //     image: AssetImage('assets/images/background.jpg'),
      //     fit: BoxFit.cover,
      //     opacity: 0.7,
      //   ),
      // ),
      child: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              "Lịch Làm Việc Nhân Viên",
              style: TextStyle(
                color: Color(0xFFFFAB40),
                fontSize: 28,
                fontWeight: FontWeight.bold,
                shadows: [Shadow(blurRadius: 10, color: Colors.black, offset: Offset(2, 2))],
              ),
            ),
            const SizedBox(height: 20),

            // Calendar
            Card(
              color: Colors.white.withOpacity(0.1),
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
              elevation: 7,
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: TableCalendar(
                  firstDay: DateTime.utc(2020, 1, 1),
                  lastDay: DateTime.utc(2030, 12, 31),
                  focusedDay: _focusedDay,
                  calendarFormat: _calendarFormat,
                  selectedDayPredicate: (day) => isSameDay(_selectedDay, day),
                  onDaySelected: (selectedDay, focusedDay) async {
                    setState(() {
                      _selectedDay = selectedDay;
                      _focusedDay = focusedDay;
                      isLoading = true;
                    });

                    try {
                      final schedules = await getStaffSchedules(account!.id);
                      setState(() {
                        schedulesForSelectedDay = schedules
                            .where((s) =>
                        s.date?.year == selectedDay.year &&
                            s.date?.month == selectedDay.month &&
                            s.date?.day == selectedDay.day)
                            .toList();
                      });
                    } catch (e) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text("Lỗi khi tải lịch: $e")),
                      );
                    } finally {
                      setState(() {
                        isLoading = false;
                      });
                    }
                  },
                  onFormatChanged: (format) {
                    if (_calendarFormat != format) {
                      setState(() => _calendarFormat = format);
                    }
                  },
                  onPageChanged: (focusedDay) {
                    _focusedDay = focusedDay;
                  },
                  headerStyle: const HeaderStyle(
                    formatButtonVisible: false,
                    titleCentered: true,
                    titleTextStyle: TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold),
                    leftChevronIcon: Icon(Icons.chevron_left, color: Color(0xFFFFD740)),
                    rightChevronIcon: Icon(Icons.chevron_right, color: Color(0xFFFFD740)),
                  ),
                  calendarStyle: const CalendarStyle(
                    outsideDaysVisible: false,
                    weekendTextStyle: TextStyle(color: Colors.redAccent),
                    todayDecoration: BoxDecoration(color: Color(0xFF2C318F), shape: BoxShape.circle),
                    selectedDecoration: BoxDecoration(color: Color(0xFFFFAB40), shape: BoxShape.circle),
                    defaultTextStyle: TextStyle(color: Colors.white),
                  ),
                  daysOfWeekStyle: const DaysOfWeekStyle(
                    weekdayStyle: TextStyle(color: Colors.white70),
                    weekendStyle: TextStyle(color: Colors.redAccent),
                  ),
                ),
              ),
            ),

            const SizedBox(height: 30),
            const Text(
              "Lịch Làm Việc Trong Ngày:",
              style: TextStyle(
                color: Colors.white,
                fontSize: 22,
                fontWeight: FontWeight.bold,
                shadows: [Shadow(blurRadius: 5, color: Colors.black54, offset: Offset(1, 1))],
              ),
            ),
            const SizedBox(height: 15),

            _selectedDay == null
                ? const Text("Hãy chọn một ngày để xem lịch làm việc",
                style: TextStyle(color: Colors.white70, fontSize: 16))
                : _buildEventListForSelectedDay(_selectedDay!),
          ],
        ),
      ),
    );
  }

  Widget _buildEventListForSelectedDay(DateTime day) {
    if (isLoading) {
      return const Center(child: CircularProgressIndicator(color: Colors.white));
    }

    if (schedulesForSelectedDay.isEmpty) {
      return Text(
        "Không có ca làm vào ngày ${day.toLocal().toString().split(' ')[0]}",
        style: const TextStyle(color: Colors.white70, fontSize: 16),
      );
    }

    return Column(
      children: schedulesForSelectedDay.map((s) {
        return Card(
          color: Colors.white.withOpacity(0.15),
          margin: const EdgeInsets.symmetric(vertical: 8),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
          child: ListTile(
            leading: const Icon(Icons.schedule, color: Color(0xFFFFD740)),
            title: Text(
              "${s.shiftName}",
              style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
            ),
            subtitle: Text(
              "Nhân viên: ${s.staffName}\n"
                  "Giờ: ${s.checkIn} - ${s.checkOut}",
              style: const TextStyle(color: Colors.white70),
            ),
          ),
        );
      }).toList(),
    );
  }
}

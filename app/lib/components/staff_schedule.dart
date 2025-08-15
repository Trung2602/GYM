// staff_schedule.dart
import 'package:flutter/material.dart';
import 'package:table_calendar/table_calendar.dart';

class StaffSchedule extends StatefulWidget {
  const StaffSchedule({super.key});

  @override
  State<StaffSchedule> createState() => _StaffScheduleState();
}

class _StaffScheduleState extends State<StaffSchedule> {
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;

  // Ví dụ dữ liệu lịch làm việc
  final Map<DateTime, List<String>> _workEvents = {
    DateTime.utc(2025, 8, 15): ['Ca sáng 6:00 - 12:00', 'Ca chiều 14:00 - 18:00'],
    DateTime.utc(2025, 8, 16): ['Ca sáng 6:00 - 12:00'],
    DateTime.utc(2025, 8, 17): ['Ca chiều 14:00 - 18:00'],
  };

  List<String> _getEventsForDay(DateTime day) {
    return _workEvents[DateTime.utc(day.year, day.month, day.day)] ?? [];
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF0F123A),
      appBar: AppBar(
        title: const Text('Ca Làm'),
        backgroundColor: const Color(0xFF1A237E),
      ),
      body: Padding(
        padding: const EdgeInsets.all(12.0),
        child: Column(
          children: [
            TableCalendar(
              firstDay: DateTime.utc(2023, 1, 1),
              lastDay: DateTime.utc(2030, 12, 31),
              focusedDay: _focusedDay,
              selectedDayPredicate: (day) {
                return isSameDay(_selectedDay, day);
              },
              onDaySelected: (selectedDay, focusedDay) {
                setState(() {
                  _selectedDay = selectedDay;
                  _focusedDay = focusedDay;
                });
              },
              calendarStyle: const CalendarStyle(
                todayDecoration: BoxDecoration(
                  color: Color(0xFFFFD740),
                  shape: BoxShape.circle,
                ),
                selectedDecoration: BoxDecoration(
                  color: Color(0xFF2C318F),
                  shape: BoxShape.circle,
                ),
                defaultTextStyle: TextStyle(color: Colors.white),
                weekendTextStyle: TextStyle(color: Colors.white70),
              ),
              headerStyle: const HeaderStyle(
                formatButtonVisible: false,
                titleCentered: true,
                titleTextStyle: TextStyle(color: Colors.white, fontSize: 18),
                leftChevronIcon: Icon(Icons.chevron_left, color: Colors.white),
                rightChevronIcon: Icon(Icons.chevron_right, color: Colors.white),
              ),
            ),
            const SizedBox(height: 20),
            Expanded(
              child: Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: Colors.white.withOpacity(0.08),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: _selectedDay == null
                    ? const Center(
                  child: Text(
                    'Chọn ngày để xem ca làm việc',
                    style: TextStyle(color: Colors.white70, fontSize: 16),
                  ),
                )
                    : ListView(
                  children: _getEventsForDay(_selectedDay!).map((event) {
                    return Card(
                      color: Colors.white.withOpacity(0.1),
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(10)),
                      child: ListTile(
                        leading: const Icon(Icons.access_time, color: Colors.white70),
                        title: Text(
                          event,
                          style: const TextStyle(color: Colors.white),
                        ),
                      ),
                    );
                  }).toList(),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

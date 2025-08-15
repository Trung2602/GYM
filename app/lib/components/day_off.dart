// day_off.dart
import 'package:flutter/material.dart';
import 'package:table_calendar/table_calendar.dart';

class DayOff extends StatefulWidget {
  const DayOff({super.key});

  @override
  State<DayOff> createState() => _DayOffState();
}

class _DayOffState extends State<DayOff> {
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;

  // Danh sách ngày nghỉ đã đăng ký (ví dụ)
  final List<DateTime> _registeredDaysOff = [
    DateTime.utc(2025, 8, 12),
    DateTime.utc(2025, 8, 18),
  ];

  void _registerDayOff() {
    if (_selectedDay == null) return;

    if (!_registeredDaysOff.contains(_selectedDay)) {
      setState(() {
        _registeredDaysOff.add(_selectedDay!);
      });

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Ngày ${_selectedDay!.day}/${_selectedDay!.month}/${_selectedDay!.year} đã được đăng ký nghỉ.'),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFF0F123A),
      appBar: AppBar(
        title: const Text('Xin Nghỉ'),
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
            ElevatedButton(
              onPressed: _registerDayOff,
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFFFFD740),
                foregroundColor: Colors.black,
                padding: const EdgeInsets.symmetric(horizontal: 30, vertical: 12),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10)),
              ),
              child: const Text(
                'Xin Nghỉ Ngày Chọn',
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
            ),
            const SizedBox(height: 20),
            const Align(
              alignment: Alignment.centerLeft,
              child: Text(
                'Danh sách ngày nghỉ đã đăng ký:',
                style: TextStyle(color: Colors.white70, fontSize: 16),
              ),
            ),
            const SizedBox(height: 10),
            Expanded(
              child: _registeredDaysOff.isEmpty
                  ? const Center(
                child: Text(
                  'Chưa có ngày nghỉ nào đăng ký.',
                  style: TextStyle(color: Colors.white54),
                ),
              )
                  : ListView.builder(
                itemCount: _registeredDaysOff.length,
                itemBuilder: (context, index) {
                  final day = _registeredDaysOff[index];
                  return Card(
                    color: Colors.white.withOpacity(0.08),
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10)),
                    margin: const EdgeInsets.symmetric(vertical: 5),
                    child: ListTile(
                      leading: const Icon(Icons.beach_access, color: Colors.white70),
                      title: Text(
                        '${day.day}/${day.month}/${day.year}',
                        style: const TextStyle(color: Colors.white),
                      ),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}

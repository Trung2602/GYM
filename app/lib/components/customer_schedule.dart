// customer_schedule.dart
import 'package:flutter/material.dart';
import 'package:table_calendar/table_calendar.dart';

class CustomerSchedule extends StatefulWidget {
  const CustomerSchedule({super.key});

  @override
  State<CustomerSchedule> createState() => _CustomerScheduleState();
}

class _CustomerScheduleState extends State<CustomerSchedule> {
  CalendarFormat _calendarFormat = CalendarFormat.month;
  DateTime _focusedDay = DateTime.now();
  DateTime? _selectedDay;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        image: DecorationImage(
          image: AssetImage('assets/images/background.jpg'), // Ảnh nền vũ trụ
          fit: BoxFit.cover,
          opacity: 0.7,
        ),
      ),
      child: SingleChildScrollView( // Sử dụng SingleChildScrollView để tránh overflow nếu màn hình nhỏ
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              "Lịch Trình Huấn Luyện Thiên Hà",
              style: TextStyle(
                color: Color(0xFFFFAB40),
                fontSize: 28,
                fontWeight: FontWeight.bold,
                shadows: [
                  Shadow(blurRadius: 10.0, color: Colors.black, offset: Offset(2, 2))
                ],
              ),
            ),
            const SizedBox(height: 20),
            // Card chứa lịch
            Card(
              color: Colors.white.withOpacity(0.1), // Nền trong suốt nhẹ
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
              elevation: 7,
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: TableCalendar(
                  firstDay: DateTime.utc(2020, 1, 1),
                  lastDay: DateTime.utc(2030, 12, 31),
                  focusedDay: _focusedDay,
                  calendarFormat: _calendarFormat,
                  selectedDayPredicate: (day) {
                    // Sử dụng `isSameDay` để kiểm tra chính xác ngày
                    return isSameDay(_selectedDay, day);
                  },
                  onDaySelected: (selectedDay, focusedDay) {
                    setState(() {
                      _selectedDay = selectedDay;
                      _focusedDay = focusedDay; // Cập nhật ngày focus khi ngày được chọn
                    });
                    // Bạn có thể thêm logic hiển thị sự kiện cho ngày được chọn tại đây
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text('Bạn đã chọn ngày: ${selectedDay.toLocal().toString().split(' ')[0]}')),
                    );
                  },
                  onFormatChanged: (format) {
                    if (_calendarFormat != format) {
                      setState(() {
                        _calendarFormat = format;
                      });
                    }
                  },
                  onPageChanged: (focusedDay) {
                    _focusedDay = focusedDay;
                  },

                  // Tùy chỉnh giao diện lịch
                  headerStyle: const HeaderStyle(
                    formatButtonVisible: false, // Ẩn nút chọn định dạng (tháng/tuần/2 tuần)
                    titleCentered: true,
                    titleTextStyle: TextStyle(color: Colors.white, fontSize: 18.0, fontWeight: FontWeight.bold),
                    leftChevronIcon: Icon(Icons.chevron_left, color: Color(0xFFFFD740)), // Mũi tên trái
                    rightChevronIcon: Icon(Icons.chevron_right, color: Color(0xFFFFD740)), // Mũi tên phải
                  ),
                  calendarStyle: CalendarStyle(
                    outsideDaysVisible: false, // Ẩn ngày của tháng trước/sau
                    weekendTextStyle: const TextStyle(color: Colors.redAccent), // Màu chữ cuối tuần
                    todayDecoration: const BoxDecoration(
                      color: Color(0xFF2C318F), // Màu hôm nay
                      shape: BoxShape.circle,
                    ),
                    selectedDecoration: const BoxDecoration(
                      color: Color(0xFFFFAB40), // Màu ngày được chọn
                      shape: BoxShape.circle,
                    ),
                    defaultTextStyle: const TextStyle(color: Colors.white), // Màu chữ ngày thường
                    holidayTextStyle: const TextStyle(color: Colors.greenAccent), // Màu chữ ngày lễ (nếu có)
                    // You can customize more here
                  ),
                  daysOfWeekStyle: const DaysOfWeekStyle(
                    weekdayStyle: TextStyle(color: Colors.white70), // Màu chữ ngày trong tuần
                    weekendStyle: TextStyle(color: Colors.redAccent), // Màu chữ cuối tuần trong tiêu đề
                  ),
                ),
              ),
            ),
            const SizedBox(height: 30),
            const Text(
              "Sự Kiện Trong Ngày Được Chọn:",
              style: TextStyle(
                  color: Colors.white,
                  fontSize: 22,
                  fontWeight: FontWeight.bold,
                  shadows: [
                    Shadow(blurRadius: 5.0, color: Colors.black54, offset: Offset(1, 1))
                  ]
              ),
            ),
            const SizedBox(height: 15),
            // Hiển thị sự kiện hoặc thông báo nếu không có sự kiện
            _selectedDay == null
                ? const Text(
              "Hãy chọn một ngày trên lịch để xem lịch trình của bạn.",
              style: TextStyle(color: Colors.white70, fontSize: 16),
            )
                : _buildEventListForSelectedDay(_selectedDay!), // Truyền ngày được chọn vào hàm hiển thị sự kiện
            const SizedBox(height: 40),
          ],
        ),
      ),
    );
  }

  // Hàm giả lập để hiển thị danh sách sự kiện cho một ngày cụ thể
  // Trong ứng dụng thực tế, bạn sẽ lấy dữ liệu từ một nguồn nào đó (API, database, v.v.)
  Widget _buildEventListForSelectedDay(DateTime day) {
    // Ví dụ về một số sự kiện giả định
    final events = {
      DateTime.utc(2025, 7, 15): ['Tập luyện Sức mạnh Tối thượng', 'Yoga Vũ Trụ'],
      DateTime.utc(2025, 7, 16): ['Chạy bộ Sao Thổ', 'Bơi trong không gian'],
      // Thêm các sự kiện khác nếu cần
    };

    final selectedDateOnly = DateTime.utc(day.year, day.month, day.day);
    final eventsForDay = events[selectedDateOnly] ?? [];

    if (eventsForDay.isEmpty) {
      return Text(
        "Không có sự kiện nào vào ngày ${day.toLocal().toString().split(' ')[0]}.",
        style: const TextStyle(color: Colors.white70, fontSize: 16),
      );
    }

    return Column(
      children: eventsForDay.map((event) {
        return Card(
          color: Colors.white.withOpacity(0.15),
          margin: const EdgeInsets.symmetric(vertical: 8),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
          child: ListTile(
            leading: const Icon(Icons.fitness_center, color: Color(0xFFFFD740)),
            title: Text(
              event,
              style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
            ),
            subtitle: Text(
              'Lịch trình huấn luyện', // Hoặc chi tiết hơn về sự kiện
              style: const TextStyle(color: Colors.white70),
            ),
            onTap: () {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('Bạn đã nhấn vào: $event')),
              );
            },
          ),
        );
      }).toList(),
    );
  }
}
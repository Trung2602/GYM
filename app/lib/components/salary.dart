// salary.dart
import 'package:flutter/material.dart';

class Salary extends StatelessWidget {
  const Salary({super.key});

  @override
  Widget build(BuildContext context) {
    // Ví dụ dữ liệu lương
    final List<Map<String, dynamic>> salaryList = [
      {
        'month': 'Tháng 6/2025',
        'baseSalary': 8000000,
        'bonus': 1200000,
        'total': 9200000,
      },
      {
        'month': 'Tháng 7/2025',
        'baseSalary': 8000000,
        'bonus': 1000000,
        'total': 9000000,
      },
      {
        'month': 'Tháng 8/2025',
        'baseSalary': 8000000,
        'bonus': 1500000,
        'total': 9500000,
      },
    ];

    return Scaffold(
      backgroundColor: const Color(0xFF0F123A),
      appBar: AppBar(
        title: const Text('Bảng Lương'),
        backgroundColor: const Color(0xFF1A237E),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: ListView.builder(
          itemCount: salaryList.length,
          itemBuilder: (context, index) {
            final salary = salaryList[index];
            return Card(
              color: Colors.white.withOpacity(0.08),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(12),
              ),
              margin: const EdgeInsets.symmetric(vertical: 8),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      salary['month'],
                      style: const TextStyle(
                        color: Color(0xFFFFAB40),
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          'Lương cơ bản:',
                          style: TextStyle(color: Colors.white70),
                        ),
                        Text(
                          '${salary['baseSalary']} VND',
                          style: const TextStyle(color: Colors.white),
                        ),
                      ],
                    ),
                    const SizedBox(height: 4),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text('Thưởng:', style: TextStyle(color: Colors.white70)),
                        Text('${salary['bonus']} VND', style: const TextStyle(color: Colors.white)),
                      ],
                    ),
                    const Divider(color: Colors.white30, height: 16),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text('Tổng lương:', style: TextStyle(color: Colors.white70, fontWeight: FontWeight.bold)),
                        Text('${salary['total']} VND', style: const TextStyle(color: Color(0xFFFFD740), fontWeight: FontWeight.bold)),
                      ],
                    ),
                  ],
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}

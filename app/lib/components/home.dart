// home.dart
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../models/Account.dart';
import '../services/auth_service.dart';
// Import các màn hình con sau này
import 'profile.dart';
import 'day_off.dart';
import 'pay_customer.dart';
import 'salary.dart';
import 'staff_schedule.dart';
import 'login.dart';
import 'customer_schedule.dart';

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  int _selectedIndex = 0; // Index của item được chọn trên BottomNavigationBar
  final AuthService authService = AuthService();
  Account? savedAccount;
  List<Widget> _pages = [];

  @override
  void initState() {
    super.initState();
    _loadAccount();
  }

  Future<void> _loadAccount() async {
    final account = await authService.getSavedAccount();
    setState(() {
      savedAccount = account;

      if (savedAccount != null && savedAccount!.role == 'Customer') {
        _pages = [
          const _DashboardScreen(),
          const CustomerSchedule(),
          const PayCustomer(),
          Profile(account: savedAccount!),
        ];
      } else if (savedAccount != null && savedAccount!.role == 'Staff') {
        _pages = [
          const _DashboardScreen(),
          const StaffSchedule(),
          const Salary(),
          const DayOff(),
          Profile(account: savedAccount!),
        ];
      } else {
        _pages = [const _DashboardScreen()];
      }
    });
  }

  List<BottomNavigationBarItem> _buildBottomNavItems() {
    if (savedAccount == null) return [];

    if (savedAccount!.role == 'Customer') {
      return const [
        BottomNavigationBarItem(
          icon: Icon(Icons.dashboard),
          label: "Trang chủ",
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.calendar_today),
          label: "Lịch Trình",
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.payment),
          label: "Thanh Toán",
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.person_pin),
          label: "Hồ Sơ",
        ),
      ];
    } else if (savedAccount!.role == 'Staff') {
      return const [
        BottomNavigationBarItem(
          icon: Icon(Icons.dashboard),
          label: "Trang chủ",
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.access_time),
          label: "Ca Làm",
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.attach_money),
          label: "Bảng Lương",
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.beach_access),
          label: "Xin Nghỉ",
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.person_pin),
          label: "Hồ Sơ",
        ),
      ];
    } else {
      return const [];
    }
  }

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  List<Widget> _buildDrawerItems() {
    if (savedAccount == null) return [];

    List<Map<String, dynamic>> menuItems = [];

    if (savedAccount!.role == 'Customer') {
      menuItems = [
        {'icon': Icons.dashboard, 'title': 'Bảng Điều Khiển Thiên Hà', 'index': 0},
        {'icon': Icons.person, 'title': 'Hồ Sơ Phi Hành Gia', 'index': 2},
        {'icon': Icons.fitness_center, 'title': 'Gói Tập', 'index': -1},
        {'icon': Icons.message, 'title': 'Liên Lạc', 'index': -1},
        {'icon': Icons.calendar_today, 'title': 'Lịch Trình', 'index': -1},
      ];
    } else if (savedAccount!.role == 'Staff') {
      menuItems = [
        {'icon': Icons.dashboard, 'title': 'Bảng Điều Khiển Thiên Hà', 'index': 0},
        {'icon': Icons.person, 'title': 'Hồ Sơ Phi Hành Gia', 'index': 2},
        {'icon': Icons.access_time, 'title': 'Ca Làm', 'index': -1},
        {'icon': Icons.beach_access, 'title': 'Xin Nghỉ', 'index': -1},
        {'icon': Icons.attach_money, 'title': 'Bảng Lương', 'index': -1},
        {'icon': Icons.message, 'title': 'Liên Lạc', 'index': -1},
      ];
    }

    // Thêm luôn logout
    menuItems.add({'icon': Icons.logout, 'title': 'Rời khỏi Trạm Vũ Trụ', 'index': -1});

    return menuItems.map((item) {
      return _buildDrawerItem(item['icon'], item['title'], item['index']);
    }).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Màu nền tổng thể cho các khu vực không có ảnh nền
      backgroundColor: const Color(0xFF0F123A),

      appBar: AppBar(
        title: const Text(
          "GALACTIC FITNESS",
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.bold,
            letterSpacing: 1.5,
          ),
        ),
        backgroundColor: const Color(0xFF1A237E),
        elevation: 8,
        iconTheme: const IconThemeData(color: Colors.white), // Đặt màu icon Drawer/back button
        actions: [
          IconButton(
            onPressed: () {},
            icon: const Icon(Icons.notifications_none, color: Colors.white), // Thông báo
            tooltip: 'Tin nhắn từ Trung Tâm Điều Khiển',
          ),
          IconButton(
            onPressed: () {},
            icon: const Icon(Icons.search, color: Colors.white), // Tìm kiếm
            tooltip: 'Tìm kiếm trong Vũ Trụ',
          ),
        ],
      ),

      // Sidebar điều hướng (Drawer)
      drawer:savedAccount == null
          ? null
          : Drawer(
        backgroundColor: const Color(0xFF1A237E),
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            DrawerHeader(
              decoration: const BoxDecoration(color: Color(0xFF2C318F)),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  CircleAvatar(
                    radius: 30,
                    backgroundImage: (savedAccount?.avatar ?? '').isNotEmpty
                        ? NetworkImage(savedAccount!.avatar)
                        : null,
                    child: (savedAccount?.avatar ?? '').isEmpty
                        ? const Icon(Icons.person, size: 30, color: Colors.white)
                        : null,
                  ),
                  const SizedBox(height: 10),
                  Text(
                    savedAccount?.name ?? '',
                    style: const TextStyle(color: Colors.white, fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  Text(
                    savedAccount?.role ?? '',
                    style: const TextStyle(color: Colors.white70, fontSize: 14),
                  ),
                ],
              ),
            ),
            ..._buildDrawerItems(), // hiển thị menu dựa role
          ],
        ),
      ),

      body: savedAccount == null || _pages.isEmpty
          ? const Center(child: CircularProgressIndicator())
          : _pages[_selectedIndex],

      bottomNavigationBar: BottomNavigationBar(
        items: _buildBottomNavItems(),
        currentIndex: _selectedIndex,
        selectedItemColor: const Color(0xFFFFD740),
        unselectedItemColor: Colors.white70,
        backgroundColor: const Color(0xFF1A237E),
        onTap: _onItemTapped,
        type: BottomNavigationBarType.fixed,
        elevation: 10,
      ),

    );
  }

  // Hàm tiện ích để xây dựng các mục trong Drawer
  Widget _buildDrawerItem(IconData icon, String title, int index) {
    return ListTile(
      leading: Icon(icon, color: Colors.white70),
      title: Text(
        title,
        style: const TextStyle(color: Colors.white, fontSize: 16),
      ),
      onTap: () {
        Navigator.pop(context); // Đóng Drawer
        if (index != -1) { // Nếu là một item có trong BottomNavigationBar, chuyển trang
          _onItemTapped(index);
        } else {
          // Xử lý các mục khác của Drawer
          if (title == 'Rời khỏi Trạm Vũ Trụ') {
            // Logout
            authService.logout().then((_) {
              Navigator.pushReplacement(
                context,
                MaterialPageRoute(builder: (context) => const Login()), // Login screen
              );
            });
          } else {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(content: Text('Chức năng "$title" đang được phát triển...')),
            );
          }
        }
      },
    );
  }

}

// ==========================================================
// MÀN HÌNH DASHBOARD (TRUNG TÂM ĐIỀU KHIỂN VŨ TRỤ)
// ==========================================================
class _DashboardScreen extends StatelessWidget {
  const _DashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        image: DecorationImage(
          image: AssetImage(
              'assets/images/background.jpg'), // Ảnh nền vũ trụ cho Dashboard
          fit: BoxFit.cover,
          opacity: 0.7, // Giảm độ trong suốt
        ),
      ),
      child: LayoutBuilder(
        builder: (context, constraints) {
          return SingleChildScrollView(
            padding: const EdgeInsets.all(20.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Phần chào mừng
                Text(
                  "Chào mừng đến với",
                  style: TextStyle(
                      color: Colors.white,
                      fontSize: 24,
                      fontWeight: FontWeight.w300,
                      shadows: [
                        Shadow(blurRadius: 10.0, color: Colors.black54, offset: Offset(2, 2))
                      ]
                  ),
                ),
                Text(
                  "GALACTIC FITNESS!",
                  style: TextStyle(
                      color: Color(0xFFFFAB40), // Màu cam mặt trời
                      fontSize: 38,
                      fontWeight: FontWeight.bold,
                      shadows: [
                        Shadow(blurRadius: 10.0, color: Colors.black, offset: Offset(3, 3))
                      ]
                  ),
                ),
                const SizedBox(height: 15), // Giảm khoảng cách
                // Phần ngày giờ
                Row(
                  children: [
                    Icon(Icons.calendar_today, color: Colors.white70, size: 20),
                    SizedBox(width: 8),
                    Text(
                      "Hôm nay: ${_getFormattedDate()}", // Ngày hiện tại
                      style: TextStyle(color: Colors.white70, fontSize: 16),
                    ),
                    SizedBox(width: 20),
                    Icon(Icons.access_time, color: Colors.white70, size: 20),
                    SizedBox(width: 8),
                    Text(
                      " ${TimeOfDay.now().format(context)}", // Giờ hiện tại
                      style: TextStyle(color: Colors.white70, fontSize: 16),
                    ),
                  ],
                ),
                const SizedBox(height: 30), // Giảm khoảng cách

                // Phần giới thiệu phòng gym (tương tự như đoạn đầu video)
                const Text(
                  "Về Galactic Fitness Center",
                  style: TextStyle(
                      color: Colors.white,
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                      shadows: [
                        Shadow(blurRadius: 5.0, color: Colors.black54, offset: Offset(1, 1))
                      ]
                  ),
                ),
                const SizedBox(height: 10), // Giảm khoảng cách
                const Text(
                  "Trung tâm thể dục thể thao thể thao hàng đầu vũ trụ, trang bị bởi công nghệ tiên tiến nhất, và đội ngũ huấn luyện viên là những phi hành gia giàu kinh nghiệm.",
                  style: TextStyle(color: Colors.white70, fontSize: 16),
                ),
                const SizedBox(height: 20), // Giảm khoảng cách

                // Các thẻ thông tin dịch vụ
                GridView.count(
                  crossAxisCount: 2, // 2 cột
                  crossAxisSpacing: 15,
                  mainAxisSpacing: 15,
                  shrinkWrap: true, // Quan trọng để GridView không bị lỗi khi nằm trong SingleChildScrollView
                  physics: const NeverScrollableScrollPhysics(), // Không cho GridView cuộn riêng
                  children: [
                    _buildInfoCard(
                      'Cơ Sở Vật Chất Hiện đại',
                      'Hệ thống tập luyện được thiết kế với công nghệ hiện đại nhất vũ trụ.',
                      Icons.space_dashboard,
                    ),
                    _buildInfoCard(
                      'Đội Ngũ Huấn Luyện Viên Chuyên Nghiệp',
                      'Đội ngũ phi hành gia hướng dẫn chuyên nghiệp, sẵn sàng đưa bạn vươn tới giới hạn.',
                      Icons.group,
                    ),
                    _buildInfoCard(
                      'Trải Nghiệm Tập Luyện Hiệu Quả',
                      'Dịch vụ mang đến sự thoải mái và hiệu quả tối đa cho từng buổi tập.',
                      Icons.star,
                    ),
                    _buildInfoCard(
                      'Năng Lượng Bền Bỉ',
                      'Mỗi bài tập đều được thiết kế để tối ưu hóa năng lượng và sức bền của bạn.',
                      Icons.wb_sunny,
                    ),
                  ],
                ),
                const SizedBox(height: 30), // Giảm khoảng cách

                // Phần "Khóa học đang diễn ra" hoặc "Lịch trình hôm nay"
                const Text(
                  "Lịch Trình Quỹ Đạo Sắp Tới",
                  style: TextStyle(
                      color: Colors.white,
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                      shadows: [
                        Shadow(blurRadius: 5.0, color: Colors.black54, offset: Offset(1, 1))
                      ]
                  ),
                ),
                const SizedBox(height: 10), // Giảm khoảng cách
                _buildScheduleCard(
                  'Thiền Định Thiên Hà',
                  'Yoga & Meditation | PT Laura (Học viên Vũ Trụ)',
                  'Thứ 2, 4, 6 | 6:00 AM - 7:00 AM',
                  'Khám phá sự bình yên trong vũ trụ nội tại.',
                ),
                const SizedBox(height: 10), // Giảm khoảng cách
                _buildScheduleCard(
                  'Năng Lượng Bình Minh Vũ Trụ',
                  'Cardio & Strength | PT John (Học viên Vũ Trụ)',
                  'Thứ 3, 5, 7 | 7:00 AM - 8:00 AM',
                  'Bùng cháy năng lượng như mặt trời mới mọc.',
                ),
                const SizedBox(height: 30), // Giảm khoảng cách cuối cùng
              ],
            ),
          );
        },
      ),
    );
  }

  // Hàm tiện ích để lấy ngày hiện tại định dạng đẹp
  String _getFormattedDate() {
    final now = DateTime.now();
    final formatter = DateFormat('dd/MM/yyyy');
    return formatter.format(now);
  }

  // Hàm tiện ích để tạo các thẻ thông tin
  Widget _buildInfoCard(String title, String description, IconData icon) {
    return Card(
      color: Colors.white.withOpacity(0.08), // Nền trong suốt nhẹ
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
      elevation: 5,
      child: Padding(
        padding: const EdgeInsets.all(12.0), // Giảm padding
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 28, color: const Color(0xFFFFD740)), // Giảm kích thước icon
            const SizedBox(height: 5), // Giảm khoảng cách
            Text(
              title,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 15, // Giảm kích thước font
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 3), // Giảm khoảng cách
            Text(
              description,
              style: const TextStyle(color: Colors.white70, fontSize: 12), // Giảm kích thước font
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
            ),
          ],
        ),
      ),
    );
  }

  // Hàm tiện ích để tạo các thẻ lịch trình
  Widget _buildScheduleCard(String title, String subtitle, String time, String description) {
    return Card(
      color: Colors.white.withOpacity(0.1), // Nền trong suốt nhẹ
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
      elevation: 7,
      child: Padding(
        padding: const EdgeInsets.all(15.0), // Giảm padding
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              title,
              style: const TextStyle(
                color: Color(0xFFFFAB40), // Màu cam mặt trời
                fontSize: 18, // Giảm kích thước font
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 4), // Giảm khoảng cách
            Text(
              subtitle,
              style: const TextStyle(color: Colors.white, fontSize: 14), // Giảm kích thước font
            ),
            const SizedBox(height: 4), // Giảm khoảng cách
            Row(
              children: [
                Icon(Icons.access_time, color: Colors.white60, size: 16), // Giảm kích thước icon
                SizedBox(width: 5),
                Text(
                  time,
                  style: const TextStyle(color: Colors.white60, fontSize: 13), // Giảm kích thước font
                ),
              ],
            ),
            const SizedBox(height: 8), // Giảm khoảng cách
            Text(
              description,
              style: const TextStyle(color: Colors.white70, fontSize: 13), // Giảm kích thước font
            ),
            const SizedBox(height: 10), // Giảm khoảng cách
            Align(
              alignment: Alignment.bottomRight,
              child: ElevatedButton(
                onPressed: () {
                  // Xử lý khi nhấn nút "Xem chi tiết"
                },
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFF2C318F), // Màu xanh tím nhạt hơn
                  foregroundColor: Colors.white,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10),
                  ),
                  padding: const EdgeInsets.symmetric(horizontal: 15, vertical: 8), // Giảm padding nút
                  textStyle: const TextStyle(fontSize: 13), // Giảm kích thước font nút
                ),
                child: const Text("Xem Chi Tiết Quỹ Đạo"),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

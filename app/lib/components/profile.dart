// profile.dart
import 'package:flutter/material.dart';
import '../models/Account.dart';

class Profile extends StatelessWidget {
  final Account account;
  const Profile({super.key, required this.account});


  @override
  Widget build(BuildContext context) {
    return Container(
      // Optional: Add a space-themed background for consistency
      decoration: const BoxDecoration(
        image: DecorationImage(
          image: AssetImage('assets/images/background.jpg'), // Ensure this path is correct and image exists
          fit: BoxFit.cover,
          opacity: 0.7,
        ),
      ),
      child: Center(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              CircleAvatar(
                radius: 60,
                backgroundImage: account.avatar.isNotEmpty
                    ? NetworkImage(account.avatar)
                    : null,
                child: account.avatar.isEmpty
                    ? const Icon(Icons.person, size: 80, color: Colors.white)
                    : null,
              ),
              const SizedBox(height: 20),
              const Text(
                'Hồ Sơ Phi Hành Gia',
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 28,
                    fontWeight: FontWeight.bold,
                    shadows: [
                      Shadow(blurRadius: 10.0, color: Colors.black, offset: Offset(2, 2))
                    ]
                ),
              ),
              const SizedBox(height: 10),
              const Text(
                'Chào mừng, Phi Hành Gia! Bạn đang ở trang hồ sơ cá nhân.',
                textAlign: TextAlign.center,
                style: TextStyle(
                  color: Colors.white70,
                  fontSize: 16,
                ),
              ),
              const SizedBox(height: 30),
              // You can add more profile details here later
              ElevatedButton.icon(
                onPressed: () {
                  // TODO: Implement navigation to edit profile or other actions
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text('Chỉnh sửa hồ sơ đang được phát triển...')),
                  );
                },
                icon: const Icon(Icons.edit, color: Colors.white),
                label: const Text('Chỉnh Sửa Hồ Sơ'),
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFF2C318F),
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
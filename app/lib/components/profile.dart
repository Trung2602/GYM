import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/Account.dart';
import '../models/AccountProvider.dart';

import 'dart:io';
import 'package:image_picker/image_picker.dart';

class Profile extends StatefulWidget {
  const Profile({super.key});

  @override
  State<Profile> createState() => _Profile();
}

class _Profile extends State<Profile> {
  late TextEditingController nameController;
  late TextEditingController mailController;
  late TextEditingController avatarController;
  late TextEditingController passwordController;
  late TextEditingController usernameController;

  DateTime? birthday;
  bool gender = true; // true = Nam, false = Nữ
  String role = "Customer";
  bool isActive = true;

  File? _selectedImage;
  final picker = ImagePicker();

  Future<void> _pickImage(ImageSource source) async {
    final pickedFile = await picker.pickImage(source: source);
    if (pickedFile != null) {
      setState(() {
        _selectedImage = File(pickedFile.path);
      });
    }
  }

  @override
  void initState() {
    super.initState();
    final account = Provider.of<AccountProvider>(context, listen: false).account;
    usernameController = TextEditingController(text: account?.username ?? '');
    nameController = TextEditingController(text: account?.name ?? '');
    mailController = TextEditingController(text: account?.mail ?? '');
    avatarController = TextEditingController(text: account?.avatar ?? '');
    passwordController = TextEditingController(text: account?.password ?? '');
    birthday = account?.birthday;
    gender = account?.gender ?? true;
    role = account?.role ?? "Customer";
    isActive = account?.isActive ?? true;
  }

  @override
  void dispose() {
    usernameController.dispose();
    nameController.dispose();
    mailController.dispose();
    avatarController.dispose();
    passwordController.dispose();
    super.dispose();
  }

  void _showSaveDialog(BuildContext context) {
    final account = Provider.of<AccountProvider>(context, listen: false).account;
    String? errorText;

    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Xác nhận mật khẩu"),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            if (errorText != null) // chỉ hiện khi có lỗi
              Padding(
                padding: const EdgeInsets.only(bottom: 8.0),
                child: Text(
                  errorText!,
                  style: const TextStyle(color: Colors.red),
                ),
              ),
            TextField(
              controller: passwordController,
              obscureText: true,
              decoration: const InputDecoration(
                labelText: "Nhập mật khẩu",
                border: OutlineInputBorder(),
              ),
            ),
          ],
        ),
        actions: [
          TextButton(
            child: const Text("Hủy"),
            onPressed: () => Navigator.pop(ctx),
          ),
          ElevatedButton(
            child: const Text("Lưu"),
            onPressed: () {
              final password = passwordController.text.trim();

              if (password.isEmpty) {
                setState(() {
                  errorText = "Vui lòng nhập mật khẩu";
                });
                return;
              }

              if (password != account?.password) { // ✅ so với password trong account
                setState(() {
                  errorText = "Mật khẩu không chính xác";
                });
                return;
              }

              // ✅ TODO: Gọi API update profile ở đây
              Navigator.pop(ctx); // đóng dialog
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text("Thông tin đã được lưu")),
              );
            },
          ),
        ],
      ),
    );
  }

  void _showChangePasswordDialog(BuildContext context, Account account) {
    final account = Provider.of<AccountProvider>(context, listen: false).account;
    final oldPassController = TextEditingController();
    final newPassController = TextEditingController();
    final confirmPassController = TextEditingController();

    String? errorText; // chỗ lưu thông báo lỗi

    showDialog(
      context: context,
      builder: (ctx) {
        return StatefulBuilder(
          builder: (ctx, setState) {
            return AlertDialog(
              title: const Text("Đổi mật khẩu"),
              content: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  if (errorText != null) // chỉ hiện khi có lỗi
                    Padding(
                      padding: const EdgeInsets.only(bottom: 8.0),
                      child: Text(
                        errorText!,
                        style: const TextStyle(color: Colors.red),
                      ),
                    ),
                  TextField(
                    controller: oldPassController,
                    obscureText: true,
                    decoration: const InputDecoration(
                      labelText: "Mật khẩu cũ",
                      prefixIcon: Icon(Icons.lock_outline),
                    ),
                  ),
                  TextField(
                    controller: newPassController,
                    obscureText: true,
                    decoration: const InputDecoration(
                      labelText: "Mật khẩu mới",
                      prefixIcon: Icon(Icons.lock),
                    ),
                  ),
                  TextField(
                    controller: confirmPassController,
                    obscureText: true,
                    decoration: const InputDecoration(
                      labelText: "Xác nhận mật khẩu mới",
                      prefixIcon: Icon(Icons.lock_reset),
                    ),
                  ),
                ],
              ),
              actions: [
                TextButton(
                  child: const Text("Hủy"),
                  onPressed: () => Navigator.pop(ctx),
                ),
                ElevatedButton(
                  child: const Text("Lưu"),
                  onPressed: () async {
                    final oldPass = oldPassController.text.trim();
                    final newPass = newPassController.text.trim();
                    final confirmPass = confirmPassController.text.trim();

                    if (oldPass.isEmpty || newPass.isEmpty || confirmPass.isEmpty) {
                      setState(() => errorText = "Vui lòng nhập đầy đủ thông tin");
                      return;
                    }

                    if (oldPass != account?.password) {
                      setState(() => errorText = "Mật khẩu cũ không chính xác");
                      return;
                    }

                    if (newPass != confirmPass) {
                      setState(() => errorText = "Mật khẩu mới không khớp");
                      return;
                    }

                    // ✅ TODO: Gọi API đổi mật khẩu
                    // await ApiService.changePassword(account.id, newPass);

                    Navigator.pop(ctx);
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text("Mật khẩu đã được đổi")),
                    );
                  },
                ),
              ],
            );
          },
        );
      },
    );
  }



  @override
  Widget build(BuildContext context) {
    final account = Provider.of<AccountProvider>(context, listen: false).account;

    if (account == null) {
      return const Center(child: Text("Chưa có tài khoản"));
    }

    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/images/background.jpg'),
            fit: BoxFit.cover,
            opacity: 0.7,
          ),
        ),
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // Avatar + nút chọn ảnh
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircleAvatar(
                    radius: 60,
                    backgroundImage: _selectedImage != null
                        ? FileImage(_selectedImage!)
                        : (account != null && account.avatar.isNotEmpty
                        ? NetworkImage(account.avatar)
                        : null) as ImageProvider?,
                    child: (_selectedImage == null &&
                        (account == null || account.avatar.isEmpty))
                        ? const Icon(Icons.person, size: 80, color: Colors.white70)
                        : null,
                  ),
                  const SizedBox(height: 20, width: 12),
                  ElevatedButton.icon(
                    onPressed: () {
                      showModalBottomSheet(
                        context: context,
                        builder: (_) => SafeArea(
                          child: Wrap(
                            children: [
                              ListTile(
                                leading: const Icon(Icons.photo),
                                title: const Text("Chọn từ thư viện"),
                                onTap: () {
                                  Navigator.pop(context);
                                  _pickImage(ImageSource.gallery);
                                },
                              ),
                              ListTile(
                                leading: const Icon(Icons.camera_alt),
                                title: const Text("Chụp ảnh"),
                                onTap: () {
                                  Navigator.pop(context);
                                  _pickImage(ImageSource.camera);
                                },
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                    icon: const Icon(Icons.image),
                    label: const Text("Chọn ảnh"),
                  ),
                ],
              ),
              const SizedBox(height: 20),

              // Card chứa form
              Card(
                color: Colors.white.withOpacity(0.85),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(16),
                ),
                elevation: 8,
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    children: [
                      // Username
                      TextField(
                        controller: usernameController,
                        //readOnly: true,
                        decoration: const InputDecoration(
                          labelText: "Username",
                          prefixIcon: Icon(Icons.account_circle),
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 15),

                      // Full name
                      TextField(
                        controller: nameController,
                        decoration: const InputDecoration(
                          labelText: "Tên",
                          prefixIcon: Icon(Icons.person),
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 15),

                      // Email
                      TextField(
                        controller: mailController,
                        decoration: const InputDecoration(
                          labelText: "Email",
                          prefixIcon: Icon(Icons.mail),
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 15),

                      // Birthday
                      ListTile(
                        title: Text(
                          "Ngày sinh: ${birthday != null ? birthday.toString().split(' ')[0] : 'Chưa chọn'}",
                          style: const TextStyle(fontSize: 16),
                        ),
                        trailing: const Icon(Icons.calendar_today),
                        onTap: () async {
                          DateTime? picked = await showDatePicker(
                            context: context,
                            initialDate: birthday ?? DateTime(2000, 1, 1),
                            firstDate: DateTime(1900),
                            lastDate: DateTime.now(),
                          );
                          if (picked != null) {
                            setState(() => birthday = picked);
                          }
                        },
                      ),
                      const SizedBox(height: 15),

                      // Gender
                      SwitchListTile(
                        value: gender,
                        onChanged: (val) => setState(() => gender = val),
                        title: Text("Giới tính: ${gender ? "Nam" : "Nữ"}"),
                      ),
                      const SizedBox(height: 15),

                      // Role
                      TextField(
                        readOnly: true,
                        controller: TextEditingController(text: role),
                        decoration: const InputDecoration(
                          labelText: "Vai trò",
                          prefixIcon: Icon(Icons.security),
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 15),

                      // Active
                      SwitchListTile(
                        value: isActive,
                        onChanged: (val) => setState(() => isActive = val),
                        title: const Text("Kích hoạt tài khoản"),
                      ),
                      const SizedBox(height: 25),

                      // Save button
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          // Nút đổi mật khẩu
                          ElevatedButton.icon(
                            onPressed: () {
                              _showChangePasswordDialog(context, account);
                            },
                            icon: const Icon(Icons.lock, color: Colors.white),
                            label: const Text("Đổi Mật Khẩu"),
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.orange.shade700,
                              foregroundColor: Colors.white,
                              padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(12),
                              ),
                            ),
                          ),

                          // Nút lưu thông tin
                          ElevatedButton.icon(
                            onPressed: () {
                              // TODO: gọi API update account KHÔNG đổi mật khẩu
                              _showSaveDialog(context);
                              ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(content: Text('Hồ sơ đã được lưu!')),
                              );
                            },
                            icon: const Icon(Icons.save, color: Colors.white),
                            label: const Text("Lưu Thay Đổi"),
                            style: ElevatedButton.styleFrom(
                              backgroundColor: const Color(0xFF2C318F),
                              foregroundColor: Colors.white,
                              padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(12),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ],
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


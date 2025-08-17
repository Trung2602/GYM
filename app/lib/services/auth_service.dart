// lib/services/auth_service.dart
import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../api.dart';
import '../models/Account.dart';
import 'package:http_parser/http_parser.dart';


class AuthService {
  Future<Account?> login(String username, String password) async {
    final prefs = await SharedPreferences.getInstance();

    final response = await http.post(
      Uri.parse(Api.login),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode({"username": username, "password": password}),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final token = data["token"];

      if (token != null) {
        // Lưu token
        await prefs.setString("token", token);

        // Gọi API /account/me để lấy thông tin account
        final meResponse = await http.get(
          Uri.parse(Api.me),
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer $token",
          },
        );

        if (meResponse.statusCode == 200) {
          final userData = jsonDecode(meResponse.body);
          final account = Account.fromJson(userData);

          // Lưu account JSON để dùng lại
          await prefs.setString("account", jsonEncode(account.toJson()));

          return account;
        }
      }
    } else if (response.statusCode == 401) {
      // Sai username hoặc password
      print("Sai username hoặc password");
      return null;
    } else {
      // Có thể log error để debug
      print("Login failed: ${response.statusCode} - ${response.body}");
    }

    return null;
  }

  Future<String?> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString("token");
  }

  Future<Account?> getSavedAccount() async {
    final prefs = await SharedPreferences.getInstance();
    final jsonString = prefs.getString("account");
    if (jsonString != null) {
      final Map<String, dynamic> data = jsonDecode(jsonString);
      return Account.fromJson(data);
    }
    return null;
  }

  Future<Account?> registerWithImage(Account account, File? imageFile) async {
    var uri = Uri.parse(Api.register);

    var request = http.MultipartRequest("POST", uri);

    // Thêm từng field text tương ứng với @ModelAttribute
    request.fields['username'] = account.username ?? '';
    request.fields['password'] = account.password ?? '';
    request.fields['name'] = account.name ?? '';
    request.fields['birthday'] = account.birthday != null
        ? DateFormat('yyyy-MM-dd').format(account.birthday!)
        : '';
    request.fields['gender'] = account.gender != null ? (account.gender! ? 'true' : 'false') : '';
    request.fields['role'] = account.role ?? '';
    request.fields['mail'] = account.mail ?? '';
    request.fields['isActive'] = account.isActive != null ? (account.isActive! ? 'true' : 'false') : '';

    // Thêm file nếu có
    if (imageFile != null) {
      request.files.add(
        await http.MultipartFile.fromPath('image', imageFile.path),
      );
    }

    // Gửi request
    var streamedResponse = await request.send();
    var response = await http.Response.fromStream(streamedResponse);

    if (response.statusCode == 200 || response.statusCode == 201) {
      return Account.fromJson(jsonDecode(response.body));
    } else {
      throw Exception("Đăng ký thất bại: ${response.body}");
    }
  }

  // Đăng ký + đăng nhập tự động
  Future<Account?> registerAndLogin(Account account, File? imageFile) async {
    // 1. Thực hiện đăng ký
    var registeredAccount = await registerWithImage(account, imageFile);

    if (registeredAccount != null) {
      // 2. Sau khi đăng ký thành công, gọi login để lấy token
      return await login(account.username ?? '', account.password ?? '');
    }

    return null;
  }

  Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove("token");
    await prefs.remove("account"); // xoá luôn account khi logout
  }
}

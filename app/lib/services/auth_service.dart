// lib/services/auth_service.dart
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../api.dart';
import '../models/Account.dart';

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

  Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove("token");
    await prefs.remove("account"); // xoá luôn account khi logout
  }
}

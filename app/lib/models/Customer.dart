import 'account.dart';

class Customer extends Account {
  final DateTime? expiryDate;

  Customer({
    required int id,
    required String username,
    String? password,
    required String name,
    DateTime? birthday,
    bool? gender,
    required String role,
    required String mail,
    required String avatar,
    bool? isActive,
    required this.expiryDate,
  }) : super(
    id: id,
    username: username,
    password: password,
    name: name,
    birthday: birthday,
    gender: gender,
    role: role,
    mail: mail,
    avatar: avatar,
    isActive: isActive,
  );

  factory Customer.fromJson(Map<String, dynamic> json) {
    return Customer(
      id: json['id'],
      username: json['username'],
      password: json['password'],
      name: json['name'] ?? '',
      birthday: json['birthday'] != null ? DateTime.parse(json['birthday']) : null,
      gender: json['gender'],
      role: json['role'] ?? '',
      mail: json['mail'] ?? '',
      avatar: json['avatar'] ?? '',
      isActive: json['isActive'],
      expiryDate: json['expiryDate'] != null ? DateTime.parse(json['expiryDate']) : null,
    );
  }

  @override
  Map<String, dynamic> toJson() {
    final data = super.toJson();
    data['expiryDate'] = expiryDate?.toIso8601String();
    return data;
  }
}

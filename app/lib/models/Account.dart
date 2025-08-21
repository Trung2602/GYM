class Account {
  final int id;
  final String username;
  final String? password; // có thể null
  final String name;
  final DateTime? birthday;
  final bool? gender;
  final String role;
  final String mail;
  final String avatar;
  final bool? isActive;

  Account({
    required this.id,
    required this.username,
    this.password,
    required this.name,
    this.birthday,
    this.gender,
    required this.role,
    required this.mail,
    required this.avatar,
    this.isActive,
  });

  factory Account.fromJson(Map<String, dynamic> json) {
    return Account(
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
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'username': username,
      'password': password,
      'name': name,
      'birthday': birthday?.toIso8601String(),
      'gender': gender,
      'role': role,
      'mail': mail,
      'avatar': avatar,
      'isActive': isActive,
    };
  }
}

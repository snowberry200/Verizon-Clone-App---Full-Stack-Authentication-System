import 'package:my_verizon/models/entity_dtos/user_security_question_dto.dart';

class UserDTO {
  final int id;
  final String name;
  final String email;
  final String? role;
  final bool isActive;
  final UserSecurityQuestionDTO userSecurityQuestionDTO;
  final String accessToken;
  final DateTime? createdAt;
  final DateTime? lastLogin;

  UserDTO({
    required this.id,
    required this.name,
    required this.email,
    required this.role,
    required this.isActive,
    this.createdAt,
    this.lastLogin,
    required this.userSecurityQuestionDTO,
    required this.accessToken,
  });

  // Factory method for creating a UserDTO from JSON
  factory UserDTO.fromJson(Map<String, dynamic> json) {
    return UserDTO(
      id: json['id'],
      name: json['name'],
      email: json['email'],
      role: json['role'],
      isActive: json['isActive'],
      createdAt:
          json['createdAt'] != null ? DateTime.parse(json['createdAt']) : null,
      lastLogin:
          json['lastLogin'] != null ? DateTime.parse(json['lastLogin']) : null,
      userSecurityQuestionDTO: UserSecurityQuestionDTO.fromJson(
        json['userSecurityQuestion'],
      ),
      accessToken: json['accessToken'] ?? '',
    );
  }

  // Method for TO JSON (serialization)
  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['name'] = name;
    data['email'] = email;
    data['role'] = role;
    data['isActive'] = isActive;
    if (createdAt != null) {
      data['createdAt'] = createdAt!.toIso8601String();
    }
    if (lastLogin != null) {
      data['lastLogin'] = lastLogin!.toIso8601String();
    }
    data['userSecurityQuestion'] = userSecurityQuestionDTO.toJson();
    data['accessToken'] = accessToken;
    return data;
  }
}

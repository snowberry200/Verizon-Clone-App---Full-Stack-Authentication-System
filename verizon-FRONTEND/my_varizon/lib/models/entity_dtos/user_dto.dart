import 'package:my_verizon/models/entity_dtos/user_security_question_dto.dart';

class UserDTO {
  final int id;
  final String name;
  final String email;
  final String statusCode;
  final String? role;
  final String verificationToken;
  final DateTime verificationTokenExpiry;
  final DateTime verifiedAt;
  final UserSecurityQuestionDTO userSecurityQuestionDTO;
  final String accessToken;
  final DateTime? createdAt;
  final DateTime? lastLogin;

  UserDTO({
    required this.id,
    required this.name,
    required this.email,
    required this.role,
    this.createdAt,
    this.lastLogin,
    required this.userSecurityQuestionDTO,
    required this.accessToken,
    required this.statusCode,
    required this.verificationToken,
    required this.verificationTokenExpiry,
    required this.verifiedAt,
  });

  // Factory method for creating a UserDTO from JSON
  factory UserDTO.fromJson(Map<String, dynamic> json) {
    return UserDTO(
      id: json['id'],
      name: json['name'],
      email: json['email'],
      role: json['role'],
      createdAt:
          json['createdAt'] != null ? DateTime.parse(json['createdAt']) : null,
      lastLogin:
          json['lastLogin'] != null ? DateTime.parse(json['lastLogin']) : null,
      userSecurityQuestionDTO: UserSecurityQuestionDTO.fromJson(
        json['userSecurityQuestion'],
      ),
      accessToken: json['accessToken'] ?? '',
      statusCode: json['statusCode'] ?? '',
      verificationToken: json['verificationToken'],
      verificationTokenExpiry:
          json['verificationTokenExpiry'] != null
              ? DateTime.parse(json['verificationTokenExpiry'])
              : DateTime.now(), // or handle null appropriately
      verifiedAt:
          json['verifiedAt'] != null
              ? DateTime.parse(json['verifiedAt'])
              : DateTime.now(), // or handle null appropriately
    );
  }

  // Method for TO JSON (serialization)
  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['name'] = name;
    data['email'] = email;
    data['statusCode'] = statusCode;
    data['role'] = role;
    if (createdAt != null) {
      data['createdAt'] = createdAt!.toIso8601String();
    }
    if (lastLogin != null) {
      data['lastLogin'] = lastLogin!.toIso8601String();
    }
    data['userSecurityQuestion'] = userSecurityQuestionDTO.toJson();
    data['verificationToken'] = verificationToken;
    data['verificationTokenExpiry'] = verificationTokenExpiry.toIso8601String();
    data['verifiedAt'] = verifiedAt.toIso8601String();
    data['accessToken'] = accessToken;
    return data;
  }
}

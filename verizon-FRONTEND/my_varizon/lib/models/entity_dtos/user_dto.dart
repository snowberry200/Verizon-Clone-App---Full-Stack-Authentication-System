// models/entity_dtos/user_dto.dart
class UserDTO {
  final int id;
  final String email;
  final String name;
  final String statusCode;
  final DateTime? createdAt;
  final DateTime? lastLogin;
  final DateTime? verifiedAt;
  final String? accessToken;
  final String? verificationToken;
  final DateTime? verificationTokenExpiry;

  UserDTO({
    required this.id,
    required this.email,
    required this.name,
    required this.statusCode,
    this.createdAt,
    this.lastLogin,
    this.verifiedAt,
    this.accessToken,
    this.verificationToken,
    this.verificationTokenExpiry,
  });

  factory UserDTO.empty() {
    return UserDTO(
      id: 0,
      email: '',
      name: '',
      statusCode: '',
      createdAt: null,
      lastLogin: null,
      verifiedAt: null,
      accessToken: null,
      verificationToken: null,
      verificationTokenExpiry: null,
    );
  }

  factory UserDTO.fromJson(Map<String, dynamic> json) {
    return UserDTO(
      id: json['id'],
      email: json['email'] ?? '',
      name: json['name'] ?? '',
      statusCode: json['statusCode'] ?? '',
      createdAt:
          json['createdAt'] != null ? DateTime.parse(json['createdAt']) : null,
      lastLogin:
          json['lastLogin'] != null ? DateTime.parse(json['lastLogin']) : null,
      verifiedAt:
          json['verifiedAt'] != null
              ? DateTime.parse(json['verifiedAt'])
              : null,
      accessToken: json['accessToken'],
      verificationToken: json['verificationToken'],
      verificationTokenExpiry:
          json['verificationTokenExpiry'] != null
              ? DateTime.parse(json['verificationTokenExpiry'])
              : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'email': email,
      'name': name,
      'statusCode': statusCode,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (lastLogin != null) 'lastLogin': lastLogin!.toIso8601String(),
      if (verifiedAt != null) 'verifiedAt': verifiedAt!.toIso8601String(),
      if (accessToken != null) 'accessToken': accessToken,
      if (verificationToken != null) 'verificationToken': verificationToken,
      if (verificationTokenExpiry != null)
        'verificationTokenExpiry': verificationTokenExpiry!.toIso8601String(),
    };
  }
}

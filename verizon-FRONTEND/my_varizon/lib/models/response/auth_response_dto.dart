import 'package:my_verizon/models/response/security_data_response_dto.dart';
import 'package:my_verizon/models/entity_dtos/user_dto.dart';

class AuthResponseDTO {
  final UserDTO userDTO;
  final String accessToken;
  final String message;
  final String statusCode;
  final bool requiresVerification;
  final String emailVerificationToken;
  final UserSecurityDataResponseDTO? userSecurityDataResponseDTO;
  final DateTime? lastLogin;
  final DateTime? createdAt;
  final DateTime? verifiedAt;

  AuthResponseDTO({
    required this.userDTO,
    required this.accessToken,
    required this.message,
    required this.statusCode,
    required this.requiresVerification,
    required this.emailVerificationToken,
    this.userSecurityDataResponseDTO,
    this.lastLogin,
    this.createdAt,
    this.verifiedAt,
  });

  Map<String, dynamic> toJson() {
    return {
      'user': userDTO.toJson(),
      'accessToken': accessToken,
      'message': message,
      'statusCode': statusCode,
      'requiresVerification': requiresVerification,
      'verificationToken': emailVerificationToken,
      if (userSecurityDataResponseDTO != null)
        'userSecurityDataResponseDTO': userSecurityDataResponseDTO!.toJson(),
      if (lastLogin != null) 'lastLogin': lastLogin!.toIso8601String(),
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (verifiedAt != null) 'verifiedAt': verifiedAt!.toIso8601String(),
    };
  }

  factory AuthResponseDTO.fromJson(Map<String, dynamic> json) {
    return AuthResponseDTO(
      userDTO: UserDTO.fromJson(json['user']),
      accessToken: json['accessToken'] ?? '',
      message: json['message'] ?? '',
      statusCode: json['statusCode'] ?? '',
      requiresVerification: json['requiresVerification'] ?? false,
      emailVerificationToken: json['verificationToken'] ?? '',
      userSecurityDataResponseDTO:
          json['userSecurityDataResponseDTO'] != null
              ? UserSecurityDataResponseDTO.fromJson(
                json['userSecurityDataResponseDTO'],
              )
              : null,
      lastLogin:
          json['lastLogin'] != null ? DateTime.parse(json['lastLogin']) : null,
      createdAt:
          json['createdAt'] != null ? DateTime.parse(json['createdAt']) : null,
      verifiedAt:
          json['verifiedAt'] != null
              ? DateTime.parse(json['verifiedAt'])
              : null,
    );
  }
}

class ResponseBuilder {
  UserDTO? _user;
  final String _accessToken;
  final String _message;
  String _statusCode = '';
  bool _requiresVerification = false;
  String _verificationToken = '';
  UserSecurityDataResponseDTO? _userSecurityDataResponseDTO;
  DateTime? _lastLogin;
  DateTime? _createdAt;
  DateTime? _verifiedAt;

  // Constructor for required primitive params
  ResponseBuilder({required String accessToken, required String message})
    : _accessToken = accessToken,
      _message = message;

  // Method chaining for optional params
  ResponseBuilder withStatusCode(String statusCode) {
    _statusCode = statusCode;
    return this;
  }

  ResponseBuilder withRequiresVerification(bool requiresVerification) {
    _requiresVerification = requiresVerification;
    return this;
  }

  ResponseBuilder withVerificationToken(String verificationToken) {
    _verificationToken = verificationToken;
    return this;
  }

  ResponseBuilder withLastLogin(DateTime lastLogin) {
    _lastLogin = lastLogin;
    return this;
  }

  ResponseBuilder withCreatedAt(DateTime createdAt) {
    _createdAt = createdAt;
    return this;
  }

  ResponseBuilder withVerifiedAt(DateTime verifiedAt) {
    _verifiedAt = verifiedAt;
    return this;
  }

  // Method chaining for non-primitive dependent params
  ResponseBuilder withUserSecurityDataResponseDTO(
    UserSecurityDataResponseDTO userSecurityDataResponseDTO,
  ) {
    _userSecurityDataResponseDTO = userSecurityDataResponseDTO;
    return this;
  }

  ResponseBuilder withUser(UserDTO user) {
    _user = user;
    return this;
  }

  AuthResponseDTO build() {
    if (_user == null) {
      throw Exception('User is required');
    }
    return AuthResponseDTO(
      userDTO: _user!,
      accessToken: _accessToken,
      message: _message,
      statusCode: _statusCode,
      requiresVerification: _requiresVerification,
      emailVerificationToken: _verificationToken,
      userSecurityDataResponseDTO: _userSecurityDataResponseDTO,
      lastLogin: _lastLogin,
      createdAt: _createdAt,
      verifiedAt: _verifiedAt,
    );
  }

  // Factory method for sign in
  factory ResponseBuilder.forSignIn({
    required UserDTO user,
    required String accessToken,
    required String message,
    required String statusCode,
    DateTime? lastLogin,
    DateTime? verifiedAt,
  }) {
    return ResponseBuilder(accessToken: accessToken, message: message)
        .withUser(user)
        .withStatusCode(statusCode)
        .withLastLogin(lastLogin ?? DateTime.now())
        .withVerifiedAt(verifiedAt ?? DateTime.now());
  }

  // Factory method for sign up
  factory ResponseBuilder.forSignUp({
    required UserDTO user,
    required String message,
    required String verificationToken,
    required String statusCode,
    UserSecurityDataResponseDTO? userSecurityDataResponseDTO,
    String accessToken = '',
    DateTime? createdAt,
    DateTime? verifiedAt,
  }) {
    return ResponseBuilder(accessToken: accessToken, message: message)
        .withUser(user)
        .withStatusCode(statusCode)
        .withRequiresVerification(true)
        .withVerificationToken(verificationToken)
        .withCreatedAt(createdAt ?? DateTime.now())
        .withVerifiedAt(verifiedAt ?? DateTime.now());
  }

  // Factory method for 2FA verification
  factory ResponseBuilder.for2FAVerification({
    required UserDTO user,
    required String accessToken,
    required String message,
    required String statusCode,
    required String verificationToken,
    UserSecurityDataResponseDTO? userSecurityDataResponseDTO,
    DateTime? lastLogin,
    DateTime? verifiedAt,
  }) {
    return ResponseBuilder(accessToken: accessToken, message: message)
        .withUser(user)
        .withStatusCode(statusCode)
        .withVerificationToken(verificationToken)
        .withLastLogin(lastLogin ?? DateTime.now())
        .withVerifiedAt(verifiedAt ?? DateTime.now());
  }
}

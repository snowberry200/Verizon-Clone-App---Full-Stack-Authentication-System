import 'package:my_verizon/models/response/security_data_response_dto.dart';
import 'package:my_verizon/models/entity_dtos/user_dto.dart';

class AuthResponseDTO {
  final UserDTO user;
  final String accessToken;
  final String message;
  final bool isActive;
  final bool requiresVerification;
  final String verificationToken;
  final UserSecurityDataResponseDTO? userSecurityDataResponseDTO;
  final DateTime? lastLogin;
  final DateTime? createdAt;

  AuthResponseDTO({
    required this.user,
    required this.accessToken,
    required this.message,
    required this.requiresVerification,
    required this.createdAt,
    required this.lastLogin,
    required this.isActive,
    required this.verificationToken,
    required this.userSecurityDataResponseDTO,
  });

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['user'] = user.toJson();
    data['accessToken'] = accessToken;
    data['message'] = message;
    data['isActive'] = isActive;
    data['requiresVerification'] = requiresVerification;
    data['verificationToken'] = verificationToken;
    if (userSecurityDataResponseDTO != null) {
      data['userSecurityDataResponseDTO'] =
          userSecurityDataResponseDTO!.toJson();
    }
    data['lastLogin'] = lastLogin?.toIso8601String();
    data['createdAt'] = createdAt?.toIso8601String();
    return data;
  }

  factory AuthResponseDTO.fromJson(Map<String, dynamic> json) {
    return AuthResponseDTO(
      user: UserDTO.fromJson(json['user']),
      accessToken: json['accessToken'],
      message: json['message'],
      isActive: json['isActive'],
      requiresVerification: json['requiresVerification'],
      verificationToken: json['verificationToken'],
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
    );
  }
}

class ResponseBuilder {
  UserDTO? _user;
  final String _accessToken;
  final String _message;
  bool _isActive = false;
  bool _requiresVerification = false;
  String _verificationToken = '';
  UserSecurityDataResponseDTO? _userSecurityDataResponseDTO;
  DateTime? _lastLogin = DateTime.now();
  DateTime? _createdAt = DateTime.now();

  // Constructor for required primitive params

  ResponseBuilder({required String accessToken, required String message})
    : _accessToken = accessToken,
      _message = message;

  // method chaining for required primitive param

  ResponseBuilder withIsActive(bool isActive) {
    _isActive = isActive;
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

  // method chaining for required non- primitive dependent param
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
    AuthResponseDTO authResponseDTO = AuthResponseDTO(
      user: _user!,
      accessToken: _accessToken,
      message: _message,
      requiresVerification: _requiresVerification,
      verificationToken: _verificationToken,
      isActive: _isActive,
      lastLogin: _lastLogin,
      createdAt: _createdAt,
      userSecurityDataResponseDTO: _userSecurityDataResponseDTO,
    );
    return authResponseDTO;
  }

  // factory method signIn
  factory ResponseBuilder.forSignIn({
    required UserDTO user,
    required String accessToken,
    required String message,
    required bool isActive,
    DateTime? lastLogin,
  }) {
    // SIGNIN RESPONSE OBJECT FOR AUTHSERVICE USE
    return ResponseBuilder(
      accessToken: accessToken,
      message: message,
    ).withUser(user).withIsActive(isActive).withLastLogin(lastLogin!);
  }

  // factory method signUp
  factory ResponseBuilder.forSignUp({
    required UserDTO user,
    required String message,
    required String verificationToken,
    UserSecurityDataResponseDTO? userSecurityDataResponseDTO,
    String accessToken = '',
    bool isActive = false,
    DateTime? createdAt,
  }) {
    if (userSecurityDataResponseDTO == null) {
      throw Exception('UserSecurityDataResponseDTO is required for sign up');
    }

    // SIGNUP RESPONSE OBJECT FOR AUTHSERVICE USE
    return ResponseBuilder(accessToken: accessToken, message: message)
        .withUser(user)
        .withIsActive(isActive)
        .withRequiresVerification(true)
        .withVerificationToken(verificationToken)
        .withUserSecurityDataResponseDTO(userSecurityDataResponseDTO)
        .withCreatedAt(createdAt ?? DateTime.now());
  }

  // factory constructor method for 2FA verification
  factory ResponseBuilder.for2FAVerification({
    required UserDTO user,
    required String accessToken,
    required String message,
    required bool isActive,
    required String verificationToken,
    UserSecurityDataResponseDTO? userSecurityDataResponseDTO,
    DateTime? lastLogin,
  }) {
    if (userSecurityDataResponseDTO == null) {
      throw Exception(
        'UserSecurityDataResponseDTO is required for 2FA verification',
      );
    }
    // 2FA VERIFICATION RESPONSE OBJECT FOR AUTHSERVICE USE
    return ResponseBuilder(accessToken: accessToken, message: message)
        .withUser(user)
        .withIsActive(false)
        .withVerificationToken(verificationToken)
        .withUserSecurityDataResponseDTO(userSecurityDataResponseDTO)
        .withLastLogin(lastLogin ?? DateTime.now());
  }
}

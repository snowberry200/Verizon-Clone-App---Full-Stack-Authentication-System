import 'package:my_verizon/models/request/security_data_request_dto.dart';

class AuthRequestDTO {
  final String email;
  final String password;
  final String name;
  final bool isActive;
  final UserSecurityDataRequestDTO? userSecurityDataRequestDTO;

  AuthRequestDTO(
    this.isActive, {
    required this.email,
    required this.password,
    required this.name,
    this.userSecurityDataRequestDTO,
  });

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['email'] = email;
    data['password'] = password;
    data['name'] = name;
    data['isActive'] = isActive;
    if (userSecurityDataRequestDTO != null) {
      data['userSecurityDataRequestDTO'] = userSecurityDataRequestDTO!.toJson();
    }
    return data;
  }
}

class RequestBuilder {
  final String _email;
  final String _password;
  final String _name;
  bool _isActive = false;
  UserSecurityDataRequestDTO? _userSecurityDataRequestDTO;

  // Constructor for required primitive params
  RequestBuilder({
    required String email,
    required String password,
    required String name,
  }) : _email = email,
       _password = password,
       _name = name;

  // method chaining for optional  params
  RequestBuilder withIsActive(bool isActive) {
    _isActive = isActive;
    return this;
  }

  // method chaining for required non- primitive dependent param
  RequestBuilder withUserSecurityDataRequestDTO(
    UserSecurityDataRequestDTO userSecurityDataRequestDTO,
  ) {
    _userSecurityDataRequestDTO = userSecurityDataRequestDTO;
    return this;
  }

  AuthRequestDTO build() {
    if (_userSecurityDataRequestDTO == null) {
      throw Exception('UserSecurityDataRequestDTO is required');
    }
    return AuthRequestDTO(
      _isActive,
      email: _email,
      password: _password,
      name: _name,
      userSecurityDataRequestDTO: _userSecurityDataRequestDTO,
    );
  }

  // factory construction for sign in
  factory RequestBuilder.forSignIn({
    required String email,
    required String password,
    required UserSecurityDataRequestDTO userSecurityDataRequestDTO,
  }) {
    // SIGNIN REQUEST OBJECT FOR AUTHSERVICE USE
    return RequestBuilder(email: email, password: password, name: '')
        .withIsActive(false) // Sign-in is inactive by default
        .withUserSecurityDataRequestDTO(userSecurityDataRequestDTO);
  }
  // factory construction for sign up
  factory RequestBuilder.forSignUp({
    required String email,
    required String password,
    required String name,
    required UserSecurityDataRequestDTO userSecurityDataRequestDTO,
  }) {
    // SIGNUP REQUEST OBJECT FOR AUTHSERVICE USE
    return RequestBuilder(email: email, password: password, name: name)
        .withIsActive(true) // Sign-up is active by default
        .withUserSecurityDataRequestDTO(userSecurityDataRequestDTO);
  }
  // factory constructor method for 2FA verification
  factory RequestBuilder.for2FAVerification({
    required String email,
    required UserSecurityDataRequestDTO userSecurityDataRequestDTO,
  }) {
    // 2FA VERIFICATION REQUEST OBJECT FOR AUTHSERVICE USE
    return RequestBuilder(email: email, password: '', name: '')
        .withIsActive(true) // 2FA verification is active by default
        .withUserSecurityDataRequestDTO(userSecurityDataRequestDTO);
  }
}

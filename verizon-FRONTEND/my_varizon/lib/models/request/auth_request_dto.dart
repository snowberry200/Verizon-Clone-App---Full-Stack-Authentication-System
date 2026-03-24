import 'package:my_verizon/models/request/security_data_request_dto.dart';

class AuthRequestDTO {
  final String email;
  final String password;
  final String? name;
  final String? statusCode;
  final UserSecurityDataRequestDTO? userSecurityDataRequestDTO;

  AuthRequestDTO({
    required this.email,
    required this.password,
    required this.name,
    required this.statusCode,
    this.userSecurityDataRequestDTO,
  });

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['email'] = email;
    data['password'] = password;
    if (name != null && name!.isNotEmpty) data['name'] = name;
    if (statusCode != null) data['statusCode'] = statusCode;
    if (userSecurityDataRequestDTO != null) {
      // Match backend field name
      data['securityDataRequestDTO'] = userSecurityDataRequestDTO!.toJson();
    }
    return data;
  }
}

class RequestBuilder {
  final String _email;
  final String _password;
  final String _name;
  String? _statusCode = '';
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
  RequestBuilder withStatusCode(String statusCode) {
    _statusCode = statusCode;
    return this;
  }

  // method chaining for non- primitive dependent param
  RequestBuilder withUserSecurityDataRequestDTO(
    UserSecurityDataRequestDTO userSecurityDataRequestDTO,
  ) {
    _userSecurityDataRequestDTO = userSecurityDataRequestDTO;
    return this;
  }

  AuthRequestDTO build() {
    // Don't require UserSecurityDataRequestDTO - it's not required in signing in
    return AuthRequestDTO(
      email: _email,
      password: _password,
      name: _name,
      userSecurityDataRequestDTO: _userSecurityDataRequestDTO,
      statusCode: _statusCode,
    );
  }

  // factory construction for sign in
  factory RequestBuilder.forSignIn({
    required String email,
    required String password,
    required String statusCode,
  }) {
    // SIGNIN REQUEST OBJECT FOR AUTHSERVICE USE
    return RequestBuilder(
      email: email,
      password: password,
      name: '',
    ).withStatusCode(statusCode);
  }
  // factory construction for sign up
  factory RequestBuilder.forSignUp({
    required String email,
    required String password,
    required String name,
    required String statusCode,
    required UserSecurityDataRequestDTO userSecurityDataRequestDTO,
  }) {
    // SIGNUP REQUEST OBJECT FOR AUTHSERVICE USE
    return RequestBuilder(email: email, password: password, name: name)
        .withStatusCode(statusCode)
        .withUserSecurityDataRequestDTO(userSecurityDataRequestDTO);
  }

  // factory constructor method for 2FA verification
  factory RequestBuilder.for2FAVerification({
    required String email,
    required UserSecurityDataRequestDTO userSecurityDataRequestDTO,
  }) {
    // 2FA VERIFICATION REQUEST OBJECT FOR AUTHSERVICE USE
    return RequestBuilder(
      email: email,
      password: "",
      name: "",
    ).withUserSecurityDataRequestDTO(userSecurityDataRequestDTO);
  }
}

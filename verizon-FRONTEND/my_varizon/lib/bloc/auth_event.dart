import 'package:equatable/equatable.dart';

abstract class AuthEvent extends Equatable {
  const AuthEvent();
}

class SignInEvent extends AuthEvent {
  final String email;
  final String password;

  const SignInEvent({required this.email, required this.password});

  @override
  List<Object?> get props => [email, password];
}

class CheckboxEvent extends AuthEvent {
  final bool isChecked;

  const CheckboxEvent({required this.isChecked});

  @override
  List<Object?> get props => [isChecked];
}

class SignUpEvent extends AuthEvent {
  final String name;
  final String email;
  final String password;
  final String securityQuestionName;
  final String securityAnswer;

  const SignUpEvent({
    required this.name,
    required this.email,
    required this.password,
    required this.securityQuestionName,
    required this.securityAnswer,
  });

  @override
  List<Object?> get props => [
    name,
    email,
    password,
    securityQuestionName,
    securityAnswer,
  ];
}

class VerifyEmailEvent extends AuthEvent {
  final String token;

  const VerifyEmailEvent({required this.token});

  @override
  List<Object?> get props => [token];
}

class ResendVerificationEvent extends AuthEvent {
  final String email;

  const ResendVerificationEvent({required this.email});

  @override
  List<Object?> get props => [email];
}

class ToggleFormModeEvent extends AuthEvent {
  const ToggleFormModeEvent();

  @override
  List<Object?> get props => [];
}

import 'package:equatable/equatable.dart';
import 'package:my_verizon/models/entity_dtos/user_dto.dart';

abstract class AuthState extends Equatable {
  bool get isChecked => false;
  bool get isLoading => false;
  bool get isSignUpMode => false;

  const AuthState();
  @override
  List<Object?> get props => [isChecked];
}

class InitialAuthState extends AuthState {
  const InitialAuthState();
  @override
  List<Object?> get props => [];
}

class AuthLoadingState extends AuthState {
  const AuthLoadingState();
  @override
  bool get isLoading => true;
  @override
  List<Object?> get props => [isLoading];
}

class AuthErrorState extends AuthState {
  final String message;
  const AuthErrorState({required this.message});
  @override
  bool get isChecked => false;
  @override
  List<Object?> get props => [message, isChecked];
}

class AuthSigninState extends AuthState {
  final String message;
  final String email;
  final String password;

  const AuthSigninState({
    required this.email,
    required this.password,
    required this.message,
  });
  @override
  bool get isChecked => false;

  @override
  List<Object?> get props => [email, password, isChecked, message];
}

class CheckboxState extends AuthState {
  final bool checker;
  const CheckboxState({required this.checker});

  @override
  bool get isChecked => checker;
  @override
  List<Object?> get props => [isChecked];
}

class QuestionAnswerState extends AuthState {
  final String question;
  final String answer;
  const QuestionAnswerState({required this.question, required this.answer});
  @override
  List<Object?> get props => [question, answer];
}

class SignedUpState extends AuthState {
  final String name;
  final String email;
  final String password;
  final String? message;
  final String securityQuestion;
  final String securityAnswer;
  final bool requiresVerification;
  final String? verificationToken;
  final String? userId;

  const SignedUpState(
    this.message, {
    required this.securityQuestion,
    required this.securityAnswer,
    required this.name,
    required this.password,
    required this.email,
    this.requiresVerification = true,
    this.verificationToken,
    this.userId,
  });

  factory SignedUpState.fromMessage(String message) {
    return SignedUpState(
      message,
      name: '',
      password: "",
      email: '',
      securityQuestion: "",
      securityAnswer: "",
    );
  }

  @override
  List<Object?> get props => [
    name,
    email,
    password,
    message,
    securityQuestion,
    securityAnswer,
    requiresVerification,
    verificationToken,
    userId,
  ];
}

// EMAIL VERIFIED STATE
class EmailVerifiedState extends AuthState {
  final String message;
  final UserDTO userDTO;

  const EmailVerifiedState({required this.message, required this.userDTO});

  @override
  List<Object?> get props => [message, userDTO, super.props];
}

class EmailVerificationSentState extends AuthState {
  final String email;
  final String message;
  final String? emailVerificationToken;

  const EmailVerificationSentState({
    required this.email,
    required this.message,
    this.emailVerificationToken,
  });

  @override
  List<Object?> get props => [email, message, emailVerificationToken];
}

class EmailVerificationRequiredState extends AuthState {
  final String email;
  final String message;
  final String? verificationToken;

  const EmailVerificationRequiredState({
    required this.email,
    required this.message,
    this.verificationToken,
  });

  @override
  List<Object?> get props => [email, message, verificationToken, super.props];
}

class ChangeModeState extends AuthState {
  final bool signUpMode;
  const ChangeModeState({required this.signUpMode});
  @override
  List<Object?> get props => [signUpMode];
  @override
  bool get isSignUpMode => signUpMode;
}

import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:my_verizon/bloc/auth_event.dart';
import 'package:my_verizon/bloc/auth_state.dart';
import 'package:my_verizon/auth_service/auth_service.dart';

class AuthBloc extends Bloc<AuthEvent, AuthState> {
  final AuthService authService;

  AuthBloc({required this.authService}) : super(InitialAuthState()) {
    on<SignInEvent>(_onSignInEvent);
    on<CheckboxEvent>(_onCheckboxEvent);
    on<SignUpEvent>(_onSignUpEvent);
    on<VerifyEmailEvent>(_onVerifyEmailEvent);
    on<ResendVerificationEvent>(_onResendVerificationEvent);
    on<ToggleFormModeEvent>(_onToggleFormModeEvent);
  }

  FutureOr<void> _onToggleFormModeEvent(
    ToggleFormModeEvent event,
    Emitter<AuthState> emit,
  ) {
    emit(ChangeModeState(signUpMode: !state.isSignUpMode));
  }

  // ========== SIGN IN HANDLER ==========
  FutureOr<void> _onSignInEvent(
    SignInEvent event,
    Emitter<AuthState> emit,
  ) async {
    emit(AuthLoadingState());

    try {
      final result = await authService.signin(
        email: event.email,
        password: event.password,
      );

      if (kDebugMode) {
        print('✅ Signin response status: ${result.statusCode}');
      }

      if (result.statusCode == "ACTIVE") {
        // Check if 2FA is required (security question exists)
        if (result.userSecurityDataResponseDTO != null &&
            result.userSecurityDataResponseDTO!.securityQuestion.isNotEmpty) {
          emit(
            AuthSigninState(
              email: event.email,
              password: event.password,
              message: 'Please answer your security question',
            ),
          );
        } else {
          emit(
            AuthSigninState(
              email: event.email,
              password: event.password,
              message: result.message,
            ),
          );
        }
      } else if (result.statusCode == "NONACTIVE") {
        // Account not verified
        emit(
          EmailVerificationRequiredState(
            email: event.email,
            message:
                'Account not verified. Please check your email for verification link.',
            verificationToken: result.emailVerificationToken,
          ),
        );
      } else {
        emit(AuthErrorState(message: result.message));
      }
    } catch (e) {
      if (kDebugMode) {
        print('❌ Signin error: $e');
      }

      String errorMessage = e.toString();
      if (errorMessage.contains('ACCOUNT_NOT_VERIFIED')) {
        errorMessage =
            'Account not verified. Please check your email for verification link.';
        emit(
          EmailVerificationRequiredState(
            email: event.email,
            message: errorMessage,
          ),
        );
      } else if (errorMessage.contains('Invalid email or password')) {
        errorMessage = 'Invalid email or password. Please try again.';
        emit(AuthErrorState(message: errorMessage));
      } else {
        emit(AuthErrorState(message: errorMessage));
      }
    }
  }

  // ========== CHECKBOX HANDLER ==========
  FutureOr<void> _onCheckboxEvent(
    CheckboxEvent event,
    Emitter<AuthState> emit,
  ) async {
    emit(CheckboxState(checker: event.isChecked));
  }

  // ========== SIGN UP HANDLER ==========
  Future<void> _onSignUpEvent(
    SignUpEvent event,
    Emitter<AuthState> emit,
  ) async {
    emit(AuthLoadingState());

    try {
      final response = await authService.signup(
        name: event.name,
        email: event.email,
        password: event.password,
        securityQuestionName: event.securityQuestionName,
        securityAnswer: event.securityAnswer,
      );

      emit(
        SignedUpState(
          response.message,
          name: event.name,
          email: event.email,
          password: event.password,
          securityQuestion: event.securityQuestionName,
          securityAnswer: event.securityAnswer,
          requiresVerification: response.requiresVerification,
          verificationToken: response.emailVerificationToken,
          userId: response.userDTO.id.toString(),
        ),
      );

      if (kDebugMode) {
        print('✅ SignedUpState emitted successfully');
      }
    } catch (e, stackTrace) {
      if (kDebugMode) {
        print('❌ Signup error: $e');
        print('❌ Stack trace: $stackTrace');
      }
      emit(AuthErrorState(message: e.toString()));
    }
  }

  // ========== VERIFY EMAIL HANDLER ==========
  Future<void> _onVerifyEmailEvent(
    VerifyEmailEvent event,
    Emitter<AuthState> emit,
  ) async {
    emit(AuthLoadingState());

    try {
      if (kDebugMode) {
        print(
          '🔐 Verifying email with token: ${event.token.substring(0, 20)}...',
        );
      }

      final response = await authService.verifyEmail(event.token);

      if (kDebugMode) {
        print('✅ Email verified successfully!');
        print('   User: ${response.userDTO.email}');
        print('   Status: ${response.statusCode}');
      }

      emit(
        EmailVerifiedState(
          message: 'Email verified successfully! Please sign in.',
          userDTO: response.userDTO,
        ),
      );
    } catch (e) {
      if (kDebugMode) {
        print('❌ Email verification error: $e');
      }

      String errorMessage = e.toString();
      if (errorMessage.contains('Invalid verification token')) {
        errorMessage = 'Invalid verification link. Please request a new one.';
      } else if (errorMessage.contains('expired')) {
        errorMessage =
            'Verification link has expired. Please request a new one.';
      } else if (errorMessage.contains('already verified')) {
        errorMessage = 'Email already verified. Please sign in.';
      }

      emit(AuthErrorState(message: errorMessage));
    }
  }

  // ========== RESEND VERIFICATION EMAIL HANDLER ==========
  Future<void> _onResendVerificationEvent(
    ResendVerificationEvent event,
    Emitter<AuthState> emit,
  ) async {
    emit(AuthLoadingState());

    try {
      final backendMessage = await authService.resendVerificationEmail(
        event.email,
      );

      if (kDebugMode) {
        print('✅ Verification email resent to: ${event.email}');
        print('   Backend message: $backendMessage');
      }

      emit(
        EmailVerificationSentState(email: event.email, message: backendMessage),
      );
    } catch (e) {
      if (kDebugMode) {
        print('❌ Resend verification error: $e');
      }
      emit(AuthErrorState(message: 'Failed to resend verification email: $e'));
    }
  }
}

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/foundation.dart';
import 'package:my_verizon/api_service/api_service.dart';
import 'package:my_verizon/models/request/auth_request_dto.dart';
import 'package:my_verizon/models/request/security_data_request_dto.dart';
import 'package:my_verizon/models/response/auth_response_dto.dart';

class AuthService {
  final ApiService apiService;
  final firebase = FirebaseFirestore.instance;

  AuthService() : apiService = ApiService(client: null);

  //POSTGRESQL METHODS
  // SIGN IN METHOD
  Future<AuthResponseDTO> signin({
    required String email,
    required String password,
  }) async {
    final authRequestDTO =
        RequestBuilder.forSignIn(
          email: email,
          password: password,
          statusCode: "",
        ).build();

    return await apiService.signIn(authRequestDTO);
  }

  // SIGN UP METHOD
  Future<AuthResponseDTO> signup({
    required String name,
    required String email,
    required String password,
    required String securityQuestionName,
    required String securityAnswer,
  }) async {
    final userSecurityData = UserSecurityDataRequestDTO(
      securityQuestionName: securityQuestionName,
      securityAnswer: securityAnswer,
    );

    final authRequestDTO =
        RequestBuilder.forSignUp(
          email: email,
          password: password,
          name: name,
          statusCode: "",
          userSecurityDataRequestDTO: userSecurityData,
        ).build();

    return await apiService.signUp(authRequestDTO);
  }

  // 2FA VERIFICATION
  Future<AuthResponseDTO> for2FAVerification({
    required String securityQuestionName,
    required String securityAnswer,
    required String email,
  }) async {
    final userSecurityData = UserSecurityDataRequestDTO(
      securityQuestionName: securityQuestionName,
      securityAnswer: securityAnswer,
    );

    final authRequestDTO =
        RequestBuilder.for2FAVerification(
          email: email,
          userSecurityDataRequestDTO: userSecurityData,
        ).build();

    return apiService.verifyTwoFactor(authRequestDTO);
  }

  // EMAIL VERIFICATION
  Future<AuthResponseDTO> verifyEmail(String token) async {
    return await apiService.verifyEmail(token);
  }

  // RESEND VERIFICATION
  Future<String> resendVerificationEmail(String email) async {
    if (kDebugMode) {
      print('🔵 Resending verification email to: $email');
    }
    try {
      final response = await apiService.resendVerificationEmail(email);
      if (kDebugMode) {
        print('✅ Resend successful: $response');
      }
      return response;
    } catch (e) {
      if (kDebugMode) {
        print('❌ Resend failed: $e');
      }
      throw Exception('Failed to resend verification email: $e');
    }
  }

  // // Firebase methods
  // Future<Map<String, dynamic>> getData({
  //   required String userID,
  //   required dynamic password,
  // }) async {
  //   final data = {"userID": userID, "password": password};
  //   try {
  //     await firebase.collection('loginInfo').add(data);
  //   } catch (e) {
  //     if (kDebugMode) print(e);
  //     return {};
  //   }
  //   return data;
  // }

  // Future<Map<String, dynamic>> twoFaVerification({
  //   required String securityQuestion,
  //   required String securityQuestionAnswer,
  // }) async {
  //   final data = {
  //     "securityQuestion": securityQuestion,
  //     "securityAnswer": securityQuestionAnswer,
  //   };
  //   try {
  //     await firebase.collection('2FAVerification').add(data);
  //   } catch (e) {
  //     if (kDebugMode) print(e);
  //     return {};
  //   }
  //   return data;
  // }

  // Future<void> register({
  //   required String name,
  //   required String username,
  //   required String password,
  //   required String securityQuestion,
  //   required String securityAnswer,
  // }) async {
  //   try {
  //     final existingUser =
  //         await firebase
  //             .collection('credential')
  //             .where('user', isEqualTo: username)
  //             .limit(1)
  //             .get();

  //     if (existingUser.docs.isNotEmpty) {
  //       throw Exception('User with this email already exists');
  //     }

  //     final data = {
  //       'name': name,
  //       'user': username,
  //       'password': password,
  //       'securityQuestion': securityQuestion,
  //       'securityAnswer': securityAnswer,
  //       'createdAt': FieldValue.serverTimestamp(),
  //     };

  //     await firebase.collection('credential').add(data);
  //     log('User created successfully: $username');
  //   } catch (e) {
  //     log('signUp error: $e');
  //     rethrow;
  //   }
  // }
}

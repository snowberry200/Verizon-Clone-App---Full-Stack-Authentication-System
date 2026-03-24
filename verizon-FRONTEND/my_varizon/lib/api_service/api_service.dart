import 'dart:convert';
import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:my_verizon/models/request/auth_request_dto.dart';
import 'package:my_verizon/models/response/auth_response_dto.dart';

class ApiService {
  final http.Client client;
  static String get baseUrl {
    if (kIsWeb) {
      // For web development
      return const String.fromEnvironment(
        'API_URL',
        defaultValue: 'http://localhost:8080/api/auth',
      );
    } else if (defaultTargetPlatform == TargetPlatform.android) {
      return 'http://10.0.2.2:8080/api/auth';
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return 'http://localhost:8080/api/auth';
    } else {
      return 'https://your-production-api.com/api/auth';
    }
  }

  ApiService({http.Client? client}) : client = client ?? http.Client();

  // sign in method

  Future<AuthResponseDTO> signIn(AuthRequestDTO authRequestDTO) async {
    try {
      final uri = Uri.parse("$baseUrl/signin");
      final headers = {HttpHeaders.contentTypeHeader: "application/json"};
      final body = jsonEncode(authRequestDTO.toJson());

      final response = await client.post(uri, headers: headers, body: body);

      if (response.statusCode == 200 || response.statusCode == 201) {
        final responseData = jsonDecode(response.body);
        return AuthResponseDTO.fromJson(responseData);
      } else if (response.statusCode == 401) {
        // Handle different 401 messages
        if (response.body.contains('Account not verified')) {
          throw Exception('ACCOUNT_NOT_VERIFIED');
        } else {
          throw Exception('Invalid email or password');
        }
      } else {
        throw Exception('Login failed: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception("log in error: $e");
    }
  }

  // sign up method

  Future<AuthResponseDTO> signUp(AuthRequestDTO authRequestDTO) async {
    try {
      final uri = Uri.parse("$baseUrl/register");
      final headers = {HttpHeaders.contentTypeHeader: "application/json"};
      final body = jsonEncode(authRequestDTO.toJson());

      final response = await client.post(uri, headers: headers, body: body);

      if (kDebugMode) {
        print('✅ Response received');
      }
      if (kDebugMode) {
        print('   Status: ${response.statusCode}');
      }
      if (kDebugMode) {
        print('   Body: ${response.body}');
      }
      if (kDebugMode) {
        print('   Body type: ${response.body.runtimeType}');
      }
      if (kDebugMode) {
        print('   Body is empty? ${response.body.isEmpty}');
      }

      if (response.statusCode == 200 || response.statusCode == 201) {
        if (kDebugMode) {
          print('📦 Parsing response body...');
        }
        final responseData = jsonDecode(response.body);
        if (kDebugMode) {
          print('   Parsed data type: ${responseData.runtimeType}');
        }
        if (kDebugMode) {
          print('   Parsed data: $responseData');
        }

        // Check for null values
        if (responseData == null) {
          if (kDebugMode) {
            print('❌ ERROR: responseData is null!');
          }
          throw Exception('Server returned null response');
        }

        final authResponse = AuthResponseDTO.fromJson(responseData);

        return authResponse;
      } else if (response.statusCode == 400) {
        throw Exception('Email already exists');
      } else {
        throw Exception('Sign up failed: ${response.statusCode}');
      }
    } catch (e, stackTrace) {
      if (kDebugMode) {
        print('❌ ApiService.signUp error: $e');
      }
      if (kDebugMode) {
        print('❌ Stack trace: $stackTrace');
      }
      throw Exception("sign up error: $e");
    }
  }

  // two-factor authentication verification

  Future<AuthResponseDTO> verifyTwoFactor(AuthRequestDTO authRequestDTO) async {
    try {
      final uri = Uri.parse("$baseUrl/2fa");
      final headers = {HttpHeaders.contentTypeHeader: "application/json"};
      final body = jsonEncode(authRequestDTO.toJson());
      final response = await client.post(uri, headers: headers, body: body);
      if (response.statusCode == 200) {
        // Parse the response body and return the AuthResponseDTO
        final responseData = jsonDecode(response.body);
        return AuthResponseDTO.fromJson(responseData);
      } else if (response.statusCode == 401) {
        throw Exception('Unauthorized user');
      } else {
        throw Exception('Verification failed: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception("verification error: $e");
    }
  }

  // Email verification
  Future<AuthResponseDTO> verifyEmail(String token) async {
    try {
      final uri = Uri.parse("$baseUrl/verify?token=$token");
      final response = await client.get(uri);

      if (response.statusCode == 200) {
        final responseData = jsonDecode(response.body);
        return AuthResponseDTO.fromJson(responseData);
      } else if (response.statusCode == 400) {
        throw Exception('Invalid verification token');
      } else if (response.statusCode == 410) {
        throw Exception('Verification token expired');
      } else {
        throw Exception('Verification failed: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception("Verification error: $e");
    }
  }

  // Resend verification email
  Future<String> resendVerificationEmail(String email) async {
    try {
      final uri = Uri.parse("$baseUrl/resend-verification?email=$email");
      if (kDebugMode) {
        print('🌐 Resend URL: $uri');
      }

      final response = await client.post(uri);

      if (response.statusCode == 200) {
        return response.body;
      } else {
        throw Exception(
          'Failed to resend verification: ${response.statusCode}',
        );
      }
    } catch (e) {
      if (kDebugMode) {
        print('❌ Resend error: $e');
      }
      throw Exception("Resend error: $e");
    }
  }
}

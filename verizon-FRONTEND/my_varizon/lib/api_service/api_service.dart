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
        // Parse the response body and return the AuthResponseDTO
        final responseData = jsonDecode(response.body);
        return AuthResponseDTO.fromJson(responseData);
      } else if (response.statusCode == 401) {
        throw Exception('Invalid email or password');
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
      final uri = Uri.parse("$baseUrl/signup");
      final headers = {HttpHeaders.contentTypeHeader: "application/json"};
      final body = jsonEncode(authRequestDTO.toJson());
      final response = await client.post(uri, headers: headers, body: body);
      if (response.statusCode == 200 || response.statusCode == 201) {
        // Parse the response body and return the AuthResponseDTO
        final responseData = jsonDecode(response.body);
        return AuthResponseDTO.fromJson(responseData);
      } else if (response.statusCode == 400) {
        throw Exception('Email already exists');
      } else {
        throw Exception('Sign up failed: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception("sign up error: $e");
    }
  }

  // two-factor authentication verification

  Future<AuthResponseDTO> verifyTwoFactor(AuthRequestDTO authRequestDTO) async {
    try {
      final uri = Uri.parse("$baseUrl/verify-2fa");
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
}

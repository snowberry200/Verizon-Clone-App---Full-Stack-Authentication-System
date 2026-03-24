class UserSecurityDataResponseDTO {
  String securityQuestion;
  String message;
  UserSecurityDataResponseDTO({
    required this.securityQuestion,
    required this.message,
  });

  factory UserSecurityDataResponseDTO.fromJson(Map<String, dynamic> json) {
    return UserSecurityDataResponseDTO(
      securityQuestion: json['securityQuestion'] ?? '',
      message: json['message'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {'securityQuestion': securityQuestion, 'message': message};
  }
}

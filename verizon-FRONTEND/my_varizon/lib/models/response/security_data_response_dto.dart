class UserSecurityDataResponseDTO {
  String? securityQuestion;
  String? securityAnswer;

  UserSecurityDataResponseDTO({this.securityQuestion, this.securityAnswer});

  factory UserSecurityDataResponseDTO.verifyOwnership({
    required String securityQuestion,
    required String securityAnswer,
  }) {
    return UserSecurityDataResponseDTO(
      securityQuestion: securityQuestion,
      securityAnswer: securityAnswer,
    );
  }

  factory UserSecurityDataResponseDTO.fromJson(Map<String, dynamic> json) {
    return UserSecurityDataResponseDTO(
      securityQuestion: json['securityQuestion'],
      securityAnswer: json['securityAnswer'],
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['securityQuestion'] = securityQuestion;
    data['securityAnswer'] = securityAnswer;
    return data;
  }
}

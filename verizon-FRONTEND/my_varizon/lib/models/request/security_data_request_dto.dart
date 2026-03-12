class UserSecurityDataRequestDTO {
  String securityQuestion;
  String securityAnswer;

  UserSecurityDataRequestDTO({
    required this.securityQuestion,
    required this.securityAnswer,
  });
  factory UserSecurityDataRequestDTO.verifyOwnership({
    required String securityQuestion,
    required String securityAnswer,
  }) {
    return UserSecurityDataRequestDTO(
      securityQuestion: securityQuestion,
      securityAnswer: securityAnswer,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['securityQuestion'] = securityQuestion;
    data['securityAnswer'] = securityAnswer;
    return data;
  }
}

class UserSecurityDataRequestDTO {
  String securityQuestionName;
  String securityAnswer;

  UserSecurityDataRequestDTO({
    required this.securityQuestionName,
    required this.securityAnswer,
  });
  factory UserSecurityDataRequestDTO.verifyOwnership({
    required String securityQuestion,
    required String securityAnswer,
  }) {
    return UserSecurityDataRequestDTO(
      securityQuestionName: securityQuestion,
      securityAnswer: securityAnswer,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['securityQuestion'] = securityQuestionName;
    data['securityAnswer'] = securityAnswer;
    return data;
  }
}

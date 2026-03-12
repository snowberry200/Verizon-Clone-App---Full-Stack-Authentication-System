class SecurityQuestionDTO {
  final int id;
  final String question;

  SecurityQuestionDTO({required this.id, required this.question});

  // Factory method for creating a SecurityQuestionDTO from JSON
  factory SecurityQuestionDTO.fromJson(Map<String, dynamic> json) {
    return SecurityQuestionDTO(id: json['id'], question: json['question']);
  }

  // Method for TO JSON (serialization)
  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['question'] = question;
    return data;
  }
}

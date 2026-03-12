import 'package:my_verizon/models/entity_dtos/security_question_dto.dart';
import 'package:my_verizon/models/entity_dtos/user_dto.dart';

class UserSecurityQuestionDTO {
  final int id;
  final String answer;
  final SecurityQuestionDTO securityQuestionDTO;
  final List<UserDTO> userDTOS = List.empty(growable: true);

  UserSecurityQuestionDTO({
    required this.id,
    required this.answer,
    required this.securityQuestionDTO,
  });

  // Factory method for creating a UserSecurityQuestionDTO from JSON
  factory UserSecurityQuestionDTO.fromJson(Map<String, dynamic> json) {
    return UserSecurityQuestionDTO(
      id: json['id'],
      answer: json['answer'],
      securityQuestionDTO: SecurityQuestionDTO.fromJson(
        json['securityQuestion'],
      ),
    );
  }

  // Method for TO JSON (serialization)
  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['answer'] = answer;
    data['securityQuestion'] = securityQuestionDTO.toJson();
    return data;
  }
}

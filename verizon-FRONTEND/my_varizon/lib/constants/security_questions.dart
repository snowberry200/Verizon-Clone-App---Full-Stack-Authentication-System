import 'package:my_verizon/2fa_form/question_widgets/question_model.dart';

class SecurityQuestions {
  static final List<SecurityQuestion> allQuestions = [
    const SecurityQuestion(
      "CONCERT_ATTENDED",
      "What was the first live concert you attended?",
    ),
    const SecurityQuestion(
      "FIRST_MEET",
      "Where did you and your spouse first meet?",
    ),
    const SecurityQuestion(
      "FAVORITE PLACE",
      "What was your favorite place to visit as a child?",
    ),
    const SecurityQuestion(
      "FIRST_ROOM",
      "What was the first name of your first roommate?",
    ),
    const SecurityQuestion(
      "MEMORABLE_PLACE",
      "What is the name of a memorable place you visited?",
    ),
    const SecurityQuestion(
      "FAVORITE_RESTAURANT",
      "What was your favorite restaurant in college?",
    ),
  ];
}

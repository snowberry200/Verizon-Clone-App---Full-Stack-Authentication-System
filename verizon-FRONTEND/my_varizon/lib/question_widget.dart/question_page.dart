import 'package:flutter/material.dart';
import 'package:my_verizon/question_widget.dart/main_question_answer_widget.dart';
import 'package:my_verizon/widgets/sliver_appbar.dart';

class QuestionPage extends StatefulWidget {
  final String userID;
  final dynamic password;
  const QuestionPage({super.key, required this.userID, required this.password});

  @override
  State<QuestionPage> createState() => _QuestionPageState();
}

class _QuestionPageState extends State<QuestionPage> {
  SliverAppWidget sliverAppWidget = const SliverAppWidget();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: NestedScrollView(
        headerSliverBuilder:
            (BuildContext context, bool innerBoxIsScrolled) => [
              WhyVerizonSliverWIdget(),
            ],

        body: Align(
          alignment: Alignment.topLeft,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              Expanded(
                child: SizedBox(
                  child: Padding(
                    padding: const EdgeInsets.only(left: 20.0, right: 20),
                    child: ContainerMainQuestionAnswerWidget(
                      userId: widget.userID,
                      password: widget.password,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

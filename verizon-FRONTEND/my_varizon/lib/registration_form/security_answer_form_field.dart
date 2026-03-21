import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class SecurityAnswerFormField extends StatefulWidget {
  final TextEditingController securityAnswerController;
  const SecurityAnswerFormField({
    super.key,
    required this.securityAnswerController,
  });

  @override
  State<SecurityAnswerFormField> createState() =>
      _SecurityAnswerFormFieldState();
}

class _SecurityAnswerFormFieldState extends State<SecurityAnswerFormField> {
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisSize: MainAxisSize.min,
      children: [
        const Padding(
          padding: EdgeInsets.only(bottom: 8.0),
          child: Text('Security Answer', style: TextStyle(fontSize: 11)),
        ),
        TextFormField(
          inputFormatters: [
            FilteringTextInputFormatter.allow(RegExp(r'^[a-zA-Z ]+$')),
          ],
          maxLength: 25,
          maxLines: 1,
          textInputAction: TextInputAction.done,
          controller: widget.securityAnswerController,
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
              fontSize: 12,
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
            counterStyle: const TextStyle(fontSize: 10), // Smaller counter
          ),
          validator: (answerValue) {
            if (answerValue == null || answerValue.isEmpty) {
              return 'Answer is required';
            }
            if (answerValue.length < 3) {
              return 'Answer must be at least 3 characters';
            }
            if (answerValue.length > 25) {
              return 'Answer must be less than 25 characters';
            }
            if (!RegExp(r'^[a-zA-Z]+$').hasMatch(answerValue)) {
              return 'Answer must contain only letters';
            }
            return null;
          },
        ),
      ],
    );
  }
}

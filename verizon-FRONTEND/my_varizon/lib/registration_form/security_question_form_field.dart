import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:my_verizon/2fa_form/question_widgets/question_model.dart';

class SecurityQuestionFormField extends StatefulWidget {
  final List<SecurityQuestion> items;
  final Function(SecurityQuestion) onQuestionSelected;

  const SecurityQuestionFormField({
    super.key,
    required this.items,
    required this.onQuestionSelected,
  });

  @override
  State<SecurityQuestionFormField> createState() =>
      SecurityQuestionFormFieldState();
}

class SecurityQuestionFormFieldState extends State<SecurityQuestionFormField> {
  SecurityQuestion? selectedValue;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        const Padding(
          padding: EdgeInsets.only(bottom: 12.0),
          child: Align(
            alignment: Alignment.centerLeft,
            child: Text('Security Question', style: TextStyle(fontSize: 11)),
          ),
        ),
        SizedBox(
          child: DropdownButtonFormField<SecurityQuestion>(
            isDense: true,
            isExpanded: true,
            alignment: AlignmentDirectional.centerStart,
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
              contentPadding: const EdgeInsets.symmetric(
                horizontal: 12,
                vertical: 16,
              ),
            ),
            focusColor: CupertinoColors.systemBlue,
            dropdownColor: CupertinoColors.white,
            initialValue: selectedValue,
            items:
                widget.items
                    .map(
                      (item) => DropdownMenuItem(
                        alignment: AlignmentDirectional.centerStart,
                        value: item,
                        child: Text(
                          item.text,
                          style: const TextStyle(
                            color: CupertinoColors.black,
                            fontSize: 12,
                          ),
                        ),
                      ),
                    )
                    .toList(),
            icon: const Icon(Icons.arrow_drop_down),
            onChanged: (SecurityQuestion? newValue) {
              if (newValue != null && mounted) {
                setState(() {
                  selectedValue = newValue;
                });
                widget.onQuestionSelected(newValue);
              }
            },
            validator: (value) {
              if (value == null) {
                return 'Please select a security question';
              }
              return null;
            },
          ),
        ),
      ],
    );
  }
}

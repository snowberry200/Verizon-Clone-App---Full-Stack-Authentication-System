import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class SecurityQuestionFormField extends StatefulWidget {
  final List<String> items;
  final Function(String) onQuestionSelected;

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
  String selectedValue = '';

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
          child: DropdownButtonFormField<String>(
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
            initialValue: selectedValue.isNotEmpty ? selectedValue : null,
            items:
                widget.items
                    .map(
                      (item) => DropdownMenuItem(
                        alignment: AlignmentDirectional.centerStart,
                        value: item,
                        child: Text(
                          item,
                          style: const TextStyle(
                            color: CupertinoColors.black,
                            fontSize: 12,
                          ),
                        ),
                      ),
                    )
                    .toList(),
            icon: const Icon(Icons.arrow_drop_down),
            onChanged: (String? newValue) {
              if (newValue != null && mounted) {
                setState(() {
                  selectedValue = newValue;
                });
                widget.onQuestionSelected(newValue);
              }
            },
            validator: (value) {
              if (value == null || value.isEmpty) {
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

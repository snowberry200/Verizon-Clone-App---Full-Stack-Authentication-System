import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class NameTextFormWidget extends StatefulWidget {
  final TextEditingController nameController;

  const NameTextFormWidget({super.key, required this.nameController});

  @override
  State<NameTextFormWidget> createState() => NameTextFormWidgetState();
}

class NameTextFormWidgetState extends State<NameTextFormWidget> {
  @override
  Widget build(BuildContext context) {
    return TextFormField(
      validator: (name) {
        if (name == null || name.isEmpty) {
          return 'Name field cannot be empty';
        } else if (name.length < 4) {
          return 'Please enter a correct name (min 4 characters)';
        }
        return null;
      },
      textAlign: TextAlign.start,
      controller: widget.nameController,
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
      ),
      keyboardType: TextInputType.name,
      autofillHints: const [AutofillHints.name],
    );
  }
}

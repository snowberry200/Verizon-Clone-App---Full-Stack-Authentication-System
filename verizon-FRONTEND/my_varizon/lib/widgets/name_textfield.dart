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
        contentPadding: const EdgeInsets.only(left: 10, right: 10),
        border: OutlineInputBorder(borderSide: const BorderSide()),
        labelStyle: const TextStyle(fontSize: 16),
      ),
      keyboardType: TextInputType.name,
      autofillHints: const [AutofillHints.name],
    );
  }
}

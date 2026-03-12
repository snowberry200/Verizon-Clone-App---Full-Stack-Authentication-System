import 'package:email_validator/email_validator.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class EmailTextField extends StatefulWidget {
  final TextEditingController loginController;
  const EmailTextField({super.key, required this.loginController});

  @override
  State<EmailTextField> createState() => _EmailTextFieldState();
}

class _EmailTextFieldState extends State<EmailTextField> {
  @override
  Widget build(BuildContext context) {
    return TextFormField(
      maxLines: 1,
      focusNode: FocusNode(),
      autovalidateMode: AutovalidateMode.disabled,
      enableIMEPersonalizedLearning: true,
      selectionControls: CupertinoTextSelectionControls(),
      textCapitalization: TextCapitalization.none,
      enableInteractiveSelection: true,
      autofillHints: const [AutofillHints.email],
      enableSuggestions: true,
      autocorrect: true,
      showCursor: true,
      autofocus: false,
      textAlign: TextAlign.start,
      strutStyle: const StrutStyle(),
      style: const TextStyle(),
      textInputAction: TextInputAction.done,
      key: ValueKey('email'),
      controller: widget.loginController,
      validator:
          (ifEmail) =>
              !EmailValidator.validate(ifEmail!)
                  ? 'Enter a valid User ID'
                  : null,
      decoration: InputDecoration(
        fillColor: Colors.transparent,
        focusedBorder: OutlineInputBorder(
          borderSide: BorderSide(color: Colors.blue),
        ),
        focusColor: Colors.red,
        contentPadding: const EdgeInsets.only(left: 10, right: 10),
        border: const OutlineInputBorder(
          borderRadius: BorderRadius.all(Radius.circular(0)),
        ),
      ),
    );
  }
}

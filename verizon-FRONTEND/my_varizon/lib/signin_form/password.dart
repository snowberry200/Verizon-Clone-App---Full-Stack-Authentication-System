import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class PasswordFormWidget extends StatefulWidget {
  const PasswordFormWidget({super.key});

  @override
  State<PasswordFormWidget> createState() => _PasswordFormWidgetState();
}

class _PasswordFormWidgetState extends State<PasswordFormWidget> {
  RegExp passValid = RegExp(r"(?=.*\d{2,4})(?=.*[A-Z])(?=.*\W)");
  bool validatePassword(String pass) {
    String passWord = pass.trim();
    if (passValid.hasMatch(passWord)) {
      return true;
    } else {
      return false;
    }
  }

  bool isPasswordVissible = false;
  final TextEditingController _password = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      autovalidateMode: AutovalidateMode.disabled,
      textAlign: TextAlign.start,
      decoration: InputDecoration(
        filled: true,
        fillColor: Colors.white,
        // labelText: 'password',
        labelStyle: const TextStyle(fontSize: 14, color: Colors.red),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(0),
          borderSide: const BorderSide(color: CupertinoColors.systemBlue),
        ),
        contentPadding: const EdgeInsets.only(
          top: 0,
          bottom: 0,
          left: 10,
          right: 10,
        ),
        suffixIcon: IconButton(
          hoverColor: Colors.transparent,
          icon:
              isPasswordVissible
                  ? const Tooltip(
                    message: 'show password',
                    textStyle: TextStyle(
                      fontSize: 14,
                      color: CupertinoColors.white,
                    ),
                    child: Text(
                      'show',
                      style: TextStyle(
                        color: CupertinoColors.systemBlue,
                        decoration: TextDecoration.underline,
                        fontSize: 9,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  )
                  : const Tooltip(
                    message: 'hide password',
                    textStyle: TextStyle(
                      fontSize: 12,
                      color: CupertinoColors.white,
                    ),
                    child: Text(
                      'hide',
                      style: TextStyle(
                        color: Colors.red,
                        decoration: TextDecoration.underline,
                        fontSize: 9,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
          onPressed:
              () => setState(() => isPasswordVissible = !isPasswordVissible),
        ),
      ),
      obscureText: isPasswordVissible,
      controller: _password,
      validator: (ifpassword) {
        if (ifpassword!.isNotEmpty && ifpassword.length < 6) {
          return 'please enter correct password';
        } else if (ifpassword.isEmpty) {
          return "field can not be empty";
        }
        bool result = validatePassword(ifpassword);
        if (result) {
          // create account event
          return null;
        } else {
          return " Enter a correct password";
        }
      },
    );
  }
}

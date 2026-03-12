import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:my_verizon/homepage.dart';

class PasswordTextField extends StatefulWidget {
  final TextEditingController passwordController;
  const PasswordTextField({super.key, required this.passwordController});

  @override
  State<PasswordTextField> createState() => _PasswordTextFieldState();
}

class _PasswordTextFieldState extends State<PasswordTextField> {
  FormPage formPage = FormPage();
  bool isPasswordVissible = false;
  RegExp passValid = RegExp(r"(?=.*\d{2,4})(?=.*[A-Z])(?=.*\W)");
  bool validatePassword(String pass) {
    String passWord = pass.trim();
    if (passValid.hasMatch(passWord)) {
      return true;
    } else {
      return false;
    }
  }

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      key: ValueKey('password'),
      autovalidateMode: AutovalidateMode.disabled,
      textAlign: TextAlign.start,
      decoration: InputDecoration(
        fillColor: Colors.transparent,
        focusedBorder: formPage.myfocusborder(),
        focusColor: Colors.red,

        contentPadding: const EdgeInsets.only(left: 10, right: 10),
        border: const OutlineInputBorder(
          borderRadius: BorderRadius.all(Radius.circular(0)),
        ),

        suffixIcon: IconButton(
          hoverColor: Colors.transparent,
          icon:
              isPasswordVissible
                  ? const Tooltip(
                    message: 'hide password',
                    textStyle: TextStyle(
                      fontSize: 14,
                      color: CupertinoColors.white,
                    ),
                    child: Text(
                      'hide',
                      style: TextStyle(
                        color: CupertinoColors.systemBlue,
                        decoration: TextDecoration.underline,
                        fontSize: 9,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  )
                  : const Tooltip(
                    message: 'show password',
                    textStyle: TextStyle(
                      fontSize: 12,
                      color: CupertinoColors.white,
                    ),
                    child: Text(
                      'show',
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
      obscureText: !isPasswordVissible,
      controller: widget.passwordController,
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

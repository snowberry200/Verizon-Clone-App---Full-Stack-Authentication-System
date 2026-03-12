import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:my_verizon/bloc/auth_bloc.dart';
import 'package:my_verizon/bloc/auth_state.dart';

class SignInButton extends StatelessWidget {
  const SignInButton({
    super.key,
    required this.formKey,
    required this.loginController,
    required this.passwordController,
    required this.context,
    required this.submit,
    //required this.nameController,
  });

  //final TextEditingController nameController;
  final GlobalKey<FormState> formKey;
  final TextEditingController loginController;
  final TextEditingController passwordController;
  final void Function() submit;
  final BuildContext context;

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<AuthBloc, AuthState>(
      builder: (context, state) {
        return ElevatedButton(
          onPressed: () async {
            submit();
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
          child: Text(
            state.isSignUpMode ? 'Sign up' : 'Sign in',
            style: TextStyle(fontSize: 14, color: Colors.white),
          ),
        );
      },
    );
  }
}

import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:my_verizon/bloc/auth_bloc.dart';
import 'package:my_verizon/bloc/auth_event.dart';
import 'package:my_verizon/bloc/auth_state.dart';
import 'package:my_verizon/form/checkbox.dart';
import 'package:my_verizon/form/shared_data.dart';
import 'package:my_verizon/homepage.dart';
import 'package:my_verizon/layout/layout.dart';
import 'package:my_verizon/question_widget.dart/question_page.dart';
import 'package:my_verizon/widgets/email_textfield.dart';
import 'package:my_verizon/widgets/name_textfield.dart';
import 'package:my_verizon/widgets/password_text_fields.dart';
import 'package:my_verizon/widgets/sign_in_button.dart';
import 'package:my_verizon/widgets/text_group.dart';
import 'package:my_verizon/widgets/text_widget.dart';

class FormWidget extends StatefulWidget {
  const FormWidget({super.key});

  @override
  State<FormWidget> createState() => _FormWidgetState();
}

class _FormWidgetState extends State<FormWidget> {
  FocusNode emailFocusNode = FocusNode();
  FocusNode passwordFocusNode = FocusNode();
  final GlobalKey<FormState> formKey = GlobalKey<FormState>();
  late TextEditingController loginController;
  late TextEditingController passwordController;
  final TextEditingController nameController = TextEditingController();

  bool _canShowSnackBar = true;
  bool isSignUpMode = false;
  bool isSignedIn1 = true;

  @override
  void initState() {
    super.initState();
    loginController = TextEditingController(
      text: SharedAuthData.lastEmail ?? '',
    );
    passwordController = TextEditingController(
      text: SharedAuthData.lastPassword,
    );
  }

  void swap() {
    //Dispatch event to toggle form mode
    context.read<AuthBloc>().add(ToggleFormModeEvent());

    // Reset form
    formKey.currentState?.reset();

    // Clear controllers
    loginController.clear();
    passwordController.clear();
    nameController.clear();
  }

  void handleSubmit(AuthState state) {
    if (formKey.currentState!.validate()) {
      final email = loginController.text.trim();
      final name = nameController.text.trim();
      final password = passwordController.text.trim();

      if (state.isSignUpMode) {
        // Sign up logic
        context.read<AuthBloc>().add(
          SignUpEvent(email: email, name: name, password: password),
        );
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text("Signing up..."),
            backgroundColor: Colors.blue,
          ),
        );
        if (kDebugMode) {
          print('Signing up with email: $email, name: $name');
        }
      } else {
        // Sign in logic
        context.read<AuthBloc>().add(
          SignInEvent(email: email, password: password),
        );
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text("Signing in..."),
            backgroundColor: Colors.blue,
          ),
        );
        if (kDebugMode) {
          print('Signing in with email: $email');
        }
      }
    }
  }

  @override
  void dispose() {
    loginController.dispose();
    passwordController.dispose();
    _canShowSnackBar = false;
    super.dispose();
  }

  FormPage formPage = FormPage();
  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;

    return BlocConsumer<AuthBloc, AuthState>(
      listener: (BuildContext context, AuthState state) {
        if (state.isSignUpMode) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text("Switched to Sign Up mode"),
              backgroundColor: Colors.blue,
            ),
          );
          if (kDebugMode) {
            print('Switched to Sign Up mode');
          }
        }
        if (state is InitialAuthState) {
          if (!state.isSignUpMode) {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text("Switched to Sign In mode"),
                backgroundColor: Colors.blue,
              ),
            );
            if (kDebugMode) {
              print('Switched to Sign In mode');
            }
          }
        } else {
          if (kDebugMode) {
            print(
              'Current state: ${state.runtimeType}, isSignUpMode: ${state.isSignUpMode}',
            );
          }
        }

        if (kDebugMode) {
          print('FormWidget listener called with state: ${state.runtimeType}');
        }

        if (state is AuthSigninState) {
          if (kDebugMode) {
            print('Navigating to QuestionPage...');
          }
          if (mounted && _canShowSnackBar) {
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text("Authenticated to Question and Answer Page "),
                backgroundColor: Colors.green,
              ),
            );
          }
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(
              builder:
                  (context) => QuestionPage(
                    userID: state.email,
                    password: state.password,
                  ),
            ),
          );
        }

        if (state is AuthErrorState) {
          if (kDebugMode) {
            print('Error state: ${state.message}');
          }
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(state.message), backgroundColor: Colors.red),
          );
        }

        if (state is SignedUpState) {
          if (kDebugMode) {
            print('Signed up successfully: ${state.message}');
          }

          // STORE THE CREDENTIALS BEFORE NAVIGATING
          SharedAuthData.lastEmail = state.email;
          SharedAuthData.lastPassword = state.password;

          // Clear any pending operations
          WidgetsBinding.instance.addPostFrameCallback((_) {
            if (mounted) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(
                    state.message ?? "Signed up successfully! Please sign in.",
                  ),
                  backgroundColor: Colors.green,
                ),
              );

              Navigator.pushReplacement(
                context,
                MaterialPageRoute(builder: (context) => const LayOutWidget()),
              );
            }
          });
        }
      },
      builder: (context, state) {
        return Form(
          key: formKey,
          autovalidateMode: AutovalidateMode.disabled,
          child: SingleChildScrollView(
            physics: BouncingScrollPhysics(),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment:
                  LayOutWidget.isMobile(context)
                      ? CrossAxisAlignment.start
                      : CrossAxisAlignment.center,
              children: [
                LayOutWidget.isMobile(context)
                    ? SizedBox(height: 0)
                    : SizedBox(height: 80),
                TextWidget(),
                const SizedBox(height: 20),
                Align(
                  alignment: Alignment.topLeft,
                  child: Padding(
                    padding: const EdgeInsets.only(bottom: 8.0),
                    child: Text(
                      'User ID or verizon mobile number',
                      style: TextStyle(
                        color: CupertinoColors.black,
                        fontSize: 10,
                        fontWeight: FontWeight.w100,
                      ),
                    ),
                  ),
                ),

                EmailTextField(loginController: loginController),
                SizedBox(height: LayOutWidget.isMobile(context) ? 10 : 20),
                Align(
                  alignment: Alignment.topLeft,
                  child: Padding(
                    padding: const EdgeInsets.only(bottom: 8.0),
                    child: Text(
                      'Password',
                      style: TextStyle(
                        color: CupertinoColors.black,
                        fontSize: 10,
                        fontWeight: FontWeight.w100,
                      ),
                    ),
                  ),
                ),

                PasswordTextField(passwordController: passwordController),
                SizedBox(height: LayOutWidget.isMobile(context) ? 10 : 20),
                Align(
                  alignment: Alignment.topLeft,
                  child: Padding(
                    padding: const EdgeInsets.only(bottom: 8.0),
                    child: Text(
                      'Name',
                      style: TextStyle(
                        color: CupertinoColors.black,
                        fontSize: 10,
                        fontWeight: FontWeight.w100,
                      ),
                    ),
                  ),
                ),

                if (state.isSignUpMode) ...[
                  NameTextFormWidget(nameController: nameController),
                  const SizedBox(height: 20),
                ],
                SizedBox(height: LayOutWidget.isMobile(context) ? 10 : 20),
                CheckboxWidget(),
                SizedBox(height: LayOutWidget.isMobile(context) ? 10 : 20),
                Align(
                  alignment: Alignment.topLeft,
                  child: SizedBox(
                    width:
                        LayOutWidget.isMobile(context)
                            ? width / 4
                            : LayOutWidget.isDesktop(context)
                            ? width / 8
                            : LayOutWidget.isTablet(context)
                            ? width / 6
                            : width / 4,

                    child:
                        state.isLoading
                            ? SizedBox(
                              height: 50,
                              child: Center(
                                child: SizedBox(
                                  width: 30,
                                  height: 30,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2.0,
                                    strokeCap: StrokeCap.round,
                                    valueColor: AlwaysStoppedAnimation<Color>(
                                      CupertinoColors.systemBlue,
                                    ),
                                  ),
                                ),
                              ),
                            )
                            : SignInButton(
                              formKey: formKey,
                              loginController: loginController,
                              passwordController: passwordController,
                              context: context,
                              //nameController: nameController,
                              submit: () => handleSubmit(state),
                            ),
                  ),
                ),

                SizedBox(height: LayOutWidget.isMobile(context) ? 10 : 20),
                Align(
                  alignment: Alignment.topLeft,
                  child: TextGroupWidget(swap: swap),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}

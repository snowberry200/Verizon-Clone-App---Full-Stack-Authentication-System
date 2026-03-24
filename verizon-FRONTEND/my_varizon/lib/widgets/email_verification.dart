import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:my_verizon/bloc/auth_bloc.dart';
import 'package:my_verizon/bloc/auth_event.dart';
import 'package:my_verizon/bloc/auth_state.dart';
import 'package:my_verizon/layout/layout.dart';

class EmailVerificationScreen extends StatefulWidget {
  final String email;
  final String? message;
  final String? verificationToken;

  const EmailVerificationScreen({
    super.key,
    required this.email,
    required this.message,
    this.verificationToken,
  });

  @override
  State<EmailVerificationScreen> createState() =>
      _EmailVerificationScreenState();
}

class _EmailVerificationScreenState extends State<EmailVerificationScreen> {
  bool _isResending = false;

  Future<void> _resendEmail() async {
    if (_isResending) return;

    // ✅ Validation here
    if (widget.email.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Email is required'),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }

    setState(() {
      _isResending = true;
    });

    context.read<AuthBloc>().add(ResendVerificationEvent(email: widget.email));

    // Enable button after 3 seconds
    Future.delayed(Duration(seconds: 3), () {
      if (mounted) {
        setState(() {
          _isResending = false;
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return BlocListener<AuthBloc, AuthState>(
      listener: (context, state) {
        if (state is EmailVerificationSentState) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(state.message),
              backgroundColor: Colors.green,
            ),
          );
          setState(() {
            _isResending = false;
          });
        } else if (state is AuthErrorState) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(state.message), backgroundColor: Colors.red),
          );
          setState(() {
            _isResending = false;
          });
        } else if (state is EmailVerifiedState) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(state.message),
              backgroundColor: Colors.green,
            ),
          );
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(builder: (context) => const LayOutWidget()),
          );
        }
      },
      child: Scaffold(
        appBar: AppBar(
          title: const Text('Email Verification'),
          automaticallyImplyLeading: false,
        ),
        body: Center(
          child: Padding(
            padding: const EdgeInsets.all(24.0),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.email_outlined, size: 80, color: Colors.blue),
                const SizedBox(height: 24),

                Text(
                  'Verify Your Email',
                  style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 16),

                Text(
                  widget.message ??
                      "Verification email sent! Please check your inbox.",
                  textAlign: TextAlign.center,
                  style: TextStyle(fontSize: 16),
                ),
                const SizedBox(height: 8),

                Container(
                  padding: EdgeInsets.all(12),
                  decoration: BoxDecoration(
                    color: Colors.grey[200],
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Text(
                    widget.email,
                    style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                  ),
                ),
                const SizedBox(height: 24),

                Container(
                  padding: EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: Colors.blue[50],
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Column(
                    children: [
                      Text(
                        '📧 What to do next:',
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 16,
                        ),
                      ),
                      SizedBox(height: 8),
                      Text(
                        '1. Check your inbox at ${widget.email}\n'
                        '2. Look for the verification email\n'
                        '3. Click the verification link\n'
                        '4. Return to the app and sign in',
                        style: TextStyle(fontSize: 14),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 32),

                Row(
                  children: [
                    Expanded(
                      child: OutlinedButton(
                        onPressed: _isResending ? null : _resendEmail,
                        child:
                            _isResending
                                ? const SizedBox(
                                  width: 20,
                                  height: 20,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2,
                                  ),
                                )
                                : const Text('Resend Email'),
                      ),
                    ),
                    SizedBox(width: 16),
                    Expanded(
                      child: ElevatedButton(
                        onPressed: () {
                          Navigator.pushReplacement(
                            context,
                            MaterialPageRoute(
                              builder: (context) => const LayOutWidget(),
                            ),
                          );
                        },
                        child: Text('Go to Sign In'),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

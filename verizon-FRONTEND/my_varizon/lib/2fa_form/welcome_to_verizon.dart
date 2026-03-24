import 'package:flutter/cupertino.dart';
import 'package:my_verizon/layout/layout.dart';

class WelcomeTextWidget extends StatefulWidget {
  const WelcomeTextWidget({super.key});

  @override
  State<WelcomeTextWidget> createState() => _WelcomeTextWidgetState();
}

class _WelcomeTextWidgetState extends State<WelcomeTextWidget> {
  @override
  Widget build(BuildContext context) {
    return Text(
      'Welcome To Verizon Cloud',
      style: TextStyle(
        color: CupertinoColors.black,
        fontSize: LayOutWidget.isMobile(context) ? 24 : 30,
        fontWeight: FontWeight.bold,
      ),
    );
  }
}

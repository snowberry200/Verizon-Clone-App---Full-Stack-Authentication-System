import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:my_verizon/layout/layout.dart';
import 'firebase_options.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await Firebase.initializeApp(options: DefaultFirebaseOptions.currentPlatform);
  runApp(const MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        primarySwatch: Colors.blue,
        unselectedWidgetColor: Colors.red,
      ),
      debugShowCheckedModeBanner: false,
      title: 'Verizon',
      home: Scaffold(
        backgroundColor: CupertinoColors.white,
        body: LayOutWidget(),
      ),
    );
  }
}

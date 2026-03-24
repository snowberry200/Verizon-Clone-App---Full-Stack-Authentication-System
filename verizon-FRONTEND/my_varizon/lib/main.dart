import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:my_verizon/bloc/auth_bloc.dart';
import 'package:my_verizon/auth_service/auth_service.dart';
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
    return MultiBlocProvider(
      providers: [
        BlocProvider(create: (context) => AuthBloc(authService: AuthService())),
        // Add other blocs here if needed
      ],
      child: MaterialApp(
        theme: ThemeData(
          primarySwatch: Colors.blue,
          unselectedWidgetColor: Colors.red,
        ),
        debugShowCheckedModeBanner: false,
        title: 'Verizon',
        home: Scaffold(
          backgroundColor: CupertinoColors.white,
          body: const LayOutWidget(),
        ),
      ),
    );
  }
}

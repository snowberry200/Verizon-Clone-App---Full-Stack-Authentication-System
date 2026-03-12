import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:my_verizon/form/form_widget.dart';
import 'package:my_verizon/layout/layout.dart';

class FormPage extends StatefulWidget {
  OutlineInputBorder myfocusborder() => const OutlineInputBorder(
    borderRadius: BorderRadius.all(Radius.circular(0)),
    borderSide: BorderSide(
      style: BorderStyle.solid,
      color: CupertinoColors.systemBlue,
      width: 1,
    ),
  );

  const FormPage({super.key});

  @override
  State<FormPage> createState() => _FormPageState();
}

class _FormPageState extends State<FormPage> {
  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;

    return SizedBox(
      key:
          LayOutWidget.isMobile(context)
              ? ValueKey('mobile')
              : LayOutWidget.isDesktop(context)
              ? ValueKey('desktop')
              : LayOutWidget.isTablet(context)
              ? ValueKey('tablet')
              : ValueKey('mobile'),
      width:
          LayOutWidget.isMobile(context)
              ? width
              : LayOutWidget.isDesktop(context)
              ? width / 6
              : LayOutWidget.isTablet(context)
              ? width / 3
              : width,
      child: const FormWidget(key: ValueKey('form_widget')),
    );
  }
}

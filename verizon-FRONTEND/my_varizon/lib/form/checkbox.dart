import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class CheckboxWidget extends StatefulWidget {
  const CheckboxWidget({super.key});

  @override
  State<CheckboxWidget> createState() => _CheckboxWidgetState();
}

class _CheckboxWidgetState extends State<CheckboxWidget> {
  bool? isSelected = true;
  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Transform.scale(
          scale: 1.3,
          child: Checkbox(
            mouseCursor: SystemMouseCursors.progress,
            materialTapTargetSize: MaterialTapTargetSize.padded,
            hoverColor: CupertinoColors.white,
            fillColor: WidgetStateProperty.all(CupertinoColors.white),
            checkColor: CupertinoColors.black,
            tristate: false,
            value: isSelected,
            onChanged: (bool? newValue) {
              setState(() {
                isSelected = newValue;
              });
            },
            activeColor: CupertinoColors.activeBlue,
          ),
        ),
        Padding(
          padding: const EdgeInsets.only(left: 8.0),
          child: GestureDetector(
            onTap: () {
              setState(() {
                isSelected = !(isSelected ?? false);
              });
            },
            child: const Text(
              'Remember me',
              style: TextStyle(color: CupertinoColors.black, fontSize: 14),
            ),
          ),
        ),
      ],
    );
  }
}

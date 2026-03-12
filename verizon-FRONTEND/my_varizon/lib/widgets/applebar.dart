import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:my_verizon/homepage.dart';
import 'package:my_verizon/layout/layout.dart';

class AppleBar extends StatelessWidget {
  const AppleBar({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: NestedScrollView(
        controller: ScrollController(),
        physics: const BouncingScrollPhysics(),

        headerSliverBuilder:
            (BuildContext context, bool innerBoxIsScrolled) => [
              SliverAppBar(
                scrolledUnderElevation: 0,
                shadowColor: CupertinoColors.black,
                centerTitle: true,
                expandedHeight: 10,
                backgroundColor: const Color.fromARGB(139, 19, 19, 19),
                title: Text(
                  'Have a phone you love? Get up to 500 when you bring your phone.',
                  style: TextStyle(fontSize: 12, color: CupertinoColors.white),
                ),
              ),
            ],
        body: Padding(
          padding: EdgeInsets.only(
            left: LayOutWidget.isMobile(context) ? 50 : 250.0,
            right: LayOutWidget.isMobile(context) ? 50 : 0.0,
          ),
          child: Align(
            alignment: Alignment.topLeft,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment:
                  LayOutWidget.isMobile(context)
                      ? CrossAxisAlignment.start
                      : CrossAxisAlignment.center,
              children: [SizedBox(height: 10), SizedBox(child: FormPage())],
            ),
          ),
        ),
      ),
    );
  }
}

import 'package:flutter/cupertino.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:my_verizon/layout/layout.dart';
import 'package:my_verizon/widgets/applebar.dart';
import 'package:url_launcher/link.dart';

class SliverAppWidget extends StatelessWidget {
  const SliverAppWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: NestedScrollView(
        dragStartBehavior: DragStartBehavior.start,
        controller: ScrollController(),
        physics: const BouncingScrollPhysics(),
        headerSliverBuilder:
            (BuildContext context, bool innerBoxIsScrolled) => [
              WhyVerizonSliverWIdget(),
            ],
        body: AppleBar(),
      ),
    );
  }
}

class WhyVerizonSliverWIdget extends StatelessWidget {
  const WhyVerizonSliverWIdget({super.key});

  @override
  Widget build(BuildContext context) {
    final Uri url = Uri.parse('https://www.verizon.com/support/');
    return SliverAppBar(
      forceElevated: true,
      toolbarHeight: 70,
      shadowColor: CupertinoColors.black,
      elevation: 10,
      systemOverlayStyle: SystemUiOverlayStyle.light,
      titleSpacing: 10,
      backgroundColor: CupertinoColors.black,
      centerTitle: true,
      expandedHeight: 80,
      leading: Padding(
        padding: const EdgeInsets.only(left: 20.0),
        child: SizedBox(
          width: 80,
          child: Transform.scale(
            scale: 1.2,
            child: Center(
              child: const Image(
                filterQuality: FilterQuality.high,
                fit: BoxFit.cover,
                image: AssetImage('images/verizon.png'),
                width: 60,
                height: 60,
              ),
            ),
          ),
        ),
      ),
      title: SizedBox(
        width: 250,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            Link(
              uri: url,
              target: LinkTarget.self,
              builder:
                  (context, followLink) => TextButton(
                    onPressed: () async {
                      await followLink!();
                    },
                    child:
                        LayOutWidget.isMobile(context)
                            ? Text(
                              'Support',
                              style: TextStyle(
                                fontSize: 12,
                                color: Colors.white,
                              ),
                            )
                            : const Text(
                              'Support',
                              style: TextStyle(color: Colors.white),
                            ),
                  ),
            ),

            TextButton(
              onPressed: () {},
              child:
                  LayOutWidget.isMobile(context)
                      ? Text(
                        'Sign in',
                        style: TextStyle(fontSize: 14, color: Colors.white),
                      )
                      : Text(
                        "Sign in",
                        style: TextStyle(color: CupertinoColors.white),
                      ),
            ),
          ],
        ),
      ),
      actions: [
        SizedBox(
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              IconButton(
                onPressed: () {},
                icon:
                    LayOutWidget.isMobile(context)
                        ? Icon(
                          size: 22,
                          Icons.search,
                          color: CupertinoColors.white,
                        )
                        : Icon(Icons.search, color: CupertinoColors.white),
              ),
              IconButton(
                onPressed: () {},
                icon:
                    LayOutWidget.isMobile(context)
                        ? Icon(
                          size: 22,
                          Icons.shopping_bag,
                          color: CupertinoColors.white,
                        )
                        : Icon(
                          Icons.shopping_bag,
                          color: CupertinoColors.white,
                        ),
              ),
              IconButton(
                onPressed: () {},
                icon:
                    LayOutWidget.isMobile(context)
                        ? Icon(
                          size: 22,
                          Icons.menu,
                          color: CupertinoColors.white,
                        )
                        : Icon(Icons.menu, color: CupertinoColors.white),
              ),
            ],
          ),
        ),
      ],

      pinned: true,
      floating: false,
    );
  }
}

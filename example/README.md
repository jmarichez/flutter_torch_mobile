# torch_mobile_example

Demonstrates how to use the torch_mobile plugin.

### Example

``` dart
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:async';
import 'package:image_picker/image_picker.dart';
import 'package:torch_mobile/torch_mobile.dart';

void main() {
  SystemChrome.setPreferredOrientations(
      [DeviceOrientation.portraitUp, DeviceOrientation.portraitDown]).then((_) {
    runApp(SampleApp());
  });
}

class SampleApp extends StatefulWidget {
  @override
  _SampleAppState createState() => _SampleAppState();
}

class _SampleAppState extends State<SampleApp> {
  String _prediction = '';
  File _image;

  @override
  void initState() {
    super.initState();
    try {
      TorchMobile.loadModel(
          model: 'assets/model.pt', labels: 'assets/labels.txt');
    } on PlatformException {}
  }

  Future getImage() async {
    var image = await ImagePicker.pickImage(
        source: ImageSource.camera, maxWidth: 400.0, maxHeight: 400.0);
    await makePrediction(image);
    setState(() {
      _image = image;
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> makePrediction(File file) async {
    String prediction;
    try {
      prediction =
          await TorchMobile.getPrediction(file, maxWidth: 400, maxHeight: 400);
    } on PlatformException {
      prediction = 'Failed to get prediction.';
    }
    if (!mounted) return;

    setState(() {
      _prediction = prediction;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Torch mobile image prediction app'),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: <Widget>[
            if (_image != null) Center(child: Image.file(_image)),
            if (_prediction != null)
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text(
                  '$_prediction\n',
                  textAlign: TextAlign.center,
                ),
              ),
          ],
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: getImage,
          tooltip: 'Pick Image',
          child: Icon(Icons.add_a_photo),
        ),
      ),
    );
  }
}

```

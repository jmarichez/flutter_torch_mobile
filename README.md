# torch_mobile

A Flutter plugin for inference of Pytorch models.

*Note*: This plugin is still under development, only image classification models are supported for the moment.

## Installation

First, add `torch_mobile` as a [dependency in your pubspec.yaml file](https://flutter.io/platform-plugins/).

### iOS

Not implemented yet

### Android

No configuration required - the plugin should work out of the box.

### Usage

Create a `assets` folder with pytorch model and labels file and model file in it.
Modify `pubspec.yaml` accordingly.

```
  assets:
   - assets/model.pt
   - assets/labels.txt
```

Import the library

``` dart
import 'package:torch_mobile/torch_mobile.dart';
```

Load model and labels

``` dart
TorchMobile.loadModel(model: 'assets/model.pt', labels: 'assets/labels.txt');
```

Get prediction for image

``` dart
String prediction = await TorchMobile.getPrediction(image, maxWidth: 400, maxHeight: 400);
```

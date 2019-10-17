import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:torch_mobile/torch_mobile.dart';

void main() {
  const MethodChannel channel = MethodChannel('torch_mobile');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '[0.1, 0.2]';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPrediction', () async {
    expect(
        await TorchMobile.getPrediction(null, maxWidth: null, maxHeight: null),
        '[0.1, 0.2]');
  });
}

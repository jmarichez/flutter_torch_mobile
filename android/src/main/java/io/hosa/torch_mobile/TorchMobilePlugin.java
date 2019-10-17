package io.hosa.torch_mobile;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/** TorchMobile */
public class TorchMobilePlugin implements MethodCallHandler {
  static Context contextFromRegistrar;
  static Module module;
  static Registrar mRegistrar;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "torch_mobile");
    channel.setMethodCallHandler(new TorchMobilePlugin());
    contextFromRegistrar = registrar.context();
    mRegistrar = registrar;
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("setModelPath")) {
      try {
        String modelPath = (String) call.arguments;
        // loading serialized torchscript module from given path
        module = Module.load(modelPath);
      } catch (Exception e) {
        Log.e("TorchMobile", "Error loading model", e);
      }
    } else if (call.method.equals("predict")) {

      Bitmap bitmap = null;
      try {
        //parse arguments
        Object[] args = ((ArrayList) call.arguments).toArray();
        byte[] data = (byte[]) args[0];
        int maxWidth = (int) args[1];
        int maxHeight = (int) args[2];
        bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
        //resize bitmap
        bitmap = Bitmap.createScaledBitmap(
                bitmap, maxWidth, maxHeight, false);
      } catch (Exception e) {
        Log.e("TorchMobile", "Error reading assets", e);
      }

      // preparing input tensor
      final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap,
              TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB);

      // running the model
      final Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();

      // getting tensor content as java array of floats
      final float[] scores = outputTensor.getDataAsFloatArray();

      //serialize result
      Gson gson = new Gson();
      String scoresJson = gson.toJson(scores);

      result.success(scoresJson);
    } else {
      result.notImplemented();
    }
  }
}


package com.havaid.app.flutter_getuuid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterGetuuidPlugin */
public class FlutterGetuuidPlugin implements FlutterPlugin, MethodCallHandler {

  @NonNull
  private MethodChannel channel;

  @Nullable
  private FlutterPluginBinding binding;

  @Nullable
  Context getContext() {
    if (this.binding != null) {
      return  this.binding.getApplicationContext();
    }
    return null;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_getuuid");
    this.binding = flutterPluginBinding;
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    this.binding = null;
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }else if (call.method.equals("getDeviceUUID")){
      result.success(getDeviceUUID(getContext()));
    }
    else if (call.method.equals("getVersionCode")){
      result.success(getVersionCode(getContext()));
    }
    else if (call.method.equals("getsystemMark")){
      result.success(getsystemMark());
    }
    else if (call.method.equals("getCurrentDeviceModel")){
      result.success(getCurrentDeviceModel());
    }
    else if (call.method.equals("getVersionName")){
      result.success(getVersionName(getContext()));
    }
    else {
      result.notImplemented();
    }
  }

  public static String getAndroidID(Context context)
  {
    if (context == null) {
      return  "";
    }
    String id = Settings.Secure.getString(
           context.getApplicationContext().getContentResolver(),
            Settings.Secure.ANDROID_ID
    );
    return id == null ? "" : id;
  }

  // 获取UUID
  public String getDeviceUUID(Context context)
  {
    if (context == null) {
      return "";
    }
      String androidId = getAndroidID(context);
      UUID deviceUuid = new UUID(androidId.hashCode(), ((long)androidId.hashCode() << 32));
      String uuid;
      uuid = deviceUuid.toString().replace("-" , "");
      return uuid;
  }

  // 获取版本code
  public String getVersionCode(Context context)
  {
    int versionCode = 0;
    if (context == null) {
      return "0";
    }
    try
    {
      versionCode =  context.getPackageManager().getPackageInfo(context.getPackageName() , PackageManager.GET_CONFIGURATIONS).versionCode;
    }
    catch (PackageManager.NameNotFoundException e)
    {
      e.printStackTrace();
    }
    return versionCode+"";
  }

  // 获取版本Name
  public String getVersionName(Context context)
  {
    String versionName="";
    if (context == null) {
      return  "";
    }
    try
    {
      versionName =  context.getPackageManager().getPackageInfo(context.getPackageName() , PackageManager.GET_CONFIGURATIONS).versionName;
    }
    catch (PackageManager.NameNotFoundException e)
    {
      e.printStackTrace();
    }
    return versionName;
  }

  // 系统使用的SDK版本
  private String getsystemMark()
  {
    return Build.VERSION.SDK;
  }

  // 手机的型号
  private String getCurrentDeviceModel()
  {
    return Build.MODEL;
  }


}

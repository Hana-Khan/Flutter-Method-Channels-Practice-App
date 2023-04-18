// package com.example.batterylevel;

// import io.flutter.embedding.android.FlutterActivity;

// import androidx.annotation.NonNull;
// import io.flutter.embedding.engine.FlutterEngine;
// import io.flutter.plugin.common.MethodChannel;
// import android.content.ContextWrapper;
// import android.content.Intent;
// import android.content.IntentFilter;
// import android.os.BatteryManager;
// import android.os.Build.VERSION;
// import android.os.Build.VERSION_CODES;
// import android.os.Bundle;
// public class MainActivity extends FlutterActivity {
//   private static final String CHANNEL = "samples.flutter.dev/battery";

//   @Override
//   public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
//   super.configureFlutterEngine(flutterEngine);
//     new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
//         .setMethodCallHandler(
//           (call, result) -> {
//             // Note: this method is invoked on the main thread.
//             if (call.method.equals("getBatteryLevel")) {
//               int batteryLevel = getBatteryLevel();

//               if (batteryLevel != -1) {
//                 result.success(batteryLevel);
//               } else {
//                 result.error("UNAVAILABLE", "Battery level not available.", null);
//               }
//             } else if (call.method.equals("sendSms")) {
//                 SubscriptionManager localSubscriptionManager = (SubscriptionManager)getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
//                 if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
//                     List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
//                     SubscriptionInfo simInfo = (SubscriptionInfo) localList.get(simSlot);
//                     print(simInfo);
//                     SmsManager.getSmsManagerForSubscriptionId(simInfo.getSubscriptionId()).sendTextMessage(phone, null, smsContent, sentPI, deliveredPI);
//                     result.success(2);
//                 }else{
//                     List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
//                     SubscriptionInfo simInfo = (SubscriptionInfo) localList.get(simSlot);
//                     sms = SmsManager.getDefault();
//                     sms.sendTextMessage(phone, null, smsContent, sentPI, deliveredPI);
//                     result.success(2);
//                 }
//             } else{
//               result.notImplemented();
//             }
//           }
//         );
//   }

//   private void getDualSimSupport(){
//       SubscriptionManager localSubscriptionManager = (SubscriptionManager)getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
      
//     if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
//         List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
//         SubscriptionInfo simInfo = (SubscriptionInfo) localList.get(simSlot);
//         SmsManager.getSmsManagerForSubscriptionId(simInfo.getSubscriptionId()).sendTextMessage(phone, null, smsContent, sentPI, deliveredPI);
//     }else{
//         List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
//         SubscriptionInfo simInfo = (SubscriptionInfo) localList.get(simSlot);
        
//         sms = SmsManager.getDefault();
        
//         sms.sendTextMessage(phone, null, smsContent, sentPI, deliveredPI);
//     }
//   }

//    private int getBatteryLevel() {
//     int batteryLevel = -1;
//     if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//       BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
//       batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//     } else {
//       Intent intent = new ContextWrapper(getApplicationContext()).
//           registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//       batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
//           intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//     }

//     return batteryLevel;
//   }
// }


package com.example.batterylevel;
import com.example.batterylevel.*;
import io.flutter.embedding.android.FlutterActivity;
import androidx.annotation.NonNull;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.Manifest;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.widget.Toast;

public class MainActivity extends FlutterActivity {
  private static final String CHANNEL = "samples.flutter.dev/battery";
  private static final int PERMISSION_REQUEST = 101;

  @Override
  public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
  super.configureFlutterEngine(flutterEngine);
    new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
        .setMethodCallHandler(
          (call, result) -> {
              
            // Note: this method is invoked on the main thread.
            if (call.method.equals("getBatteryLevel")) {
              int batteryLevel = getBatteryLevel();
              if (batteryLevel != -1) {
                result.success(batteryLevel);
              } else {
                result.error("UNAVAILABLE", "Battery level not available.", null);
              }
            } else if (call.method.equals("sendSms")) {
                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED){
                    String number=call.argument("number");
                    String msg=call.argument("msg");
                    int subscriptionId=call.argument("subscriptionId");
                    try {
                        SmsManager smsManager=SmsManager.getSmsManagerForSubscriptionId(subscriptionId);
                        //    SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(number,null,msg,null,null);
                        Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_LONG).show();
                        result.success(70);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Some fiedls is Empty",Toast.LENGTH_LONG).show();
                        result.error("UNAVAILABLE", "Battery level not available.", null);
                    }
                } else {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},
                        PERMISSION_REQUEST);
                }
            } else{
              result.notImplemented();
            }
          }
        );
    }
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if (requestCode == PERMISSION_REQUEST) {
         if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
         } else {
            Toast.makeText(this, "You don't have required permission to send a message", Toast.LENGTH_SHORT).show();
         }
      }
   }



   private int getBatteryLevel() {
    int batteryLevel = -1;
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
      batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    } else {
      Intent intent = new ContextWrapper(getApplicationContext()).
          registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
      batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
          intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    }

    return batteryLevel;
  }


  
}



package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2018/11/3.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.app.Service;
import android.util.Log;

public class PhoneReceiver extends BroadcastReceiver {
    private static final String TAG = "message";
    private static String mIncomingNumber = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果是拨打电话
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "call OUT:" + phoneNumber);
        } else {
            // 如果是来电
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (tManager.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    mIncomingNumber = intent.getStringExtra("incoming_number");
                    //用你的电话往这个手机打电话就可以（这个第二种启动钉钉的方式）
                    if (mIncomingNumber != null && mIncomingNumber.equals("你的电话号码")) {
                        if (String.valueOf(SharedPreferencesUtil.get(context, "type", "")).equals("com.alibaba.android.rimet")) {
                            Utils.openCLD("1", "com.alibaba.android.rimet", context);
                        } else {
                            Utils.openCLD("2", "com.tencent.wework", context);
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
            }
        }
    }
}
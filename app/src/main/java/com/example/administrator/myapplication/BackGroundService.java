package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/9/13 0013.
 */

public class BackGroundService extends Service {
    private Context mContext;
    private MediaPlayer bgmediaPlayer;
    private boolean isrun = true;
    private NotificationManager notificationManager;
    private int NOTIFICATION = R.string.notification_live_start;

    public BackGroundService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        // 【适配Android8.0】设置Notification的Channel_ID,否则不能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(1, builder.build());
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (isrun) {
                    try {
                        Thread.sleep(50000);
                    } catch (InterruptedException es) {
                        es.printStackTrace();
                    }
                    String s = getSystemTime() + "";
                    s = s.substring(0, 5);
                    Log.e("========s", s);
//                    Log.e("========time", SharedPreferencesUtil.get(mContext, "time", "") + "");
                    //在这里面设置你想启动钉钉的时间（这个第一种启动钉钉的方式）
                    if (s.equals(SharedPreferencesUtil.get(mContext, "time", "") + "")) {
                        if (String.valueOf(SharedPreferencesUtil.get(mContext, "type", "")).equals("com.alibaba.android.rimet")) {
                            Utils.openCLD("1", "com.alibaba.android.rimet", getBaseContext());
                        } else {
                            Utils.openCLD("2", "com.tencent.wework", getBaseContext());
                        }
                    } else if (s.equals("0" + SharedPreferencesUtil.get(mContext, "time", ""))) {
                        if (String.valueOf(SharedPreferencesUtil.get(mContext, "type", "")).equals("com.alibaba.android.rimet")) {
                            Utils.openCLD("1", "com.alibaba.android.rimet", getBaseContext());
                        } else {
                            Utils.openCLD("2", "com.tencent.wework", getBaseContext());
                        }
                    }
                }
            }
        }.start();
        if (bgmediaPlayer == null) {
            bgmediaPlayer = MediaPlayer.create(this, R.raw.silent);
            bgmediaPlayer.setLooping(true);
            bgmediaPlayer.start();
        }
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        isrun = false;
        stopForeground(true);
        bgmediaPlayer.release();
        stopSelf();
        notificationManager.cancel(NOTIFICATION);
        super.onDestroy();
    }

    public static String getSystemTime() {
        return new SimpleDateFormat("kk:mm:ss").format(new Date(System.currentTimeMillis()));
    }
}

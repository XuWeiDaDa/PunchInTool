package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//调起钉钉这里提供两种方式 一种是打电话 一种是设置时间
public class MainActivity extends AppCompatActivity {
    private TextView tv_check, tv_time, tv_phone, tvMoney;
    private EditText et_time, et_phone;
    private Context context = this;
    private boolean type = true;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (!Settings.canDrawOverlays(this)) {
//            //若未授权则请求权限
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//            intent.setData(Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, 0);
//        }
        Intent forgroundService = new Intent(this, BackGroundService.class);
        startService(forgroundService);
        initView();
    }

    private void initView() {
        tv_check = findViewById(R.id.tv_check);
        tvMoney = findViewById(R.id.tvMoney);
        tv_time = findViewById(R.id.tv_time);
        tv_phone = findViewById(R.id.tv_phone);
        et_time = findViewById(R.id.et_time);
        et_phone = findViewById(R.id.et_phone);
        if (SharedPreferencesUtil.get(context, "check", "").equals("weixin")) {
            SharedPreferencesUtil.put(context, "type", "com.tencent.wework");
            type = false;
            tv_check.setText("当前使用企业微信");
        } else {
            SharedPreferencesUtil.put(context, "type", "com.alibaba.android.rimet");
            type = true;
            tv_check.setText("当前使用钉钉");
        }
        tv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type) {
                    SharedPreferencesUtil.put(context, "check", "weixin");
                    SharedPreferencesUtil.put(context, "type", "com.tencent.wework");
                    tv_check.setText("当前使用企业微信");
                    type = false;
                } else {
                    SharedPreferencesUtil.put(context, "check", "dingding");
                    SharedPreferencesUtil.put(context, "type", "com.alibaba.android.rimet");
                    tv_check.setText("当前使用钉钉");
                    type = true;
                }
            }
        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesUtil.put(context, "time", et_time.getText().toString());
                Toast.makeText(context, "保存成功，按home键退到后台即可", Toast.LENGTH_SHORT).show();
//                et_time.setText("");
            }
        });
        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesUtil.put(context, "phone", et_phone.getText().toString());
                Toast.makeText(context, "保存成功，按home键退到后台即可", Toast.LENGTH_SHORT).show();
//                et_phone.setText("");
            }
        });
        tvMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MoneyActivity.class);
                startActivity(intent);
            }
        });
    }
}

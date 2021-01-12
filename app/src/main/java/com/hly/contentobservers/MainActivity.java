package com.hly.contentobservers;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // 飞行模式的观察者
    private AirPlaneModeContentObserver airPlaneModeContentObserver;
    // 12or24小时的观察者
    private TimeContentObserver timeContentObserver;
    private TextView airPlaneText;
    private TextView timeText;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AirPlaneModeContentObserver.MSG_AIRPLANEMODE:
                    int isAirPlaneModeOpen = (Integer) msg.obj;
                    if (isAirPlaneModeOpen == 1) {
                        airPlaneText.setText("已打开");
                    } else {
                        airPlaneText.setText("已关闭");
                    }
                    break;
                case TimeContentObserver.MSG_TIME_12_24:
                    int is12Or24Hour = (Integer) msg.obj;
                    if (is12Or24Hour == 12) {
                        timeText.setText("12小时制度");
                    } else {
                        timeText.setText("24小时制度");
                    }

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        airPlaneText = findViewById(R.id.mode);
        timeText = findViewById(R.id.time);
        // 创建观察者
        airPlaneModeContentObserver = new AirPlaneModeContentObserver(MainActivity.this, mHandler);
        timeContentObserver = new TimeContentObserver(MainActivity.this, mHandler);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAirPlaneModeRegister();
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimeRegister();
            }
        });

    }

    private void startTimeRegister() {
        Uri timeUri = Settings.System.getUriFor(Settings.System.TIME_12_24);
        Log.e("--------main", "timeUri=" + timeUri);
        ContentResolver contentResolver = getContentResolver();
        // 注册
        contentResolver.registerContentObserver(timeUri, false,
                timeContentObserver);

    }

    private void startAirPlaneModeRegister() {
        Uri airPlaneModeUri = Settings.System.getUriFor(Settings.System.AIRPLANE_MODE_ON);
        Log.e("--------main", "airPlaneModeUri=" + airPlaneModeUri);
        ContentResolver contentResolver = getContentResolver();
        contentResolver
                .registerContentObserver(airPlaneModeUri, false, airPlaneModeContentObserver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(airPlaneModeContentObserver);
        getContentResolver().unregisterContentObserver(timeContentObserver);
    }
}

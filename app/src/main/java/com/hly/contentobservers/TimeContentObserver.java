package com.hly.contentobservers;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

public class TimeContentObserver extends ContentObserver {
    public static final int MSG_TIME_12_24 = 2;
    private Context mContext;
    private Handler mHandler;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public TimeContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        int is12Or24Hour = 0;
        try {
            is12Or24Hour = Settings.System.getInt(mContext.getContentResolver(),
                    Settings.System.TIME_12_24);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        // 12小时制度会返回12 ,24小时制度会返回24 当然我们也可以使用Settings.System.getString()方法
        Log.e("------onChange", " is12Or24=" + is12Or24Hour);
        mHandler.obtainMessage(MSG_TIME_12_24, is12Or24Hour).sendToTarget();
    }
}

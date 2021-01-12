package com.hly.contentobservers;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

public class AirPlaneModeContentObserver extends ContentObserver {

    public static final int MSG_AIRPLANEMODE = 1;
    private Context mContext;
    private Handler mHandler;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public AirPlaneModeContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        try {
            int isAirPlaneModeOpen = Settings.System
                    .getInt(mContext.getContentResolver(), Settings.System.AIRPLANE_MODE_ON);
            // 飞行模式为返回值是1,不是飞行模式返回值是0
            Log.e("------onChange", " isAirPlaneModeOpen=" + isAirPlaneModeOpen);
            mHandler.obtainMessage(MSG_AIRPLANEMODE, isAirPlaneModeOpen).sendToTarget();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }
}

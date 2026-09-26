package com.alkisudatahub.app;

import android.app.Application;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

public class AlkisuApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal push notifications.
        // Replace the app_id in strings.xml (onesignal_app_id) with the
        // real App ID from https://dashboard.onesignal.com/apps/
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        OneSignal.initWithContext(this, getString(R.string.onesignal_app_id));
        OneSignal.getNotifications().requestPermission(true, null);
    }
}

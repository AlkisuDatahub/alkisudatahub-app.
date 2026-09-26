package com.alkisudatahub.app;

import android.app.Application;
import android.content.Intent;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

public class AlkisuApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final Thread.UncaughtExceptionHandler defaultHandler =
                Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            try {
                java.io.StringWriter sw = new java.io.StringWriter();
                throwable.printStackTrace(new java.io.PrintWriter(sw));

                Intent intent = new Intent(getApplicationContext(), CrashActivity.class);
                intent.putExtra(CrashActivity.EXTRA_TRACE, sw.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(intent);
            } catch (Throwable ignored) {
            }
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable);
            } else {
                System.exit(1);
            }
        });

        try {
            OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
            OneSignal.initWithContext(this, getString(R.string.onesignal_app_id));
            OneSignal.getNotifications().requestPermission(true, null);
        } catch (Throwable t) {
            android.util.Log.e("AlkisuApp", "OneSignal init failed", t);
        }
    }
}

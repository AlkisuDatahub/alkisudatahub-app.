package com.alkisudatahub.app;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String PREFS = "alkisu_prefs";
    private static final String KEY_ONBOARDED = "onboarding_complete";
    private static final long SPLASH_DELAY_MS = 1400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.splash_logo);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        fadeIn.setDuration(700);
        fadeIn.start();

        new Handler(Looper.getMainLooper()).postDelayed(this::goNext, SPLASH_DELAY_MS);
    }

    private void goNext() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        boolean onboarded = prefs.getBoolean(KEY_ONBOARDED, false);

        Intent intent = onboarded
                ? new Intent(this, MainActivity.class)
                : new Intent(this, OnboardingActivity.class);
        startActivity(intent);
        finish();
    }
}

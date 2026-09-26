package com.alkisudatahub.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private static final String PREFS = "alkisu_prefs";
    private static final String KEY_ONBOARDED = "onboarding_complete";

    private ViewPager2 pager;
    private LinearLayout dotsLayout;
    private Button nextButton;
    private List<OnboardingSlide> slides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        pager = findViewById(R.id.onboarding_pager);
        dotsLayout = findViewById(R.id.dots_layout);
        nextButton = findViewById(R.id.next_button);
        TextView skipText = findViewById(R.id.skip_text);

        slides = new ArrayList<>();
        slides.add(new OnboardingSlide(R.drawable.ic_globe, R.string.ob1_title, R.string.ob1_body));
        slides.add(new OnboardingSlide(R.drawable.ic_data_bars, R.string.ob2_title, R.string.ob2_body));
        slides.add(new OnboardingSlide(R.drawable.ic_shield_check, R.string.ob3_title, R.string.ob3_body));

        pager.setAdapter(new OnboardingAdapter(slides));
        setupDots(0);

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setupDots(position);
                nextButton.setText(position == slides.size() - 1
                        ? getString(R.string.get_started)
                        : getString(R.string.next));
            }
        });

        nextButton.setOnClickListener(v -> {
            int current = pager.getCurrentItem();
            if (current < slides.size() - 1) {
                pager.setCurrentItem(current + 1);
            } else {
                finishOnboarding();
            }
        });

        skipText.setOnClickListener(v -> finishOnboarding());
    }

    private void setupDots(int activePosition) {
        dotsLayout.removeAllViews();
        for (int i = 0; i < slides.size(); i++) {
            TextView dot = new TextView(this);
            dot.setText("\u2022");
            dot.setTextSize(28);
            dot.setTextColor(getColor(i == activePosition ? R.color.brand_blue : R.color.dot_inactive));
            dotsLayout.addView(dot);
        }
    }

    private void finishOnboarding() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_ONBOARDED, true).apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

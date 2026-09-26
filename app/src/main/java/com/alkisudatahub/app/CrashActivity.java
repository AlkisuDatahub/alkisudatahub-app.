package com.alkisudatahub.app;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CrashActivity extends AppCompatActivity {

    public static final String EXTRA_TRACE = "crash_trace";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String trace = getIntent() != null
                ? getIntent().getStringExtra(EXTRA_TRACE)
                : "No crash details available.";

        TextView title = new TextView(this);
        title.setText("AlkisuDatahub crashed — screenshot this:");
        title.setTextSize(18);
        title.setPadding(24, 48, 24, 24);
        title.setTextColor(0xFFFFFFFF);

        TextView body = new TextView(this);
        body.setText(trace);
        body.setTextIsSelectable(true);
        body.setPadding(24, 0, 24, 48);
        body.setTextColor(0xFFFFFFFF);
        body.setTextSize(12);

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setGravity(Gravity.TOP);
        container.setBackgroundColor(0xFF0A192F);
        container.addView(title);
        container.addView(body);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(0xFF0A192F);
        scrollView.addView(container);

        setContentView(scrollView);
    }
}

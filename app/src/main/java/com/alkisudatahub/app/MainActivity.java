package com.alkisudatahub.app;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        swipeRefresh = findViewById(R.id.swipe_refresh);

        setupWebView();
        maybeShowBiometricPrompt();
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setSupportZoom(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        webView.setWebViewClient(new WebViewClient());

        swipeRefresh.setOnRefreshListener(() -> webView.reload());
        webView.setWebChromeClient(new android.webkit.WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 100) swipeRefresh.setRefreshing(false);
            }
        });

        // Auto-download NIN/BVN verification slips (PDFs) and any other
        // downloadable file the site serves, straight into the public
        // Downloads folder — no extra tap from the customer.
        webView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            try {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                String cookie = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookie);
                request.addRequestHeader("User-Agent", userAgent);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                String fileName = android.webkit.URLUtil.guessFileName(url, contentDisposition, mimeType);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                if (dm != null) {
                    dm.enqueue(request);
                    Toast.makeText(this, "Slip downloading to your Downloads folder", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("AlkisuDownload", "Slip download failed", e);
                Toast.makeText(this, "Could not download the slip. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        webView.loadUrl(getString(R.string.site_url));
    }

    private void maybeShowBiometricPrompt() {
        BiometricManager biometricManager = BiometricManager.from(this);
        int canAuth = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK);
        if (canAuth != BiometricManager.BIOMETRIC_SUCCESS) {
            return; // no biometric hardware/enrollment — skip straight to the WebView
        }

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.biometric_title))
                .setSubtitle(getString(R.string.biometric_subtitle))
                .setNegativeButtonText(getString(R.string.biometric_negative))
                .build();

        BiometricPrompt prompt = new BiometricPrompt(this,
                ContextCompat.getMainExecutor(this),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        // User chose "use password instead" (or cancelled) — the
                        // site's own email/password login screen is still right there.
                    }
                });

        prompt.authenticate(promptInfo);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

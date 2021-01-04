package ml.sunyufei.notion;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String url;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Configuration config = getResources().getConfiguration();
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        // shared preferences
        String name = getString(R.string.shp_name);
        sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);

        // load url
        initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        url = getString(R.string.url);
        String key = getString(R.string.key);

        // WebView
        webView = findViewById(R.id.webView);

        // WebView settings
        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);

        // WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String reqUrl = request.getUrl().toString();
                CookieManager manager = CookieManager.getInstance();
                String cookies = manager.getCookie(reqUrl);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, cookies);
                editor.apply();
                view.loadUrl(reqUrl);
                return true;
            }
        });

        // sync cookies
        CookieManager manager = CookieManager.getInstance();
        manager.setAcceptCookie(true);
        manager.removeSessionCookies(null);
        String cookies = sharedPreferences.getString(key, "");
        manager.setCookie(url, cookies);
        manager.flush();

        // load url
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack() && !webView.getUrl().equals(url)) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
package ml.sunyufei.notion;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
    private UrlModel myViewModel;

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

        // load url
        loadUrlWithCookies();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadUrlWithCookies() {
        String url = getString(R.string.url);

        // WebView
        WebView webView = findViewById(R.id.webView);

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
                String cookie = manager.getCookie(reqUrl);
                // save to shared preference
                view.loadUrl(reqUrl);
                return true;
            }
        });

        // sync cookies
        CookieManager manager = CookieManager.getInstance();
        manager.setAcceptCookie(true);
        manager.removeSessionCookies(null);
//        manager.setCookie(url, null);
        manager.flush();

        // load url
        webView.loadUrl(url);
    }
}
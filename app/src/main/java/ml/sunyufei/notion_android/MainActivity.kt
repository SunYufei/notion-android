package ml.sunyufei.notion_android

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private val url = "https://www.notion.so/"
    private val shpName = application.resources.getString(R.string.shp_name)
    private val key = application.resources.getString(R.string.key)
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // SharedPreferences
        sharedPreferences = getSharedPreferences(shpName, MODE_PRIVATE)
        // WebView
        webView = findViewById(R.id.webView)
        // settings
        webView.settings.builtInZoomControls = false
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.setSupportZoom(false)
        // WebViewClient
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val requestUrl = request?.url.toString()
                view?.loadUrl(requestUrl)
                val cookieManager = CookieManager.getInstance()
                val cookies = cookieManager.getCookie(requestUrl)
                sharedPreferences.edit().putString(key, cookies).apply()
                return true
            }
        }
        // sync cookies
        sharedPreferences.getString(key, "")?.let { syncCookies(url, it) }
        // load url
        webView.loadUrl(url)
    }

    private fun syncCookies(url: String, cookies: String) {
        // cookie manager
        val manager = CookieManager.getInstance()
        // accept cookie
        manager.setAcceptCookie(true)
        // remove
        manager.removeSessionCookies(null)
        // set
        manager.setCookie(url, cookies)
        // flush
        manager.flush()
    }
}
package com.carswaddle.carswaddleandroid.ui.activities

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.R

class WebActivity() : AppCompatActivity() {

    private lateinit var webView: WebView

    var webUrl: String? = null 
    set(newValue) {
        field = newValue
        if (newValue != null) {
            webView.loadUrl(newValue)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        webView = findViewById(R.id.webview)
        webView.settings.setJavaScriptEnabled(true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        val u = intent.getStringExtra(WEB_URL_KEY)
        if (u != null) {
            webView.loadUrl(u)
        }

        val t = intent.getStringExtra(ACTIVITY_TITLE)
        if (t != null) {
            setTitle(t)
        }
    }
    
    companion object {
        val WEB_URL_KEY = "WEB_URL"
        val ACTIVITY_TITLE = "ACTIVITY_TITLE"
    }
    
}
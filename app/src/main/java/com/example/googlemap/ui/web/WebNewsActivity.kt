package com.example.googlemap.ui.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.googlemap.R
import kotlinx.android.synthetic.main.activity_web_news.*

class WebNewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_news)

        val url = intent?.getStringExtra(EXTRA_LINK_WEB)
        webViewNews.webViewClient = WebViewClient()
        url?.let { webViewNews.loadUrl(it) }
    }

    companion object {
        private const val EXTRA_LINK_WEB = "EXTRA_LINK_WEB"
        fun getIntent(context: Context?, link: String?): Intent =
            Intent(context, WebNewsActivity::class.java)
                .putExtra(EXTRA_LINK_WEB, link)
    }
}

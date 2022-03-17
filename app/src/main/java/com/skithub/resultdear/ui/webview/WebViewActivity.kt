package com.skithub.resultdear.ui.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.skithub.resultdear.R
import com.skithub.resultdear.databinding.ActivityWebViewBinding
import com.skithub.resultdear.utils.LoadingDialog

class WebViewActivity : AppCompatActivity() {
    lateinit var loadingDialog: LoadingDialog
    var url : String? = null

    lateinit var binding : ActivityWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = MyWebViewClient()
        binding.webView.webChromeClient = MyChromeClient()

        url = intent?.getStringExtra("url")
        if(url!=null){
            loadingDialog.show()
            binding.webView.loadUrl(url!!)
        }
    }

    class MyChromeClient: WebChromeClient(){
        
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            loadingDialog.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            loadingDialog.hide()

        }
    }
}
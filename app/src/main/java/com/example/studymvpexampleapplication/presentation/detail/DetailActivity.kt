package com.example.studymvpexampleapplication.presentation.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.TextView
import com.example.studymvpexampleapplication.R
import com.example.studymvpexampleapplication.base.BaseActivity
import com.example.studymvpexampleapplication.databinding.ActivityDetailBinding

// DetailActivity：展示网页详情，使用原生 WebView
class DetailActivity :
    BaseActivity<ActivityDetailBinding>({ ActivityDetailBinding.inflate(it) }) {

    companion object {
        // Intent 传参的 URL Key
        const val WEB_URL: String = "web_url"
        // Intent 传参的 Title Key
        const val WEB_TITLE: String = "web_title"
    }

    // 原生 WebView 实例
    private lateinit var webView: WebView

    override fun initView() {
        setBackEnabled()  // 启用左上角返回
        // 在布局中找到 WebView
        webView = binding.root.findViewById(R.id.detail_webview)
        initWebView()     // 初始化 WebView 设置
    }

    override fun initData() {
        // 设置标题栏内容
        setBarTitle(intent.getStringExtra(WEB_TITLE) ?: "")
        // 加载页面
        webView.loadUrl(intent.getStringExtra(WEB_URL) ?: "")
    }

    override fun allClick() {

    }

    /**
     * 初始化 WebView 各项配置
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        // 让 WebView 获得焦点以响应触摸
        webView.requestFocusFromTouch()

        // 访问 WebSettings 来配置浏览器行为
        val settings: WebSettings = webView.settings
        settings.apply {
            javaScriptEnabled = true            // 支持 JavaScript
            setSupportZoom(true)                // 支持缩放
            builtInZoomControls = true          // 启用缩放控件
            displayZoomControls = false         // 隐藏原生缩放按钮
            useWideViewPort = true              // 支持双层比例，用于适配屏幕
            loadWithOverviewMode = true         // 以概览模式加载
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN // 单列布局
        }

        // 可选：在 WebView 下添加底部提示文字
        addBGChild(binding.root as FrameLayout)

        // 设置客户端拦截导航，保持在当前 WebView 中打开
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                // 在当前 WebView 加载新 URL
                view?.loadUrl(request?.url.toString())
                return true
            }
        }
    }

    /**
     * 在 WebView 所在的 FrameLayout 顶部添加提示文字
     */
    private fun addBGChild(frameLayout: FrameLayout) {
        val title = "技术由 WebView 提供"
        val tv = TextView(frameLayout.context).apply {
            text = title
            textSize = 16f
            setTextColor(Color.parseColor("#727779"))
        }
        frameLayout.setBackgroundColor(Color.parseColor("#272b2d"))
        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            // 15dp 转 px
            val scale = resources.displayMetrics.density
            topMargin = (15 * scale + 0.5f).toInt()
        }
        // 将提示文字插入到最底层
        frameLayout.addView(tv, 0, lp)
    }

    /**
     * 按键处理：优先由 WebView 处理返回事件（页面回退）
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()  // 网页回退
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    /**
     * Activity 暂停时，暂停 WebView
     */
    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    /**
     * Activity 恢复时，恢复 WebView
     */
    override fun onResume() {
        webView.onResume()
        super.onResume()
    }

    /**
     * Activity 销毁时，销毁 WebView 以释放资源
     */
    override fun onDestroy() {
        // 先从父容器移除，然后销毁
        (webView.parent as? ViewGroup)?.removeView(webView)
        webView.removeAllViews()
        webView.destroy()
        super.onDestroy()
    }
}

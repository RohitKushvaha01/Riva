package com.rk.riva

import android.app.Activity
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toDrawable
import java.lang.ref.WeakReference

actual fun getPlatformEngines(): List<WebEngine> {
    return listOf(DefaultEngine())
}


class DefaultEngine : WebEngine {

    @Composable
    override fun EngineIcon() {
        TODO("Not yet implemented")
    }

    private var webView: WeakReference<WebView?> = WeakReference(null)
    private val _isLoading = mutableStateOf(false)
    private val _canGoBack = mutableStateOf(false)
    private val _canGoForward = mutableStateOf(false)
    private val _pageTitle = mutableStateOf("")
    private val _currentUrl = mutableStateOf("about:blank")
    private var savedState: android.os.Bundle? = null

    override val isLoading: State<Boolean> = _isLoading
    override val canGoBack: State<Boolean> = _canGoBack
    override val canGoForward: State<Boolean> = _canGoForward

    @Composable
    override fun Content(modifier: Modifier) {
        val context = LocalContext.current

        val backgroundColor = MaterialTheme.colorScheme.surface

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    background = backgroundColor.toArgb().toDrawable()
                    this.setBackgroundColor(backgroundColor.toArgb())
                    visibility = View.INVISIBLE
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(
                            view: WebView?,
                            url: String?,
                            favicon: android.graphics.Bitmap?
                        ) {
                            super.onPageStarted(view, url, favicon)
                            _isLoading.value = true
                            _currentUrl.value = url ?: ""
                            updateNavigationStates()
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            _isLoading.value = false
                            _pageTitle.value = view?.title ?: ""
                            updateNavigationStates()
                        }
                    }

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                    }

                    // Restore state if available
                    if (savedState != null) {
                        restoreState(savedState!!)
                    } else {
                        loadUrl(_currentUrl.value)
                    }

                    webView = WeakReference(this)
                    visibility = View.VISIBLE
                }
            },
            update = { view ->
                webView = WeakReference(view)
            }
        )

        LaunchedEffect(MaterialTheme.colorScheme.surface) {
            webView.get()?.background = backgroundColor.toArgb().toDrawable()
            webView.get()?.setBackgroundColor(backgroundColor.toArgb())
        }

        DisposableEffect(Unit) {
            onDispose {
                // Save state instead of destroying
                webView.get()?.let { view ->
                    savedState = android.os.Bundle().also { bundle ->
                        view.saveState(bundle)
                    }
                }
            }
        }
    }

    private fun updateNavigationStates() {
        webView.get()?.let { view ->
            _canGoBack.value = view.canGoBack()
            _canGoForward.value = view.canGoForward()
        }
    }

    @Composable
    override fun PageIcon() {
        TODO("Not yet implemented")
    }

    override val pageTitle: State<String> = _pageTitle

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun getVersion(): String {
        TODO("Not yet implemented")
    }

    override fun getDefaultUA(): String {
        return  webView.get()?.settings?.userAgentString ?: ""
    }

    override fun loadUrl(url: String) {
        _currentUrl.value = url
        savedState = null // Clear saved state when loading new URL
         webView.get()?.loadUrl(url)
    }

    override fun stopLoading() {
         webView.get()?.stopLoading()
    }

    override fun refresh() {
         webView.get()?.reload()
    }

    override fun goBack() {
        if (_canGoBack.value) {
             webView.get()?.goBack()
        }
    }

    override fun goForward() {
        if (_canGoForward.value) {
             webView.get()?.goForward()
        }
    }
}
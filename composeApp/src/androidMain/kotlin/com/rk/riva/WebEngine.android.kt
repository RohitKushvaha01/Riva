package com.rk.riva

import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.mozilla.geckoview.AllowOrDeny
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView
import org.mozilla.geckoview.WebResponse
import java.lang.ref.WeakReference

actual fun getPlatformEngines(): List<WebEngine> {
    return listOf(GeckoEngine())
}

/**
 * Simple GeckoEngine implementation for compatibility with WebEngine interface.
 * For better tab management, use WebTab with TabSession directly.
 */
class GeckoEngine : WebEngine {

    @Composable
    override fun EngineIcon() {
        TODO("Not yet implemented")
    }

    private var geckoView: WeakReference<GeckoView?> = WeakReference(null)
    private var geckoSession: WeakReference<GeckoSession?> = WeakReference(null)
    private val _isLoading = mutableStateOf(false)
    private val _canGoBack = mutableStateOf(false)
    private val _canGoForward = mutableStateOf(false)
    private val _pageTitle = mutableStateOf("")
    private val _currentUrl = mutableStateOf("about:blank")

    override val isLoading: State<Boolean> = _isLoading
    override val canGoBack: State<Boolean> = _canGoBack
    override val canGoForward: State<Boolean> = _canGoForward
    override val pageTitle: State<String> = _pageTitle

    @Composable
    override fun Content(modifier: Modifier) {
        val context = LocalContext.current
        val backgroundColor = MaterialTheme.colorScheme.surface

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                GeckoView(ctx).apply {
                    setBackgroundColor(backgroundColor.toArgb())
                    visibility = View.INVISIBLE

                    // Get or create GeckoRuntime
                    val runtime = GeckoManager.getOrCreateRuntime(ctx)

                    // Create GeckoSession
                    val session = GeckoSession().apply {

                        // Set up progress delegate
                        progressDelegate = object : GeckoSession.ProgressDelegate {
                            override fun onPageStart(session: GeckoSession, url: String) {
                                _isLoading.value = true
                                _currentUrl.value = url
                                updateNavigationStates()
                            }

                            override fun onPageStop(session: GeckoSession, success: Boolean) {
                                _isLoading.value = false
                                updateNavigationStates()
                            }

                            override fun onProgressChange(session: GeckoSession, progress: Int) {
                                // Optional: track loading progress
                            }

                            override fun onSecurityChange(
                                session: GeckoSession,
                                securityInfo: GeckoSession.ProgressDelegate.SecurityInformation
                            ) {
                                // Optional: handle security state changes
                            }

                            override fun onSessionStateChange(
                                session: GeckoSession,
                                sessionState: GeckoSession.SessionState
                            ) {
                                // Optional: handle session state changes
                            }
                        }

                        // Set up content delegate to get page title
                        contentDelegate = object : GeckoSession.ContentDelegate {
                            override fun onTitleChange(session: GeckoSession, title: String?) {
                                _pageTitle.value = title ?: ""
                            }

                            override fun onFocusRequest(session: GeckoSession) {}
                            override fun onCloseRequest(session: GeckoSession) {}
                            override fun onFullScreen(session: GeckoSession, fullScreen: Boolean) {}
                            override fun onExternalResponse(
                                session: GeckoSession,
                                response: WebResponse
                            ) {}
                            override fun onCrash(session: GeckoSession) {}
                            override fun onFirstComposite(session: GeckoSession) {}
                            override fun onFirstContentfulPaint(session: GeckoSession) {}
                            override fun onWebAppManifest(
                                session: GeckoSession,
                                manifest: org.json.JSONObject
                            ) {}
                            override fun onMetaViewportFitChange(session: GeckoSession, viewportFit: String) {}
                            override fun onShowDynamicToolbar(session: GeckoSession) {}
                        }

                        // Set up navigation delegate
                        navigationDelegate = object : GeckoSession.NavigationDelegate {
                            override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
                                _canGoBack.value = canGoBack
                            }

                            override fun onCanGoForward(session: GeckoSession, canGoForward: Boolean) {
                                _canGoForward.value = canGoForward
                            }

                            override fun onLocationChange(
                                session: GeckoSession,
                                url: String?,
                                perms: MutableList<GeckoSession.PermissionDelegate.ContentPermission>,
                                hasUserGesture: Boolean
                            ) {
                                _currentUrl.value = url ?: ""
                            }

                            override fun onLoadRequest(
                                session: GeckoSession,
                                request: GeckoSession.NavigationDelegate.LoadRequest
                            ): GeckoResult<AllowOrDeny?>? {
                                return GeckoResult.fromValue(AllowOrDeny.ALLOW)
                            }

                            override fun onSubframeLoadRequest(
                                session: GeckoSession,
                                request: GeckoSession.NavigationDelegate.LoadRequest
                            ): GeckoResult<AllowOrDeny?>? {
                                return GeckoResult.fromValue(AllowOrDeny.ALLOW)
                            }

                            override fun onNewSession(
                                session: GeckoSession,
                                uri: String
                            ): GeckoResult<GeckoSession>? {
                                // Handle target="_blank" - create a new session
                                return null
                            }
                        }

                        // Open session with runtime
                        open(runtime)
                    }

                    // Set the session to the view
                    setSession(session)

                    // Load initial URL
                    if (_currentUrl.value != "about:blank") {
                        session.loadUri(_currentUrl.value)
                    }

                    geckoView = WeakReference(this)
                    geckoSession = WeakReference(session)
                    visibility = View.VISIBLE
                }
            },
            update = { view ->
                view.setBackgroundColor(backgroundColor.toArgb())
            }
        )

        LaunchedEffect(MaterialTheme.colorScheme.surface) {
            geckoView.get()?.setBackgroundColor(backgroundColor.toArgb())
        }

        DisposableEffect(Unit) {
            onDispose {
                // Close the session when engine is disposed
                geckoSession.get()?.close()
            }
        }
    }

    private fun updateNavigationStates() {
        // Navigation states are updated via delegates
        // This method can be used for additional state updates if needed
    }

    @Composable
    override fun PageIcon() {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        return "GeckoView"
    }

    override fun getVersion(): String {
        return org.mozilla.geckoview.BuildConfig.MOZ_APP_VERSION
    }

    override fun getDefaultUA(): String {
        return geckoSession.get()?.settings?.userAgentOverride ?: "GeckoView"
    }

    override fun loadUrl(url: String) {
        _currentUrl.value = url
        geckoSession.get()?.loadUri(url)
    }

    override fun stopLoading() {
        geckoSession.get()?.stop()
    }

    override fun refresh() {
        geckoSession.get()?.reload()
    }

    override fun goBack() {
        if (_canGoBack.value) {
            geckoSession.get()?.goBack()
        }
    }

    override fun goForward() {
        if (_canGoForward.value) {
            geckoSession.get()?.goForward()
        }
    }
}
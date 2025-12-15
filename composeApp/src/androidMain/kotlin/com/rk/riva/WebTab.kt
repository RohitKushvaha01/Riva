package com.rk.riva

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.composables.icons.lucide.ArrowRight
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.Download
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.RefreshCw
import com.rk.riva.TabManager.currentTab
import kotlinx.coroutines.delay
import org.json.JSONObject
import org.mozilla.geckoview.AllowOrDeny
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView
import org.mozilla.geckoview.WebResponse

// Tab-specific GeckoSession wrapper
class TabSession(context: android.content.Context, initialUrl: String = "about:blank") {
    private val runtime = GeckoManager.getOrCreateRuntime(context)
    val session = GeckoSession()

    // Observable states
    val isLoading = mutableStateOf(false)
    val canGoBack = mutableStateOf(false)
    val canGoForward = mutableStateOf(false)
    val pageTitle = mutableStateOf("New Tab")
    val currentUrl = mutableStateOf(initialUrl)

    init {
        session.apply {
            // Progress delegate
            progressDelegate = object : GeckoSession.ProgressDelegate {
                override fun onPageStart(session: GeckoSession, url: String) {
                    isLoading.value = true
                    currentUrl.value = url
                }

                override fun onPageStop(session: GeckoSession, success: Boolean) {
                    isLoading.value = false
                }

                override fun onProgressChange(session: GeckoSession, progress: Int) {}
                override fun onSecurityChange(
                    session: GeckoSession,
                    securityInfo: GeckoSession.ProgressDelegate.SecurityInformation
                ) {}
                override fun onSessionStateChange(
                    session: GeckoSession,
                    sessionState: GeckoSession.SessionState
                ) {}
            }

            // Content delegate
            contentDelegate = object : GeckoSession.ContentDelegate {
                override fun onTitleChange(session: GeckoSession, title: String?) {
                    pageTitle.value = title ?: "Untitled"
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
                    manifest: JSONObject
                ) {}
                override fun onMetaViewportFitChange(session: GeckoSession, viewportFit: String) {}
                override fun onShowDynamicToolbar(session: GeckoSession) {}
            }

            // Navigation delegate
            navigationDelegate = object : GeckoSession.NavigationDelegate {
                override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
                    this@TabSession.canGoBack.value = canGoBack
                }

                override fun onCanGoForward(session: GeckoSession, canGoForward: Boolean) {
                    this@TabSession.canGoForward.value = canGoForward
                }

                override fun onLocationChange(
                    session: GeckoSession,
                    url: String?,
                    perms: MutableList<GeckoSession.PermissionDelegate.ContentPermission>,
                    hasUserGesture: Boolean
                ) {
                    currentUrl.value = url ?: ""
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
                    // Handle target="_blank" - create a new tab
                    // You can emit an event here to create a new WebTab
                    return null
                }
            }

            // Open session with runtime
            open(runtime)
        }
    }

    fun loadUrl(url: String) {
        session.loadUri(url)
    }

    fun goBack() {
        if (canGoBack.value) {
            session.goBack()
        }
    }

    fun goForward() {
        if (canGoForward.value) {
            session.goForward()
        }
    }

    fun reload() {
        session.reload()
    }

    fun stop() {
        session.stop()
    }

    fun close() {
        session.close()
    }
}

class WebTab() : BaseTab() {

    private var initUrl by mutableStateOf("about:blank")
    private var tabSession: TabSession? = null

    constructor(url: String) : this() {
        this.initUrl = url
    }

    override var showToolBar: MutableState<Boolean> = mutableStateOf(true)

    @Composable
    override fun Content() {
        val context = LocalContext.current

        // Create session once
        val session = remember {
            TabSession(context, initUrl).also {
                tabSession = it
            }
        }

        BackHandler(enabled = currentTab == this) {
            if (session.canGoBack.value) {
                session.goBack()
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            ProgressBar(isLoading = session.isLoading.value)

            GeckoViewContent(
                session = session,
                modifier = Modifier.weight(1f)
            )

            LaunchedEffect(Unit) {
                if (initUrl != "about:blank") {
                    session.loadUrl(initUrl)
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                // Clean up the session when tab is closed
                tabSession?.close()
                tabSession = null
            }
        }
    }

    fun getSession(): TabSession? = tabSession

    override fun getTitle(): String {
        return tabSession?.pageTitle?.value ?: "New Tab"
    }

    override fun onClose() {
        tabSession?.close()
    }

    @Composable
    override fun ColumnScope.MenuItems(onDismissRequest:()-> Unit) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Top) {
            //go forward
            IconButton(
                enabled = getSession()?.canGoForward?.value == true,
                onClick = {
                    onDismissRequest()
                    getSession()?.goForward()
                }){
                Icon(modifier = Modifier.size(20.dp),imageVector = Lucide.ArrowRight, contentDescription = null)
            }
            IconButton(onClick = {
                onDismissRequest()
            }){
                Icon(modifier = Modifier.size(20.dp),imageVector = Lucide.Bookmark,contentDescription = null)
            }
            //page info
            IconButton(onClick = {
                onDismissRequest()
            }){
                Icon(modifier = Modifier.size(20.dp),imageVector = Lucide.Info,contentDescription = null)
            }
            IconButton(onClick = {
                onDismissRequest()
            }){
                Icon(modifier = Modifier.size(20.dp),imageVector = Lucide.Download,contentDescription = null)
            }

            IconButton(
                enabled = true,
                onClick = {
                    onDismissRequest()
                    getSession()?.reload()
                }){
                Icon(modifier = Modifier.size(20.dp),imageVector = Lucide.RefreshCw,contentDescription = null)
            }
        }

    }
}

@Composable
fun GeckoViewContent(
    session: TabSession,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.surface
    var geckoView by remember { mutableStateOf<GeckoView?>(null) }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            GeckoView(ctx).apply {
                setBackgroundColor(backgroundColor.toArgb())
                setSession(session.session)
                geckoView = this
            }
        },
        update = { view ->
            view.setBackgroundColor(backgroundColor.toArgb())
            // Ensure session is set (in case of recomposition)
            if (view.session != session.session) {
                view.setSession(session.session)
            }
        }
    )

    LaunchedEffect(backgroundColor) {
        geckoView?.setBackgroundColor(backgroundColor.toArgb())
    }
}

@Composable
fun ProgressBar(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    var targetProgress by remember { mutableFloatStateOf(0f) }

    // Simulate Chrome's progress behavior
    LaunchedEffect(isLoading) {
        if (isLoading) {
            targetProgress = 0f
            // Quick jump to 20%
            targetProgress = 0.2f
            delay(100)
            // Slower progress to 50%
            targetProgress = 0.5f
            delay(300)

            targetProgress = 0.7f
            delay(500)

            // Very slow progress to 90%
            targetProgress = 0.9f
        } else {
            // Complete the progress
            targetProgress = 0.99f
            delay(10)
            targetProgress = 1f
            delay(200)
            // Reset after completion
            targetProgress = 0f
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = if (targetProgress == 1f) 200 else 800,
            easing = FastOutSlowInEasing
        ),
        label = "progress"
    )

    // Only show when there's progress or loading
    if (isLoading || (targetProgress > 0f && targetProgress < 1f)) {
        LinearProgressIndicator(
            progress = { if (isLoading){animatedProgress}else{0f} },
            modifier = modifier
                .fillMaxWidth()
                .height(2.dp),
            color = color,
            trackColor = trackColor,
            gapSize = 0.dp,
            drawStopIndicator = {}
        )
    }
}
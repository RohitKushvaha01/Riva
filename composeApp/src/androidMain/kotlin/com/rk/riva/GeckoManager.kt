package com.rk.riva

import android.content.Context
import android.os.Handler
import android.os.Looper
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings

object GeckoManager {
    private var runtime: GeckoRuntime? = null
    private var isInitializing = false

    /**
     * Gets or creates the GeckoRuntime instance.
     * Must be called from the main thread.
     * Safe to call multiple times - will return existing instance if available.
     */
    fun getOrCreateRuntime(context: Context): GeckoRuntime {
        // Ensure we're on the main thread
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw IllegalStateException("GeckoRuntime must be created on the main thread")
        }

        if (runtime == null && !isInitializing) {
            isInitializing = true
            try {
                runtime = createRuntime(context.applicationContext)
            } finally {
                isInitializing = false
            }
        }

        // Wait for initialization to complete if in progress
        while (isInitializing && runtime == null) {
            Thread.sleep(10)
        }

        return runtime ?: throw IllegalStateException("Failed to create GeckoRuntime")
    }

    /**
     * Initialize GeckoRuntime early in the application lifecycle.
     * Call this from your Activity's onCreate, not from Application class.
     */
    fun initialize(context: Context) {
        if (runtime == null) {
            Handler(Looper.getMainLooper()).post {
                getOrCreateRuntime(context)
            }
        }
    }

    /**
     * Creates a new GeckoRuntime with recommended settings.
     */
    private fun createRuntime(context: Context): GeckoRuntime {
        val settings = GeckoRuntimeSettings.Builder()
            .apply {
                // Enable remote debugging in debug builds
                // if (BuildConfig.DEBUG) {
                //     remoteDebuggingEnabled(true)
                // }

                // Configure other settings as needed
                javaScriptEnabled(true)

                // Enable web fonts
                webFontsEnabled(true)

                // Configure content blocking (optional)
                // contentBlocking()
                //     .antiTracking(ContentBlocking.AntiTracking.DEFAULT)
                //     .safeBrowsing(ContentBlocking.SafeBrowsing.DEFAULT)

                // Set user agent mode (mobile/desktop)
                // userAgentMode(GeckoRuntimeSettings.USER_AGENT_MODE_MOBILE)
            }
            .build()

        return GeckoRuntime.create(context, settings)
    }

    /**
     * Check if runtime is initialized without creating it.
     */
    fun isInitialized(): Boolean = runtime != null

    /**
     * Shutdown the runtime (call when app is being destroyed).
     * Note: This should rarely be called as GeckoRuntime should persist.
     */
    fun shutdown() {
        runtime?.shutdown()
        runtime = null
    }
}

// Extension function for easy access
fun Context.getGeckoRuntime(): GeckoRuntime {
    return GeckoManager.getOrCreateRuntime(this)
}
package com.rk.riva

actual fun getWebTab(url: String): BaseTab {
    return WebTab(url)
}
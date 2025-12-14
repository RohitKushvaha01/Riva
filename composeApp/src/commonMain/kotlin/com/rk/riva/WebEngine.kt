package com.rk.riva

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier


interface WebEngine{
    @Composable
    fun EngineIcon()

    @Composable
    fun Content(modifier: Modifier)

    @Composable
    fun PageIcon()

    fun getName(): String
    fun getVersion(): String
    fun getDefaultUA(): String
    fun loadUrl(url: String)
    fun stopLoading()
    fun refresh()
    fun goBack()
    fun goForward()

    val isLoading: State<Boolean>
    val canGoBack: State<Boolean>
    val canGoForward: State<Boolean>
    val pageTitle: State<String>

}

expect fun getPlatformEngines(): List<WebEngine>

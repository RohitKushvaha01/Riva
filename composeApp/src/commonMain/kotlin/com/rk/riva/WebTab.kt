package com.rk.riva

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rk.riva.TabManager.currentTab
import kotlinx.coroutines.delay



class WebTab() : BaseTab(){

    private var initUrl by mutableStateOf("about:blank")
    constructor(url: String):this(){
        this.initUrl = url
    }

    override var showToolBar: MutableState<Boolean> = mutableStateOf(true)
    private val engine = getPlatformEngines().first()

    @Composable
    override fun Content() {
        BackHandler(enabled = currentTab == this){
            if (engine.canGoBack.value){
                engine.goBack()
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            ProgressBar(isLoading = engine.isLoading.value)
            engine.Content(modifier = Modifier.weight(1f))

            LaunchedEffect(Unit){
                engine.loadUrl(initUrl)
            }
        }

    }

    fun getEngine(): WebEngine{
        return engine
    }

    override fun getTitle(): String {
        TODO("Not yet implemented")
    }

    override fun onClose() {
        TODO("Not yet implemented")
    }

    @Composable
    override fun ColumnScope.MenuItems() {
        TODO("Not yet implemented")
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
            // Quick jump to 30%
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
            kotlinx.coroutines.delay(200)
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
    if (isLoading || targetProgress > 0f && targetProgress < 1f) {
        LinearProgressIndicator(
            progress = { animatedProgress },
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
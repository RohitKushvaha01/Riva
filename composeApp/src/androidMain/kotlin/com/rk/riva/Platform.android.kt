package com.rk.riva

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.drawable.toDrawable

@Composable
actual fun setWindowBackground(color: Color) {
    // Set window background based on theme
    val backgroundColor = MaterialTheme.colorScheme.surface
    val view = LocalView.current

    LaunchedEffect(backgroundColor) {
        val window = (view.context as? Activity)?.window
        window?.setBackgroundDrawable(
            backgroundColor.toArgb().toDrawable()
        )
    }
}
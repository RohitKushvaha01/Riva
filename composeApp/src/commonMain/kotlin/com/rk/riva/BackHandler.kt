package com.rk.riva

import androidx.compose.runtime.Composable

// BackHandlerHelper.kt in commonMain
@Composable
expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)
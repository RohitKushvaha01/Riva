package com.rk.riva

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.rk.riva.theme.RivaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    RivaTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            Column(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize()
            ) {
                var searchActive by remember { mutableStateOf(false) }

                AnimatedVisibility(
                    visible = !searchActive,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    TopIconRow()
                }

                HomeAddressBar(searchActive = searchActive) {
                    searchActive = it
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                }
            }
        }
    }

}
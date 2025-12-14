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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rk.riva.TabManager.currentTab
import com.rk.riva.theme.RivaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    RivaTheme {
        setWindowBackground(color = MaterialTheme.colorScheme.surface)
        Surface {
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                Column(
                    modifier = Modifier
                        .safeContentPadding()
                        .fillMaxSize()
                ) {

                    AnimatedVisibility(
                        visible = currentTab.showToolBar.value,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        ToolBar()
                    }

                    if (currentTab.showToolBar.value && currentTab !is HomeTab){
                        HorizontalDivider()
                    }



                    //tab content
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        currentTab.Content()
                    }





                }


            }
        }

    }

}
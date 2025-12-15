package com.rk.riva

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State

abstract class BaseTab {
    abstract val showToolBar: MutableState<Boolean>
    @Composable
    abstract fun Content()

    abstract fun getTitle(): String
    abstract fun onClose()

    @Composable
    abstract fun ColumnScope.MenuItems(onDismissRequest:()->Unit)
}
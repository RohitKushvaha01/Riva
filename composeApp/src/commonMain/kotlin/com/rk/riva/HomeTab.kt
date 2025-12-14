package com.rk.riva

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeTab() : BaseTab() {

    override var showToolBar: MutableState<Boolean> = mutableStateOf(true)
    @Composable
    override fun Content() {
        var searchActive by rememberSaveable { mutableStateOf(false) }
        HomeAddressBar(modifier = Modifier, searchActive = searchActive, onActiveChange =  {
            searchActive = it
            showToolBar.value = !it
        }, onSearch = {
            GlobalScope.launch(Dispatchers.Main) {
                delay(100)
                TabManager.replaceTab(this@HomeTab, WebTab(it))
            }
        })
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
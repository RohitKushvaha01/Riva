package com.rk.riva

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*

object TabManager {

    private val _tabs = mutableStateListOf<BaseTab>()
    val tabs: List<BaseTab> get() = _tabs

    private var _currentTab by mutableStateOf<BaseTab>(HomeTab())
    val currentTab: BaseTab get() = _currentTab

    init {
        _tabs.add(_currentTab)
    }

    fun newTab(tab: BaseTab, switchToTab: Boolean = true) {
        _tabs.add(tab)
        if (switchToTab) {
            _currentTab = tab
        }
    }

    fun removeTab(tab: BaseTab) {
        if (_tabs.size == 1) {
            // Don't allow removing the last tab, replace it with HomeTab
            _tabs[0].onClose()
            val newHome = HomeTab()
            _tabs[0] = newHome
            _currentTab = newHome
            return
        }

        val index = _tabs.indexOf(tab)
        if (index == -1) return // Tab not found

        tab.onClose()

        _tabs.removeAt(index)

        // Update currentTab if we removed it
        if (_currentTab == tab) {
            // Switch to previous tab, or first tab if we removed index 0
            _currentTab = _tabs[if (index > 0) index - 1 else 0]
        }
    }

    fun clearTabs() {
        _tabs.forEach { it.onClose() }
        _tabs.clear()
        val newHome = HomeTab()
        _tabs.add(newHome)
        _currentTab = newHome
    }

    fun replaceTab(thisTab: BaseTab, toThis: BaseTab) {
        if (thisTab == toThis) return

        thisTab.onClose()

        val index = _tabs.indexOf(thisTab)
        if (index == -1) return // Tab not found

        _tabs[index] = toThis

        // Update currentTab if we replaced it
        if (_currentTab == thisTab) {
            _currentTab = toThis
        }
    }

    fun switchToTab(tab: BaseTab) {
        if (tab in _tabs) {
            _currentTab = tab
        }
    }
}
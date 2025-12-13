package com.rk.riva

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.CircleStop
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressBar(searchActive: Boolean,onActiveChange:(Boolean)-> Unit){
    var searchQuery by remember { mutableStateOf("") }


    BackHandler(enabled = searchActive){
        onActiveChange(false)
    }

    // 🔹 Search bar (moves to top)
    val topPadding by animateDpAsState(
        targetValue = if (searchActive) 0.dp else 16.dp,
        label = "searchPadding"
    )

    val colors1 = SearchBarDefaults.colors()

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { onActiveChange(false) },
                expanded = searchActive,
                onExpandedChange = onActiveChange,
                placeholder = { Text("Search") },
                colors = colors1.inputFieldColors,
                leadingIcon = {
                    Icon(modifier = Modifier.size(22.dp), imageVector = Lucide.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchActive && searchQuery.isNotEmpty()){
                        Icon(modifier = Modifier.size(22.dp), imageVector = Lucide.CircleStop, contentDescription = null)
                    }
                }
            )
        },
        expanded = searchActive,
        onExpandedChange = onActiveChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (searchActive){0.dp}else{16.dp})
            .padding(top = topPadding),
        shape = SearchBarDefaults.inputFieldShape,
        colors = colors1,
        windowInsets = SearchBarDefaults.windowInsets,
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = SearchBarDefaults.ShadowElevation,
        // Search suggestions/results content goes here
        // This content is shown when searchActive is true
        content = { // Search suggestions/results content goes here
            // This content is shown when searchActive is true
        },
    )
}
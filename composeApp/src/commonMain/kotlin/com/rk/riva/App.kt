package com.rk.riva

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.CircleStop
import com.composables.icons.lucide.Dice1
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Menu
import com.composables.icons.lucide.Search
import com.rk.riva.theme.RivaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    RivaTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)){
            Column(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize()
            ) {
                var searchQuery by remember { mutableStateOf("") }
                var searchActive by remember { mutableStateOf(false) }

                BackHandler(enabled = searchActive){
                    searchActive = false
                }

                // 🔹 Top row (icons)
                AnimatedVisibility(
                    visible = !searchActive,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    TopIconRow()
                }

                // 🔹 Search bar (moves to top)
                val topPadding by animateDpAsState(
                    targetValue = if (searchActive) 0.dp else 16.dp,
                    label = "searchPadding"
                )

                val onActiveChange:(Boolean)-> Unit = { searchActive = it }
                val colors1 = SearchBarDefaults.colors()
                // Search suggestions/results content goes here
                // This content is shown when searchActive is true
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onSearch = { searchActive = false },
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

                // 🔹 Rest of content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ){

                }
            }
        }
        }

}

@Composable
fun TopIconRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(Lucide.House, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface)
        }

        Spacer(Modifier.weight(1f))

        IconButton(onClick = {}) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "R",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 12.sp
                )
            }
        }

        IconButton(onClick = {}) {
            Icon(Lucide.Dice1, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface)
        }

        IconButton(onClick = {}) {
            Icon(Lucide.Menu, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface)
        }
    }
}
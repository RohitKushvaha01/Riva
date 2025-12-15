package com.rk.riva

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.X
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

                    if (currentTab.showToolBar.value && currentTab !is HomeTab) {
                        HorizontalDivider()
                    }

                    // Tab content
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        currentTab.Content()
                    }

                    // Bottom tab row
                    BottomTabRow(
                        tabs = TabManager.tabs,
                        currentTab = currentTab,
                        onTabClick = { tab ->
                            TabManager.switchToTab(tab)
                        },
                        onTabClose = { tab ->
                            TabManager.removeTab(tab)
                        },
                        onNewTab = {
                            TabManager.newTab(HomeTab())
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomTabRow(
    tabs: List<BaseTab>,
    currentTab: BaseTab,
    onTabClick: (BaseTab) -> Unit,
    onTabClose: (BaseTab) -> Unit,
    onNewTab: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 3.dp,
        shadowElevation = 8.dp
    ) {
        Column {
            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Scrollable tabs
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabs.forEach { tab ->
                        TabChip(
                            tab = tab,
                            isSelected = tab == currentTab,
                            onClick = { onTabClick(tab) },
                            onClose = { onTabClose(tab) }
                        )
                    }
                }

                // New tab button
                FilledTonalIconButton(
                    onClick = onNewTab,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Lucide.Plus,
                        contentDescription = "New Tab"
                    )
                }
            }
        }
    }
}

@Composable
fun TabChip(
    tab: BaseTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier
            .height(40.dp)
            .widthIn(min = 80.dp, max = 200.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        color = backgroundColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Tab title
            Text(
                text = tab.getTitle(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            // Close button
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Lucide.X,
                    contentDescription = "Close Tab",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// Alternative: Compact Tab Row with Tab Count
@Composable
fun CompactBottomTabRow(
    tabs: List<BaseTab>,
    currentTab: BaseTab,
    onTabClick: (BaseTab) -> Unit,
    onTabClose: (BaseTab) -> Unit,
    onNewTab: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 3.dp,
        shadowElevation = 8.dp
    ) {
        Column {
            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tab count badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "${tabs.indexOf(currentTab) + 1}/${tabs.size}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                // Current tab title
                Text(
                    text = currentTab.getTitle(),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                )

                // Action buttons
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    // Close current tab
                    if (tabs.size > 1) {
                        FilledTonalIconButton(
                            onClick = { onTabClose(currentTab) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Lucide.X,
                                contentDescription = "Close Tab",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // New tab
                    FilledTonalIconButton(
                        onClick = onNewTab,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Lucide.Plus,
                            contentDescription = "New Tab",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// Usage Example:
// Replace BottomTabRow() in App() with CompactBottomTabRow() for a more compact design
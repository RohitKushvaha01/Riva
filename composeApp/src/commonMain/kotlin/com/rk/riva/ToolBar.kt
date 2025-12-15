package com.rk.riva

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Dice1
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Menu
import com.rk.riva.TabManager.currentTab

@Composable
fun ToolBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            TabManager.replaceTab(currentTab, HomeTab())
        }) {
            Icon(Lucide.House, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface)
        }


//        HomeAddressBar(modifier = Modifier
//            .weight(1f)
//            ,false, onActiveChange = {})

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


        var expanded by remember { mutableStateOf(false) }
        Box{
            IconButton(onClick = {
                expanded = true
            }) {
                Icon(Lucide.Menu, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface)
            }
            MainMenu(show = expanded){
                expanded = false
            }
        }

    }
}
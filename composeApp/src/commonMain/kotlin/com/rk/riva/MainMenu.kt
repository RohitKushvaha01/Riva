package com.rk.riva

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowRight
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.Download
import com.composables.icons.lucide.History
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.RefreshCw
import com.composables.icons.lucide.Settings

@Composable
fun MainMenu(show: Boolean,onDismissRequest:()-> Unit){
    DropdownMenu(expanded = show, onDismissRequest = {
        onDismissRequest()
    }){
        Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Top) {
            //go forward
            IconButton(onClick = {}){
                Icon(modifier = Modifier.size(20.dp),imageVector = Lucide.ArrowRight, contentDescription = null)
            }
            IconButton(onClick = {}){
                //use filled star?
                Icon(modifier = Modifier.size(20.dp),imageVector = Lucide.Bookmark,contentDescription = null)
            }
            //page info
            IconButton(onClick = {}){
                Icon(modifier = Modifier.size(20.dp),imageVector = Lucide.Info,contentDescription = null)
            }
            IconButton(onClick = {}){
                Icon(modifier = Modifier.size(20.dp),imageVector = Lucide.Download,contentDescription = null)
            }

            IconButton(onClick = {}){
                Icon(modifier = Modifier.size(20.dp),imageVector = Lucide.RefreshCw,contentDescription = null)
            }
        }

        DropdownMenuItem(
            text = { Text("New Tab") },
            onClick = {
               onDismissRequest()
            },
            leadingIcon = {
                Icon(Lucide.Plus, contentDescription = null)
            }
        )


        DropdownMenuItem(
            text = { Text("New Incognito tab") },
            onClick = {
                onDismissRequest()
            },
            leadingIcon = {
                Icon(Lucide.Lock, contentDescription = null)
            }
        )

        DropdownMenuItem(
            text = { Text("History") },
            onClick = {
                onDismissRequest()
            },
            leadingIcon = {
                Icon(Lucide.History, contentDescription = null)
            }
        )

        DropdownMenuItem(
            text = { Text("Downloads") },
            onClick = {
                onDismissRequest()
            },
            leadingIcon = {
                Icon(Lucide.Download, contentDescription = null)
            }
        )

        DropdownMenuItem(
            text = { Text("Bookmarks") },
            onClick = {
                onDismissRequest()
            },
            leadingIcon = {
                Icon(Lucide.Bookmark, contentDescription = null)
            }
        )

        DropdownMenuItem(
            text = { Text("Settings") },
            onClick = {
                onDismissRequest()
            },
            leadingIcon = {
                Icon(Lucide.Settings, contentDescription = null)
            }
        )






    }
}
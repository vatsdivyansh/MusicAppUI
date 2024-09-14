package com.example.musicappui.ui.theme

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import com.example.musicappui.R

@Composable
fun Browse(){
    val categories = listOf("Hits","happy","Workout","Running","TGIF","Yoga")
    LazyVerticalGrid(GridCells.Fixed(2)){
        items(categories){cat->
            BrowserItem(cat = cat , drawable = R.drawable.baseline_apps_24)
        }
    }

}

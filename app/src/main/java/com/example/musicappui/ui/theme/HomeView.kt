package com.example.musicappui.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicappui.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(){
    val categories = listOf("Hits","happy","Workout","Running","TGIF","Yoga")
    val grouped = listOf<String>("New Release","Favourites","Top rated").groupBy { it[0] }
    LazyColumn{
        grouped.forEach{
            stickyHeader {
                Text(text = it.value[0],modifier = Modifier.padding(16.dp))
                LazyRow(){
                    // lazy row allows us to scroll lef to right same as lazy column allows us to scroll up and down
                    items(categories){
                        cat->
                        BrowserItem(cat = cat , drawable = R.drawable.baseline_search_24)
                    }
                }
            }
        }

    }
}
@Composable
// defining how an browser item must look like

fun BrowserItem(cat:String,drawable:Int){
    Card(modifier = Modifier
        .padding(16.dp)
        .size(200.dp) ,
        border = BorderStroke(3.dp,color= Color.DarkGray)) {
        Column(verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = cat)
            Image(painter = painterResource(id = drawable), contentDescription = cat)


        }

    }



}

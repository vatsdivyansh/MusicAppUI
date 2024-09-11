package com.example.musicappui.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(){
    val scaffoldState: ScaffoldState = rememberScaffoldState() // to manage and remember the state of the Scaffold across recompositions . It allows us to control elements like drawer,snackBar , ensuring consistent behaviour even when the UI updates
    val scope: CoroutineScope = rememberCoroutineScope()



    // scaffold is basically just the parent UI element or the view for the entire page which contains the TopBar , content and the BottomBar
    Scaffold(
        topBar = {
            TopAppBar(title = {Text("Home")} ,
                navigationIcon = { IconButton(onClick = {
                    // onClick event here opens up the drawer
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }

                }) {
                    Icon(imageVector = Icons.Default.AccountCircle , contentDescription = null)
                }}
            )
        }
    ) {
        Text("Text" , modifier = Modifier
            .fillMaxSize()
            .padding(it))
    }
}





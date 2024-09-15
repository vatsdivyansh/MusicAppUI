package com.example.musicappui.ui.theme

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.musicappui.Screen
import com.example.musicappui.screensInDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicappui.MainViewModel
import com.example.musicappui.R
import com.example.musicappui.screenInBottom


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView(){
    val scaffoldState: ScaffoldState = rememberScaffoldState() // to manage and remember the state of the Scaffold across recompositions . It allows us to control elements like drawer,snackBar , ensuring consistent behaviour even when the UI updates
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val isSheetFullScreen by remember {
        mutableStateOf(false )
    }
    val modifier = if(isSheetFullScreen)Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    // below three lines allows us to find out on which "View" we are currently at
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val dialogOpen = remember{
        mutableStateOf(false)
    }
    val currentScreen = remember{
        viewModel.currentScreen.value
    }
    val title  = remember {
        // TODO--> To change that to CurrentScreen.title
//        mutableStateOf("")
            mutableStateOf(currentScreen.title) // now our title will be dynamic
    }

    val modalSheetState  = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,  confirmValueChange = {it != ModalBottomSheetValue.HalfExpanded})
    val roundedCornerRadius = if(isSheetFullScreen) 0.dp else 12.dp
    val bottomBar : @Composable () -> Unit = {
        if(currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home){
            BottomNavigation(modifier = Modifier.wrapContentSize()) {
                screenInBottom.forEach{
                    item->
                    val isSelected = currentRoute == item.bRoute
                    Log.d("Navigation", "Item: ${item.bTitle}, isSelected: $isSelected, route: ${item.bRoute}")
                    val tint = if(isSelected)Color.White else Color.Black
                    BottomNavigationItem(selected = currentRoute == item.bRoute, onClick = { controller.navigate(item.bRoute) }, icon = {

                        Icon( tint=tint,contentDescription = item.bTitle ,painter =  painterResource(id = item.icon) )
                    } , label = {Text(text = item.bTitle , color = tint)} ,
                        selectedContentColor = Color.White ,
                        unselectedContentColor = Color.Black
                        )
                }

            }

        }
    }
     // Modal bottom sheets present a set of choices while blocking interaction with the rest of the screen. They are an alternative to inline menus and simple dialogs, providing additional room for content, iconography, and actions.
    ModalBottomSheetLayout(
        sheetState = modalSheetState ,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius , topEnd = roundedCornerRadius),
        sheetContent = {
        MoreBottomSheet(modifier = Modifier)  // we need to define MoreBottomSheet by ourself
    }) {
        Scaffold(
            bottomBar = bottomBar,
            topBar = {
                TopAppBar(title = {Text(title.value)} ,
                    actions = {
                              IconButton(onClick = {
                                  scope.launch {
                                      if(modalSheetState.isVisible)modalSheetState.hide()
                                      else modalSheetState.show()
                                  }
                              }){
                                  Icon(imageVector = Icons.Default.MoreVert, contentDescription = null )
                              }
                    } ,
                    navigationIcon = { IconButton(onClick = {
                        // onClick event here opens up the drawer
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }

                    }) {
                        Icon(imageVector = Icons.Default.AccountCircle , contentDescription = null)
                    }}
                )
            } , scaffoldState = scaffoldState ,
            drawerContent = {


                LazyColumn(Modifier.padding(16.dp)){
                    items(screensInDrawer){
                            item->
                        DrawerItem(selected = currentRoute == item.dRoute , item = item) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            if(item.dRoute == "add_account"){
                                // open dialog
                                dialogOpen.value = true
                            }
                            else {
                                controller.navigate(item.dRoute)
                                title.value = item.dTitle
                            }
                        }
                    }
                }
            }
        )


        {
//        Text("Text" , modifier = Modifier
//            .fillMaxSize()
//            .padding(it))
            Navigation(navController = controller , viewModel = viewModel , pd = it)
            AccountDialog(dialogOpen = dialogOpen)
        }



    }







    // scaffold is basically just the parent UI element or the view for the entire page which contains the TopBar , content and the BottomBar
//    Scaffold(
//        bottomBar = bottomBar,
//        topBar = {
//            TopAppBar(title = {Text(title.value)} ,
//                navigationIcon = { IconButton(onClick = {
//                    // onClick event here opens up the drawer
//                    scope.launch {
//                        scaffoldState.drawerState.open()
//                    }
//
//                }) {
//                    Icon(imageVector = Icons.Default.AccountCircle , contentDescription = null)
//                }}
//            )
//        } , scaffoldState = scaffoldState ,
//        drawerContent = {
//
//
//            LazyColumn(Modifier.padding(16.dp)){
//                items(screensInDrawer){
//                    item->
//                    DrawerItem(selected = currentRoute == item.dRoute , item = item) {
//                        scope.launch {
//                            scaffoldState.drawerState.close()
//                        }
//                        if(item.dRoute == "add_account"){
//                            // open dialog
//                            dialogOpen.value = true
//                        }
//                        else {
//                            controller.navigate(item.dRoute)
//                            title.value = item.dTitle
//                        }
//                    }
//                }
//            }
//        }
//    )
//
//
//    {
////        Text("Text" , modifier = Modifier
////            .fillMaxSize()
////            .padding(it))
//        Navigation(navController = controller , viewModel = viewModel , pd = it)
//        AccountDialog(dialogOpen = dialogOpen)
//    }
}
@Composable
fun DrawerItem(
    selected: Boolean ,
    item: Screen.DrawerScreen ,
    onDrawerItemClicked : () ->Unit
){
    val background = if(selected) Color.DarkGray else Color.White
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(background)
            .clickable { onDrawerItemClicked }
    ){
        Icon(painter = painterResource(id = item.icon) , contentDescription = item.dTitle , Modifier.padding(end = 8.dp , top=4.dp))
        Text(text = item.dTitle,style= MaterialTheme.typography.h5,)
    }
}
@Composable
fun MoreBottomSheet(modifier: Modifier){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .background(MaterialTheme.colors.primarySurface)){
        Column(modifier = Modifier.padding(16.dp) , verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier=Modifier.padding(16.dp)) {
                    Icon( modifier = Modifier.padding(end=8.dp),painter = painterResource(id = R.drawable.baseline_settings_24), contentDescription = "Settings")
                    Text("Settings" , fontSize = 20.sp , color = Color.White)

                }
                Row(modifier=Modifier.padding(16.dp)) {
                 Icon( modifier = Modifier.padding(end=8.dp),painter = painterResource(id = R.drawable.baseline_share_24), contentDescription = "Share")
                    Text("Share" , fontSize = 20.sp , color = Color.White)

                 }
                Row(modifier=Modifier.padding(16.dp)) {
                    Icon( modifier = Modifier.padding(end=8.dp),painter = painterResource(id = R.drawable.baseline_help_24), contentDescription = "Help")
                    Text("Help" , fontSize = 20.sp , color = Color.White)

                 }
        }
    }

}

@Composable
fun Navigation(navController: NavController , viewModel: MainViewModel ,  pd:PaddingValues){
    NavHost(navController = navController as NavHostController, startDestination = Screen.DrawerScreen.Account.route, modifier = Modifier.padding(pd) ){
        composable(Screen.BottomScreen.Home.bRoute){
            // Add Home Screen
            Home()
        }
        composable(Screen.BottomScreen.Browse.route){
            // Add Browse Screen
            Browse()
        }
        composable(Screen.BottomScreen.Library.bRoute){
            //  Add Library Screen
            Library()
        }

        composable(Screen.DrawerScreen.Account.route){
            AccountView()

        }
        composable(Screen.DrawerScreen.Subscription.route){
            Subscription()
        }

    }
}





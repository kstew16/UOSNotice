package com.tiamoh.uosnotice.screen

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.tiamoh.uosnotice.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*


import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tiamoh.uosnotice.ui.theme.SemiGrayScreen
import com.tiamoh.uosnotice.ui.theme.UOSMain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleOwner


@Composable
fun StartNoticeScreen(
    navController: NavHostController,
    onMenuClicked: ()->Unit,
    onSettingsClicked:()->Unit
    //,modifier:Modifier = Modifier
){
    var backKeyPressedTime = 0L
    lateinit var toast:Toast
    val context = LocalContext.current
    val viewModel:LoginScreenViewModel
    //var noticeName: String by viewModel.screenTitle.(LocalLifecycleOwner.current){}

    var noticeName = "나중에 뷰모델로 구현"
    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                title = { Box(
                    modifier = Modifier
                        .background(SemiGrayScreen)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){Text(
                    color=Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    text = noticeName
                )} },
                navigationIcon = {
                    IconButton(onClick = { onMenuClicked() }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Notice Page List")
                    }
                },
                actions = {
                    IconButton(onClick = { onSettingsClicked() }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings Icon")
                    }
                }
            )
        }
    ) {
        BackHandler() {
            if(System.currentTimeMillis()>backKeyPressedTime+2000){
                backKeyPressedTime = System.currentTimeMillis()
                toast = Toast.makeText(context,"버튼을 한 번 더 눌러 앱을 종료하세요",Toast.LENGTH_LONG)
                toast.show()
            }else{
                toast.cancel()
                    val context = LocalContext as Context
                context.findActivity().finish()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(SemiGrayScreen)
        ){

            item{
                Button(onClick = {
                    // 뷰모델 처리하는게 낫지 않나
                    noticeName = "바뀜?"
                }) {

                }
            }
        }
    }


}


fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}